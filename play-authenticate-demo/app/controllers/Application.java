package controllers;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import providers.MyUsernamePasswordAuthProvider;
import providers.MyUsernamePasswordAuthProvider.MySignup;

import com.feth.play.module.pa.controllers.Authenticate;
import com.feth.play.module.pa.providers.password.UsernamePasswordAuthProvider;


public class Application extends Controller {
  
	public static final String FLASH_ERROR_KEY = "error";
	
	public static final String USER_ROLE = "user";
	
    public static Result index() {
        return ok(views.html.index.render("Your new application is ready."));
    }
    
    public static Result signup() {
		return ok(views.html.signup.render(MyUsernamePasswordAuthProvider.SIGNUP_FORM));
	}
    
    public static Result doSignup() {
		Authenticate.noCache(response());
		final Form<MySignup> filledForm = MyUsernamePasswordAuthProvider.SIGNUP_FORM
				.bindFromRequest();
		if (filledForm.hasErrors()) {
			// User did not fill everything properly
			return badRequest(signup.render(filledForm));
		} else {
			// Everything was filled
			// do something with your part of the form before handling the user
			// signup
			return UsernamePasswordAuthProvider.handleSignup(ctx());
		}
	}
  
}
