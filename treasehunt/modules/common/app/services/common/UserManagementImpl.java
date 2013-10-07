/**
 * 
 */
package services.common;

import java.util.ArrayList;
import java.util.List;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.user.AuthUser;

import models.common.User;
import play.Configuration;
import play.api.i18n.Lang;
import play.mvc.Http.Session;

/**
 * @author Stefan Illgen
 * 
 */
public class UserManagementImpl implements UserManagementService {

	/**
	 * 
	 */
	public UserManagementImpl() {

	}

	@Override
	public List<Lang> getConfiguredLanguages() {

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

	/**
	 * Return the currently authenticated {@link User}, whose UUID is stored
	 * inside the {@link Session}.
	 * 
	 * @param session
	 * @return
	 */
	@Override
	public User getLoggedUser(Session session) {
		final AuthUser currentAuthUser = PlayAuthenticate.getUser(session);
		final User localUser = User.findByAuthUserIdentity(currentAuthUser);
		return localUser;
	}

	@Override
	public Lang getSessionLanguage(Session session) {
		String lang = getLoggedUser(session).language;
		if(lang!=null)
			return Lang.apply(lang);
		else
			return getConfiguredLanguages().get(0);
	}
	
}
