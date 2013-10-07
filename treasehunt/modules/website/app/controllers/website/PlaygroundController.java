package controllers.website;

import models.Playground;

import org.codehaus.jackson.map.ObjectMapper;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;

import play.mvc.Controller;
import play.mvc.Result;
import security.common.Roles;
import services.common.PlaygroundService;
import services.common.PlaygroundServiceImpl;

/**
 * Für den Datenaustausch mit JSON
 * 
 * @author justus.markert
 * 
 */
@Restrict(@Group(Roles.ROLE_PLAYER))
public class PlaygroundController extends Controller {

    public static Result index() {

    	return ok(views.html.displayfield.render());
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
