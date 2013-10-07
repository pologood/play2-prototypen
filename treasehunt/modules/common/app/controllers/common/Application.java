package controllers.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import models.common.SecurityRole;
import models.common.User;
import play.Configuration;
import play.Routes;
import play.api.i18n.Lang;
import play.data.Form;
import play.data.format.Formatters;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Http.Session;
import play.mvc.Result;
import providers.common.MyUsernamePasswordAuthProvider;
import providers.common.MyUsernamePasswordAuthProvider.MyLogin;
import providers.common.MyUsernamePasswordAuthProvider.MySignup;
import security.common.Roles;
import services.common.ApplicationService;
import services.common.ApplicationServiceImpl;
import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;

import com.avaje.ebean.Ebean;
import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.providers.password.UsernamePasswordAuthProvider;
import com.feth.play.module.pa.user.AuthUser;

public class Application extends Controller {

	public static final String FLASH_MESSAGE_KEY = "message";
	public static final String FLASH_ERROR_KEY = "error";

	public static Result index() {
		return ok(views.html.common.index.render());
	}

	public static Result login() {
		return ok(views.html.common.login
				.render(MyUsernamePasswordAuthProvider.LOGIN_FORM));
	}

	public static Result doLogin() {
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		final Form<MyLogin> filledForm = MyUsernamePasswordAuthProvider.LOGIN_FORM
				.bindFromRequest();
		if (filledForm.hasErrors()) {
			// User did not fill everything properly
			return badRequest(views.html.common.login.render(filledForm));
		} else {
			// Everything was filled
			return UsernamePasswordAuthProvider.handleLogin(ctx());
		}
	}

	public static Result signup() {
		return ok(views.html.common.signup
				.render(MyUsernamePasswordAuthProvider.SIGNUP_FORM));
	}

	public static Result jsRoutes() {
		return ok(
				Routes.javascriptRouter("jsRoutes",
						controllers.common.routes.javascript.Signup
								.forgotPassword())).as("text/javascript");
	}

	public static Result doSignup() {
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		final Form<MySignup> filledForm = MyUsernamePasswordAuthProvider.SIGNUP_FORM
				.bindFromRequest();
		int i = 1;
		i++;
		if (filledForm.hasErrors()) {
			// User did not fill everything properly
			return badRequest(views.html.common.signup.render(filledForm));
		} else {
			// Everything was filled
			// do something with your part of the form before handling the user
			// signup
			Context c = ctx();
			Result r = UsernamePasswordAuthProvider.handleSignup(c);
			return r;
		}
	}

	public static String formatTimestamp(final long t) {
		return new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").format(new Date(t));
	}

	/**
	 * Return the currently authenticated {@link User}, whose UUID is stored
	 * inside the {@link Session}.
	 * 
	 * @param session
	 * @return
	 */
	public static User getLocalUser(final Session session) {
		final AuthUser currentAuthUser = PlayAuthenticate.getUser(session);
		final User localUser = User.findByAuthUserIdentity(currentAuthUser);
		return localUser;
	}

	// ###############################

//	@Restrict(@Group(Roles.ROLE_PLAYER))
	@SubjectPresent
	public static Result profile() {
		final User localUser = getLocalUser(session());
		Formatters.register(SecurityRole.class, new SecurityRoleFormatter());
		Form<User> filledForm = userForm.fill(localUser);
		return ok(views.html.common.profile.render(localUser, filledForm,
				getConfiguredLanguages()));
	}

	static Form<User> userForm = Form.form(User.class);

//	@Restrict(@Group(Roles.ROLE_PLAYER))
	@SubjectPresent
	public static Result saveProfile() {
		
		Formatters.register(SecurityRole.class, new SecurityRoleFormatter());		
		
		// get data from HTTP request
		Form<User> filledForm = userForm.bindFromRequest();

		// in error case 400 bad request
		if (filledForm.hasErrors()) {
			final User localUser = getLocalUser(session());
			return badRequest(views.html.common.profile.render(localUser,
					filledForm, getConfiguredLanguages()));
		} else {
			// update user entity
			User newUserData = filledForm.get();
			final User authUser = getLocalUser(session());
			
			// update authUser does not work
			authUser.email = newUserData.email;
			authUser.name = newUserData.name;
			authUser.firstName = newUserData.firstName;
			authUser.lastName = newUserData.lastName;
			authUser.language = newUserData.language;
			authUser.description = newUserData.description;
			authUser.birthDate = newUserData.birthDate;
//			authUser.lastLogin = newUserData.lastLogin;
//			authUser.active = newUserData.active;
//			authUser.emailValidated = newUserData.emailValidated;
			
			// roles
			// change strange BeanList deferred behaviour > does not work
			authUser.roles.size();
			authUser.roles.clear();
			Ebean.saveManyToManyAssociations(authUser, User.ROLES);
			if(newUserData.roles!=null)
				for(SecurityRole newSecRole : newUserData.roles)				
					authUser.roles.add(newSecRole);
			Ebean.saveManyToManyAssociations(authUser, User.ROLES);
			
//			authUser.linkedAccounts = newUserData.linkedAccounts;
//			authUser.permissions = newUserData.permissions;			

			Ebean.save(Arrays.asList(new User[] { authUser }));
			
			// redirect
			return redirect(controllers.common.routes.Application.profile());
		}

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
	
	/**
	 * JSON Authentification Test.
	 * 
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	@Restrict(@Group(Roles.ROLE_ADMIN))
	public static Result secureJsonRedirect(){
		return TODO;
	}

}