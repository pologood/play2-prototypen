/**
 * 
 */
package controllers.admin;

import java.util.Arrays;

import com.avaje.ebean.Ebean;

import controllers.common.SecurityRoleFormatter;
import models.common.SecurityRole;
import models.common.User;
import play.data.Form;
import play.data.format.Formatters;
import play.mvc.Controller;
import play.mvc.Result;
import security.common.Roles;
import services.common.UserManagementImpl;
import services.common.UserManagementService;
import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;

/**
 * @author Stefan Illgen
 * 
 */
public class Players extends Controller {

	private static UserManagementService ums = null;

	/**
	 * TODO: stefan inject
	 * 
	 * @return
	 */
	private static UserManagementService getUMS() {
		if (ums == null)
			ums = new UserManagementImpl();
		return ums;
	}

	private static Form<User> userForm = new Form<User>(User.class);

	@Restrict(@Group(Roles.ROLE_ADMIN))
	public static Result players() {

		UserManagementService ums = new UserManagementImpl();
		final User loggedUser = ums.getLoggedUser(session());
		loggedUser.getRoles().isEmpty();

		Formatters.register(SecurityRole.class, new SecurityRoleFormatter());
		Form<User> filledForm = userForm.fill(loggedUser);

		return ok(views.html.admin.players.render(
				getUMS().getSessionLanguage(session()), User.find.all(),
				SecurityRole.find.all()));
	}

}
