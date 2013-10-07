package controllers;

import models.TokenAction;
import models.TokenAction.Type;
import models.User;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;

import com.feth.play.module.pa.controllers.Authenticate;

public class Signup extends Controller {

	public static Result oAuthDenied(final String getProviderKey) {
		// sets HTTP-Headers for NO-Cache and expiration time to 0 and shows denied template
		Authenticate.noCache(response());
		return ok(oAuthDenied.render(getProviderKey));
		// or put an error message into the flash scope, redirect and show this
		// message 2 the user
		// flash(FLASH_ERROR_KEY,
		// "You need to accept the OAuth connection in order to use this website!");
	}
	
	public static Result exists() {
		Authenticate.noCache(response());
		return ok(exists.render());
	}
	
	public static Result unverified() {
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		return ok(unverified.render());
	}
	
	public static Result verify(final String token) {
		Authenticate.noCache(response());
		final TokenAction ta = tokenIsValid(token, Type.EMAIL_VERIFICATION);
		if (ta == null) {
			return badRequest(no_token_or_invalid.render());
		}
		final String email = ta.targetUser.email;
		User.verify(ta.targetUser);
		flash(Application.FLASH_MESSAGE_KEY,
				Messages.get("playauthenticate.verify_email.success", email));
		if (Application.getLocalUser(session()) != null) {
			return redirect(routes.Application.index());
		} else {
			return redirect(routes.Application.login());
		}
	}
	
	/**
	 * Returns a token object if valid, null if not
	 * 
	 * @param token
	 * @param type
	 * @return
	 */
	private static TokenAction tokenIsValid(final String token, final Type type) {
		TokenAction ret = null;
		if (token != null && !token.trim().isEmpty()) {
			final TokenAction ta = TokenAction.findByToken(token, type);
			if (ta != null && ta.isValid()) {
				ret = ta;
			}
		}

		return ret;
	}

}
