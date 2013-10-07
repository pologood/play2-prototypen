/**
 * 
 */
package controllers.admin;

import java.util.ArrayList;
import java.util.List;

import models.common.User;
import play.Configuration;
import play.api.i18n.Lang;
import play.mvc.Controller;
import play.mvc.Result;
import security.common.Roles;
import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.user.AuthUser;

/**
 * @author Stefan Illgen
 * 
 */
public class Application extends Controller {

	@Restrict(@Group(Roles.ROLE_ADMIN))
	public static Result index() {

		// TODO local User übergeben wie hier unten
		// final models.common.User localUser = controllers.common.Application
		// .getLocalUser(session());
		// return ok(views.html.admin.restricted.render(localUser));

		return ok(views.html.admin.index.render(getUserLanguage()));
	}

	private static Lang getUserLanguage() {

		final AuthUser currentAuthUser = PlayAuthenticate.getUser(session());
		final User localUser = User.findByAuthUserIdentity(currentAuthUser);

		Lang result = null;
		if (localUser.language != null)
			result = Lang.apply(localUser.language);
		else {
			// if user has not defined language yet, get the first configured
			// language
			result = getConfiguredLanguages().get(0);
		}
		return result;
	}

	public static List<Lang> getConfiguredLanguages() {

		Configuration conf = Configuration.root();
		String langs = conf.getString("application.langs");
		String[] langss = langs.split(",");
		List<String> langsl = new ArrayList<String>();
		for (String lang : langss) {
			langsl.add(lang);
		}

		List<Lang> llangs = new ArrayList<Lang>();
		for (String lang : langss) {
			llangs.add(Lang.apply(lang));
		}

		return llangs;
	}

}
