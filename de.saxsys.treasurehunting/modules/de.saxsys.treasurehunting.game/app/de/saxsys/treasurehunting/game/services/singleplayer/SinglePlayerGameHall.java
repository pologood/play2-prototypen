/**
 * 
 */
package de.saxsys.treasurehunting.game.services.singleplayer;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import play.libs.Akka;
import play.libs.F.Callback;
import play.libs.F.Callback0;
import play.mvc.WebSocket;
import play.i18n.Messages;
import play.mvc.Controller;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

import static akka.pattern.Patterns.ask;
import static java.util.concurrent.TimeUnit.*;

import com.avaje.ebean.Ebean;

import de.saxsys.treasurehunting.common.models.actions.Action;
import de.saxsys.treasurehunting.common.models.actions.ActionRequest;
import de.saxsys.treasurehunting.common.models.actions.ActionResponse;
import de.saxsys.treasurehunting.common.models.game.Counter;
import de.saxsys.treasurehunting.common.models.game.Game;
import de.saxsys.treasurehunting.common.models.playgrounds.Playground;
import de.saxsys.treasurehunting.common.models.playgrounds.Point;
import de.saxsys.treasurehunting.common.services.UserService;
import de.saxsys.treasurehunting.game.services.GameHall;
import de.saxsys.treasurehunting.game.services.Join;
import de.saxsys.treasurehunting.game.services.PlaygroundService;
import de.saxsys.treasurehunting.game.services.Quit;
import de.saxsys.treasurehunting.game.services.exceptions.GameCreationException;
import de.saxsys.treasurehunting.game.services.exceptions.GameJoinException;

/**
 * This class represents a game service for a single player game. It also
 * manages the web socket for the client.
 * 
 * @author stefan.illgen
 * 
 */
@SuppressWarnings("unused")
public class SinglePlayerGameHall extends GameHall {

	/**
	 * Creates the {@link Game} with {@link Game#hMode} for a single player.
	 * 
	 * @param username
	 *            The name of the user.
	 * @param gamename
	 *            The name of the game.
	 * @param countercolor
	 *            The color of the counter.
	 * @param playgroundname
	 *            The name of the playground.
	 * 
	 * @throws GameCreationException
	 *             Throws an {@link GameCreationException} in case of an error
	 *             while creating the {@link Game}.
	 * 
	 * @return The ID of the created {@link Game}.
	 */
	public static long createGame(String username, String gamename,
			int countercolor, String playgroundname)
			throws GameCreationException {

		// check preconditions
		if (username == null || username.trim().isEmpty())
			throw new GameCreationException(
					"Parameter username must not be null and must "
							+ "be a trimmed non empty String value.");
		if (gamename == null)
			throw new GameCreationException(
					"Parameter gamename must not be null and must "
							+ "be a trimmed non empty String value.");
		if (countercolor < 0 || countercolor > 0xffffff)
			throw new GameCreationException(
					"Integer value for countercolor must be in the "
							+ "range from 0 to 0xffffff.");
		if (playgroundname == null || playgroundname.trim().isEmpty())
			throw new GameCreationException(
					"Parameter playgroundname must not be null and "
							+ "must be a trimmed non empty String value.");

		// do it
		try {
			Playground playground = PlaygroundService
					.findPlayground(playgroundname);

			// create counter
			Counter counter = new Counter();
			counter.color = countercolor;
			counter.cards = 0;
			counter.user = UserService.findUser(username);
			counter.position = playground.startPoint;

			// create game
			Game game = new Game();
			game.name = gamename;
			game.superState = Game.STATE_CREATED;
			game.subState = 0;
			game.hMode = Game.H_MODE_SINGLEPLAYER;
			game.activeCounter = 0;
			game.counters.add(counter);
			game.playground = playground;

			game.save();

			// caches as active game
			Props props = new Props(SinglePlayerGameHall.class);
			activeGames.put(game.id, Akka.system().actorOf(props));

			return game.id;

		} catch (Exception e) {
			throw new GameCreationException(e.getLocalizedMessage());
		}
	}

