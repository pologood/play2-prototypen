/**
 * 
 */
package de.saxsys.treasurehunting.game.services;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonNode;

import play.mvc.WebSocket;
import akka.actor.ActorRef;
import akka.actor.UntypedActor;

import com.avaje.ebean.Ebean;

import de.saxsys.treasurehunting.common.models.actions.Action;
import de.saxsys.treasurehunting.common.models.actions.ActionRequest;
import de.saxsys.treasurehunting.common.models.game.Counter;
import de.saxsys.treasurehunting.common.models.game.Game;
import de.saxsys.treasurehunting.common.models.user.User;
import de.saxsys.treasurehunting.common.services.UserService;

/**
 * This class represents an abstract game service for managing arbitrary typed
 * games. It has one central method for handling client actions
 * {@link #handleClientAction(Action)}, which is abstract. Concrete classes must
 * implement it, to define the transitions for the game states for each games
 * modes.
 * 
 * @author stefan.illgen
 * 
 */
public abstract class GameHall extends UntypedActor{

	/**
	 * This static {@link Map} caches instances of active games on the server.
	 * It's key represents the game id. It's value identifies the
	 * {@link ActorRef}, which corresponds to the {@link Game}.
	 */
	protected static Map<Long, ActorRef> activeGames = new HashMap<Long, ActorRef>();

	/**
	 * This non-static {@link Map} caches all instances of {@link Counter}
	 * participating in the game. Each {@link Counter} is related to it's
	 * {@link WebSocket} out channel.
	 */
	protected Map<Long, WebSocket.Out<JsonNode>> members = new HashMap<Long, WebSocket.Out<JsonNode>>();

	/**
	 * Checker for identifying active games via its ID.
	 * 
	 * @param id
	 *            The ID of the game.
	 * @return Return true, if this is an active game, and otherwise false.
	 */
	public static boolean isActiveGame(long id) {
		Game game = findGame(id);
		return activeGames.containsKey(id) && game != null
				&& game.superState != Game.STATE_CREATED;
	}

	/**
	 * Returns the {@link Game} identified by its ID.
	 * 
	 * @param id
	 *            The ID of the game.
	 * @return Returns the {@link Game} identified by its ID or null if the
	 *         {@link Game} wasn't found.
	 */
	protected static Game findGame(long id) {
		return Ebean.find(Game.class, id);
	}

	/**
	 * Finds the {@link Game}s name belonging to the given game ID.
	 * 
	 * @param id
	 *            The ID of the Game.
	 * @return Return the {@link Game}s name belonging to the given game ID or
	 *         null if the Game was not found.
	 */
	public static String findGameName(Long id) {
		Game game = findGame(id);
		if (game != null)
			return game.name;
		else
			return null;
	}

	/**
	 * Find the {@link Counter}s color value (hexa).
	 * 
	 * @param id
	 *            The {@link Counter}s ID.
	 * @return Returns the {@link Counter}s color value (hexa) or -1 if the
	 *         {@link Counter} was not found.
	 */
	public static int findCounterColor(long id) {
		Counter counter = findCounter(id);
		return (counter != null) ? counter.color : -1;
	}

	/**
	 * Find the {@link Counter} given by id.
	 * 
	 * @param id
	 * @return Returns the {@link Counter} given by id or null if the
	 *         {@link Counter} was not found.
	 */
	protected static Counter findCounter(long id) {
		return Ebean.find(Counter.class, id);
	}
	
	/**
	 * Finds the counter identified by its user id.
	 * 
	 * @param username
	 *            The users name, which act as id and base for finding the
	 *            {@link User}.
	 * @return The {@link User} or null if the {@link User} could not be found.
	 */
	protected static Counter findCounter(Long gameid, String username) {
		
		Game game = findGame(gameid.longValue());
		User user = UserService.findUser(username);
		
		for(Counter counter : game.counters)
			if(counter.user.name.compareTo(user.name)==0)
				return counter;
		
		return null;
	}

	/**
	 * This is the central method for handling client actions. Concrete classes
	 * must implement it, to define the transitions for the game states for each
	 * games modes.
	 * 
	 * @param actionRequest
	 *            The {@link ActionRequest} received from the client.
	 */
	public abstract void handleClientAction(ActionRequest actionRequest);
}
