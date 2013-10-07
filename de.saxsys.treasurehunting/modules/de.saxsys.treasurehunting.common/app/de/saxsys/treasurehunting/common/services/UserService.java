/**
 * 
 */
package de.saxsys.treasurehunting.common.services;

import java.util.List;

import play.Logger;
import play.api.i18n.Lang;
import play.db.ebean.Model;
import play.mvc.Http.Request;
import play.mvc.Http.Session;

import com.avaje.ebean.ExpressionList;

import de.saxsys.treasurehunting.common.models.user.User;

/**
 * This service class provides an implementation for.
 * 
 * <ul>
 * <li>managing the {@link User} model</li>
 * <li>managing authentication</li>
 * </ul>
 * 
 * 
 * @author stefan.illgen
 */
public abstract class UserService {

	private static Model.Finder<String, User> find = new Model.Finder<String, User>(
			String.class, User.class);

	/**
	 * Creates an anonymous user.
	 * 
	 * @param username
	 *            The name of the user to be created.
	 * @return The {@link User}.
	 */
	public static User createAnonymousUser(String username) {

		User user = new User();
		user.name = username;
		user.save();

		Logger.info("User " + user.name
				+ " was added to the authenticated users.");

		return user;
	}

	/**
	 * Returns the language configured with the given {@link Session}. If there
	 * is no language configured, the first language matching an application
	 * supported and browser accepted language is returned. If there is no match
	 * found, the first application supported language is returned.
	 * 
	 * @param session
	 *            The session the language is registered with.
	 * @param request
	 *            The current HTTP request need for accing browser accepted
	 *            langugages.
	 * @return The {@link Lang}.
	 */
	public static Lang getSessionLanguage(Session session, Request request) {

		// session language
		String sLang = session.get("lang");
		if (sLang != null)
			return play.i18n.Lang.forCode(sLang).underlyingLang;

		// matching application supported and browser accepted language
		if (sLang == null) {
			String sSupportedLangs = play.Play.application().configuration()
					.getString("application.langs");
			List<play.i18n.Lang> acceptedLangs = request.acceptLanguages();
			for (play.i18n.Lang acceptedLang : acceptedLangs) {
				for (String sSupportedLang : sSupportedLangs.split(",")) {
					if (acceptedLang.code().compareTo(sSupportedLang) == 0)
						return acceptedLang.underlyingLang;
				}
			}
		}

		// no matching between supported and accepted languages found: return
		// the first configured language
		return Lang.defaultLang();
	}

	/**
	 * Sets the session specific language.
	 * 
	 * @param session
	 *            The {@link Session}.
	 * @param id
	 *            The language code ID.
	 */
	public static void setSessionLang(Session session, String id) {
		session.put("lang", id);
	}

	/**
	 * Authenticates and returns an instance of {@link User} for the case the
	 * user is already registered.
	 * 
	 * If the user isn't registered yet this operation will dynamically create
	 * an anonymous user identified by parameter username.
	 * 
	 * @param session
	 *            The {@link Session} the user has to be authenticated with.
	 * @param username
	 *            The user name to be authenticated.
	 * @return The {@link User}.
	 */
	public static User authenticate(Session session, String username) {

		User authUser = findUser(username);

		// if the user is not regisitered
		if (authUser == null)
			// dynamically create an anonymous user
			authUser = UserService.createAnonymousUser(username);

		// register user id with session
		session.put("username", authUser.name);

		return authUser;
	}

	/**
	 * This Operation returns the currently authenticated {@link User}.
	 * 
	 * @param session
	 *            The Session the user is authenticated with.
	 * @return The {@link User}.
	 */
	public static User getAuthUser(Session session) {
		return findUser(session.get("username"));
	}

	/**
	 * Finds the user identified by its user id.
	 * 
	 * @param username
	 *            The users name, which act as id and base for finding the
	 *            {@link User}.
	 * @return The {@link User} or null if the {@link User} could not be found.
	 */
	public static User findUser(String username) {
		if (username != null && find.where() != null) {
			ExpressionList<User> ul = find.where().eq("name", username);
			return ul.findUnique();
		} else
			return null;
	}

	/**
	 * Returns the authenticated users name.
	 * 
	 * @param session
	 *            The Session the user is authenticated with.
	 * @return The authenticated {@link User}'s name or null if the {@link User}
	 *         could not be found.
	 */
	public static String getAuthUserName(Session session) {
		User authUser = getAuthUser(session);
		if (authUser != null)
			return authUser.name;
		else
			return null;
	}

	/**
	 * Checks if the user is a registered one by proofing it's existence inside
	 * the database.
	 * 
	 * @param username
	 *            The user's name.
	 * @return Returns <code>true</code>, if the user is registered, otherwise
	 *         <code>false</code>.
	 */
	public static boolean isRegistered(String username) {
		return findUser(username) != null;
	}
}