	/**
	 * This operation enables a user given by its name (i.e. the user's ID) to
	 * join the {@link Game} identified by its ID. For this it sends an
	 * asynchronous {@link Join} message to the actor (i.e. {@link ActorRef})
	 * related to the mentioned {@link Game}. This message is handled by the
	 * {@link SinglePlayerGameHall#onReceive} operation.
	 * 
	 * Afterwards it sets the {@link Callback}s for receiving regularly web
	 * socket messages from the client and for handling the closing of the web
	 * socket initiated by the client (f.i. if a connection loss appears).
	 * 
	 * @param gameid
	 *            The ID of the {@link Game} the join is related to.
	 * @param username
	 *            The name of the {@link User}, who is joining the {@link Game}.
	 * @param in
	 *            The web socket's in channel, receiving messages from the
	 *            client.
	 * @param out
	 *            The web socket's out channel for sending messages to the
	 *            client.
	 */
	public static void join(final Long gameid, final String username,
			WebSocket.In<JsonNode> in, WebSocket.Out<JsonNode> out) {

		try {
			
			final Long counterId = findCounter(gameid, username).id;

			ActorRef actor = activeGames.get(gameid);
			
			String result = (String) Await.result(
					ask(actor, new Join(gameid, counterId,
							out), 1000000), Duration.create(1000, SECONDS));

			// if access was granted
			if ("OK".equals(result)) {

				// for every received message
				in.onMessage(new Callback<JsonNode>() {

					@SuppressWarnings("deprecation")
					public void invoke(JsonNode event) throws Throwable {

						// parse the ActionRequest
						ObjectMapper mapper = new ObjectMapper();
						ActionRequest actionRequest = mapper.readValue(event,
								new TypeReference<ActionRequest>() {
								});

						// send the action request the game actor
						activeGames.get(gameid).tell(actionRequest);
					}
				});

				// if the socket was closed by the client or through a
				// connection loss
				in.onClose(new Callback0() {

					@SuppressWarnings("deprecation")
					public void invoke() {
						// send a Quit message to the game actor to remove the
						// user's counter from members of the game
						activeGames.get(gameid).tell(
								new Quit(gameid, counterId));
					}

				});
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * This
	 * 
	 * @see {@link UntypedActor#onReceive}
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(Object message) throws Exception {

		if (message instanceof Join) {

			Join join = (Join) message;

			// if members already contains the counter id, throw an exception
			if (members.containsKey(join.counterID))

				throw new GameJoinException(
						"Internal server error: The user has already join the game with its counter.");

			else {

				// put it in relation to the game
				members.put(join.counterID, join.out);

				// if this is not an active game
				ActionRequest actionRequest = new ActionRequest();
				actionRequest.data = new Object[] { join.gameID, join.out };

				if (isActiveGame(join.gameID)) {
					// if connection loss and active game
					actionRequest.initializer = Action.TYPE_REINITIALIZE_GAME;
					// reinitialzie the game
					reinitializeGame(actionRequest);
				} else {
					// if it's not an active game yet
					actionRequest.initializer = Action.TYPE_INITIALIZE_GAME;
					// initialize the game
					initializeGame(actionRequest);
				}
				
				getSender().tell("OK");

			}
		} else

		if (message instanceof ActionRequest) {

			handleClientAction((ActionRequest) message);

		} else

		// Beim Schließen des Websockets (z.B. infolge eines Routenwechsels) ..
		if (message instanceof Quit) {

			Quit quit = (Quit) message;
			// remove game ID and socket out channel from the active members of
			// the game
			members.remove(quit.gameID);

		} else {
			unhandled(message);
		}
	}

	/**
	 * Handles an {@link ActionRequest} for horizontal game mode single player.
	 * 
	 * @param actionRequest
	 *            the {@link ActionRequest} to handle.
	 * 
	 * @see {@link GameHall#handleClientAction}
	 */
	@Override
	public void handleClientAction(ActionRequest actionRequest) {

		switch (actionRequest.initializer) {

//		case Action.TYPE_INITIALIZE_GAME:
//			initializeGame(actionRequest);
//			break;
//
//		case Action.TYPE_REINITIALIZE_GAME:
//			reinitializeGame(actionRequest);
//			break;

		case Action.TYPE_START_GAME:
			startGame(actionRequest);
			break;

		case Action.TYPE_FINISH_GAME:
			finishGame(actionRequest);
			break;

		case Action.TYPE_RESTART_GAME:
			restartGame(actionRequest);
			break;

		case Action.TYPE_CANCEL_GAME:
			cancelGame(actionRequest);
			break;

		case Action.TYPE_PAUSE_GAME:
			pauseGame(actionRequest);
			break;

		case Action.TYPE_RESUME_GAME:
			resumeGame(actionRequest);
			break;

		case Action.TYPE_MOVE:
			move(actionRequest);
			break;

		case Action.TYPE_THROW_DICE:
			throwDice(actionRequest);
			break;

		case Action.TYPE_DICE_REPEAT:
			diceRepeat(actionRequest);
			break;

		case Action.TYPE_POI:
			poi(actionRequest);
			break;

		}
	}

	private static void initializeGame(ActionRequest actionRequest) {

		final Game game = findGame((Long) actionRequest.data[0]);
		// change game state
		game.superState = Game.STATE_READY;
		game.subState = 0;
		// set active counter to first position in the list of counters
		game.activeCounter = 0;

		// construct the action response
		ActionResponse response = new ActionResponse();
		// define socket request initializing action
		response.initializer = Action.TYPE_INITIALIZE_GAME;
		// assemble socket response data
		response = assembleInitializeGameResponseData(game, response);

		@SuppressWarnings("unchecked")
		WebSocket.Out<JsonNode> out = (WebSocket.Out<JsonNode>) actionRequest.data[1];

		// finally write action response on the socket's out channel
		writeOut(response, out);

		// persist the game state
		game.save();
	}

	/**
	 * Assembles action response data for {@link Action.TYPE_INITIALIZE_GAME}.
	 */
	private static ActionResponse assembleInitializeGameResponseData(Game game,
			ActionResponse response) {

		game.counters.size();
		response.data = new Object[] { game.playground,
				game.counters.get(game.activeCounter) };
		// define response data
		response.data = new Object[] { 
				game.playground, 
				new Object[] { 
					game.counters.get(0), 
					new Object[] { game.counters.get(0) }  
				} 
			};
		// define follower actions
		response.followers = new ArrayList<Action>() {
			private static final long serialVersionUID = 1L;
			{
				add(Ebean.find(Action.class, 1));
				add(Ebean.find(Action.class, 2));
			}
		};

		return response;
	}

	private void startGame(ActionRequest actionRequest) {

		final Game game = findGame((Long) actionRequest.data[0]);
		// change game state
		game.superState = Game.STATE_ACTIVE;
		game.subState = 0;

		// TODO

	}

	private static ActionResponse assembleStartGameResponseData(Game game,
			ActionResponse response) {

		// TODO

		return response;
	}

	private void finishGame(ActionRequest actionRequest) {

		final Game game = findGame((Long) actionRequest.data[0]);
		// change game state
		game.superState = Game.STATE_FINISHED;
		game.subState = 0;

		// TODO
	}

	private static ActionResponse assembleFinishGameResponseData(Game game,
			ActionResponse response) {

		// TODO

		return response;
	}

	private void cancelGame(ActionRequest actionRequest) {

		final Game game = findGame((Long) actionRequest.data[0]);
		// change game state
		game.superState = Game.STATE_FINISHED;
		game.subState = 0;

		// TODO
	}

	private static ActionResponse assembleCancelGameResponseData(Game game,
			ActionResponse response) {

		// TODO

		return response;
	}

	private void pauseGame(ActionRequest actionRequest) {

		final Game game = findGame((Long) actionRequest.data[0]);
		// change game state
		game.superState = Game.STATE_PAUSED;
		game.subState = 0;

		// TODO
	}

	private static ActionResponse assemblePauseGameResponseData(Game game,
			ActionResponse response) {

		// TODO

		return response;
	}

	private void restartGame(ActionRequest actionRequest) {

		final Game game = findGame((Long) actionRequest.data[0]);
		// change game state
		game.superState = Game.STATE_READY;
		game.subState = 0;

		// TODO
	}

	private static ActionResponse assembleRestartGameResponseData(Game game,
			ActionResponse response) {

		// TODO

		return response;
	}

	private void resumeGame(ActionRequest actionRequest) {

		final Game game = findGame((Long) actionRequest.data[0]);
		// change game state
		game.superState = Game.STATE_ACTIVE;
		game.subState = 0;

		// TODO
	}

	private static ActionResponse assembleResumeGameResponseData(Game game,
			ActionResponse response) {

		// TODO

		return response;
	}

	private void move(ActionRequest actionRequest) {

		final Game game = findGame((Long) actionRequest.data[0]);
		// change game state
		game.superState = Game.STATE_ACTIVE;
		game.subState = Game.STATE_MOVING;

		// TODO
	}

	private static ActionResponse assembleMoveResponseData(Game game,
			ActionResponse response) {

		// TODO

		return response;
	}

	private void throwDice(ActionRequest actionRequest) {

		final Game game = findGame((Long) actionRequest.data[0]);
		// change game state
		game.superState = Game.STATE_ACTIVE;
		game.subState = Game.STATE_DICING;

		// TODO
	}

	private static ActionResponse assembleThrowDiceResponseData(Game game,
			ActionResponse response) {

		// TODO

		return response;
	}

	private void diceRepeat(ActionRequest actionRequest) {

		final Game game = findGame((Long) actionRequest.data[0]);
		// change game state
		game.superState = Game.STATE_ACTIVE;
		game.subState = Game.STATE_PERFORMING_DICE_REPEAT;

		// TODO
	}

	private static ActionResponse assembleDiceRepeatResponseData(Game game,
			ActionResponse response) {

		// TODO

		return response;
	}

	private void poi(ActionRequest actionRequest) {

		final Game game = findGame((Long) actionRequest.data[0]);

		// change game state
		game.superState = Game.STATE_ACTIVE;
		game.subState = Game.STATE_PERFORMING_POI;
		// set active counter to the next counter in List Game.counters
		game.activeCounter = (game.activeCounter++) % game.counters.size();

		// TODO
	}

	private static ActionResponse assemblePoiResponseData(Game game,
			ActionResponse response) {

		// TODO

		return response;
	}

	/**
	 * <b>Remark:</b> Game.STATE_CREATED will not be recognized, because socket
	 * connection isn't established at this state of game.
	 */
	private void reinitializeGame(ActionRequest actionRequest) {

		final Game game = findGame((Long) actionRequest.data[0]);

		// construct the action response
		ActionResponse response = new ActionResponse();
		// define socket request initializing action
		response.initializer = Action.TYPE_REINITIALIZE_GAME;

		// switch over current game state
		switch (game.superState) {

		// primary states of the game
		case Game.STATE_READY:
			response = assembleInitializeGameResponseData(game, response);
			break;

		case Game.STATE_ACTIVE:

			// switch over secondary states of the game
			switch (game.subState) {

			case Game.STATE_DICING:
				response = assembleThrowDiceResponseData(game, response);
				break;

			case Game.STATE_MOVING:
				response = assembleMoveResponseData(game, response);
				break;

			case Game.STATE_PERFORMING_POI:
				response = assemblePoiResponseData(game, response);
				break;

			case Game.STATE_PERFORMING_DICE_REPEAT:
				response = assembleDiceRepeatResponseData(game, response);
				break;

			}

			break;

		case Game.STATE_FINISHED:
			response = assembleFinishGameResponseData(game, response);
			break;

		case Game.STATE_PAUSED:
			response = assemblePauseGameResponseData(game, response);
			break;

		}

		@SuppressWarnings("unchecked")
		WebSocket.Out<JsonNode> out = (WebSocket.Out<JsonNode>) actionRequest.data[1];

		// finally write action response on the socket's out channel
		writeOut(response, out);

	}

	/**
	 * Writes the ActionResponse out to the socket's out channel.
	 * 
	 * @param response
	 *            The ActionResponse to write out.
	 * @param out
	 *            The socket's out channel.
	 */
	private static void writeOut(ActionResponse response,
			WebSocket.Out<JsonNode> out) {
		try {
			// map it to Json
			ObjectMapper mapper = new ObjectMapper();
			String sResult = mapper.writeValueAsString(response);
			JsonFactory factory = new JsonFactory();
			JsonParser jp = factory.createJsonParser(sResult);
			JsonNode actualObj = mapper.readTree(jp);

			// send it via web socket
			out.write(actualObj);

		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
