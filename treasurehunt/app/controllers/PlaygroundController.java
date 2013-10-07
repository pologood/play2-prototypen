package controllers;

import models.Playground;

import org.codehaus.jackson.map.ObjectMapper;

import play.mvc.Controller;
import play.mvc.Result;
import services.PlaygroundService;
import services.PlaygroundServiceImpl;
import views.html.displayfield;

/**
 * Für den Datenaustausch mit JSON
 * 
 * @author justus.markert
 * 
 */
public class PlaygroundController extends Controller {

    public static Result index() {

    	return ok(displayfield.render());
    }

	/**
	 * Lade die Daten des gewünschten Spielfelds
	 * 
	 * @param name
	 * @return Playground
	 */
	public static Result loadPlayground() {

		String name = request().getQueryString("name");
		
		if (name == null) {
			return badRequest("No name given!");
		}

		PlaygroundService playgroundService = new PlaygroundServiceImpl();
		Playground playground = playgroundService.findByName(name);

		if (playground == null) {
			return badRequest("No Playground for this name!");
		}

		ObjectMapper mapper = new ObjectMapper();
		try {
			String json = mapper.writeValueAsString(playground);
			return ok(json);
		} catch (Exception e) {

			e.printStackTrace();
			return internalServerError();
		}
		
	}
}
