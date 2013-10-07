package de.saxsys.treasurehunting.game.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;

import play.Logger;
import play.data.Form;
import play.data.validation.Constraints.Required;
import play.data.validation.ValidationError;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Http.Session;
import play.mvc.Result;
import play.mvc.Security;
import play.mvc.WebSocket;
import de.saxsys.treasurehunting.common.controllers.Secured;
import de.saxsys.treasurehunting.common.models.game.Counter;
import de.saxsys.treasurehunting.common.services.UserService;
import de.saxsys.treasurehunting.game.services.PlaygroundService;
import de.saxsys.treasurehunting.game.services.exceptions.GameCreationException;
import de.saxsys.treasurehunting.game.services.singleplayer.SinglePlayerGameHall;
import de.saxsys.treasurehunting.game.views.html.index;
import de.saxsys.treasurehunting.game.views.html.singleplayer.singleplayer;

/**
 * This {@link Controller} implements the game actions.
 * 
 * @author stefan.illgen
 */
public class GameController extends Controller {

	/**
	 * Represents a SingleplayGameConfiguration with game name, counter color
	 * and play ground name.
	 * 
	 * @author stefan.illgen
	 * 
	 */
	public static class SingleplayerGameConfiguration {

		/**
		 * The name of the game.
		 */
		public String gameName;

		/**
		 * The counters color as hexa decimal RGB int value.
		 */
		@Required
		public Integer counterColor = Counter.COUNTER_COLOR_GREEN;

		/**
		 * Returns the colors red, green and blue used for counter coloring.
		 * 
		 * @return Returns the colors red, green and blue used for counter
		 *         coloring.
		 */
		public static Map<Integer, String> getCounterColors() {
			return new HashMap<Integer, String>() {
				private static final long serialVersionUID = -4984259332509141788L;
				{
					put(Counter.COUNTER_COLOR_RED, Messages.get(UserService
							.getSessionLanguage(session(), request()),
							"game.index.sp.conf.counterColor.red"));
					put(Counter.COUNTER_COLOR_GREEN, Messages.get(UserService
							.getSessionLanguage(session(), request()),
							"game.index.sp.conf.counterColor.green"));
					put(Counter.COUNTER_COLOR_BLUE, Messages.get(UserService
							.getSessionLanguage(session(), request()),
							"game.index.sp.conf.counterColor.blue"));
				}
			};
		}

		/**
		 * The name of the playground to be used for the single player game.
		 */
		@Required
		public String playgroundName = PlaygroundService
				.getAllPlaygroundNames().get(0);

		/**
		 * The validation method checks the following conditions.
		 * 
		 * <ul>
		 * <li>the trimmed name is greater than zero.</li>
		 * </ul>
		 * 
		 * @return The {@link Map} containing the keyed {@link ValidationError}
		 *         s.
		 */
		public Map<String, List<ValidationError>> validate() {

			Map<String, List<ValidationError>> result = new HashMap<String, List<ValidationError>>();

			// trimmed length must be greater 0
			if (gameName.trim().length() == 0) {
				List<ValidationError> errors = new ArrayList<ValidationError>();
				errors.add(new ValidationError("gameName",
						play.i18n.Messages.get(UserService.getSessionLanguage(
								session(), request()),
								"game.index.sp.conf.gameName.error")));
				result.put("gameName", errors);
			}

			return result;
		}
	}

	/**
	 * Returns the rendered index page for the game.
	 * 
	 * @return The {@link Result}, which represents the rendered index page for
	 *         the game.
	 */
	@Security.Authenticated(Secured.class)
	public static Result index() {
		return ok(index.render(Form.form(SingleplayerGameConfiguration.class)));
	}

	/**
	 * Validates the single player configuration form and if there are no
	 * errors, creates the single player game. If there is a validation error,
	 * it shows the corresponding error message to the user.
	 * 
	 * @return The {@link Result}.
	 */
	@Security.Authenticated(Secured.class)
	public static Result startSPGame() {

		Form<SingleplayerGameConfiguration> spConfForm = Form.form(
				SingleplayerGameConfiguration.class).bindFromRequest();

		if (spConfForm.hasErrors())
			return badRequest(index.render(spConfForm));

		Map<String, String> spConfMap = spConfForm.data();

		try {
			long gameid = SinglePlayerGameHall.createGame(UserService
					.getAuthUserName(session()), spConfMap.get("gameName"),
					Integer.parseInt(
							spConfMap.get("counterColor").substring(2), 16),
					spConfMap.get("playgroundName"));

			return redirect(de.saxsys.treasurehunting.game.controllers.routes.GameController
					.singleplayer(gameid));
		} catch (GameCreationException e) {
			e.printStackTrace();
			Logger.error(e.getMessage());
			return internalServerError(e.getMessage());
		}
	}

	/**
	 * .
	 * 
	 * @return The {@link Result}.
	 */
	@Security.Authenticated(Secured.class)
	public static Result singleplayer(Long game) {
		return ok(singleplayer
				.render(SinglePlayerGameHall.findGameName(game)));
	}

	/**
	 * Controller Action for initiating the web socket.
	 */
	public static WebSocket<JsonNode> initializeSinglePlayerGame(final Long game) {

		final Session session = session();
		
		return new WebSocket<JsonNode>() {

			public void onReady(WebSocket.In<JsonNode> in,
					WebSocket.Out<JsonNode> out) {
				try {
					SinglePlayerGameHall.join(game,
							UserService.getAuthUserName(session), in, out);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		};
	}

	/**
	 * .
	 * 
	 * @return The {@link Result}.
	 */
	@Security.Authenticated(Secured.class)
	public static Result visitorarena() {
		return TODO;
	}

	/**
	 * .
	 * 
	 * @return The {@link Result}.
	 */
	@Security.Authenticated(Secured.class)
	public static Result startVAGame() {
		return TODO;
	}
}
