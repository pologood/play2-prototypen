package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {

	public static final String WELCOME_MESSAGE = "Servus, Gruezi und Hallo!";
	
	public static Result index() {

		// Not the way how it should be done, but how I do:
		/*ApplicationService applicationService = new ApplicationServiceImpl();
		applicationService.deleteTestData();
		applicationService.createInitialTestData();*/

		return ok(index.render(WELCOME_MESSAGE));
	}

}
