/**
 * 
 */
package controllers.admin;

import java.util.ArrayList;
import java.util.List;

import models.Path;
import models.Point;
import models.admin.GameHall;
import models.common.User;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import play.data.Form;
import play.data.format.Formatters;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http.Request;
import play.mvc.Result;
import play.mvc.WebSocket;
import security.common.Roles;
import services.common.PlaygroundService;
import services.common.PlaygroundServiceImpl;
import services.common.UserManagementImpl;
import services.common.UserManagementService;
import admin.utils.PlaygroundUtils;
import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import controllers.admin.formatter.PointFormatter;

/**
 * Prototyp eines Controllers zur Demonstration von Ajax, Websockets (via Json) und Play Forms.
 * 
 * @author Stefan Illgen
 * 
 */
@Restrict(@Group(Roles.ROLE_ADMIN))
public class Playgrounds extends Controller {
	
	public static Result playgrounds() {
		return ok(views.html.admin.playgrounds.render(getUMS()
				.getSessionLanguage(session()), GameHall.playground,
				new PlaygroundServiceImpl().findAll()));
	}

	// ############### Json Preview ###############
	
	@BodyParser.Of(BodyParser.Json.class)
	public static Result showPreviewJson() {

		String name = request().getQueryString("name");
		if (name == null)
			return badRequest("Missing parameter [name]");

		PlaygroundService playgroundService = new PlaygroundServiceImpl();
		GameHall.playground = playgroundService.findByName(name);

		return returnPreviewJson();
	}

	public static Result returnPreviewJson() {
		if (GameHall.playground == null)
			return badRequest("No Playground for this name!");

		ObjectMapper mapper = new ObjectMapper();
		try {
			String sResult = mapper.writeValueAsString(GameHall.playground);
			return ok(sResult);
		} catch (Exception e) {
			e.printStackTrace();
			return internalServerError();
		}
	}
	
	// ################### Websockets #################

	public static Result pointsWS() {
		return ok(views.html.admin.websockets.render(
				getUMS().getSessionLanguage(session()), GameHall.playground,
				new PlaygroundServiceImpl().findAll()));
	}
	
	/**
	 * Websocket für das Administrieren der Spielfelder verwalten.
	 */
	public static WebSocket<JsonNode> administratePlayground() {
		
		final User user = getUMS().getLoggedUser(session());
		
		return new WebSocket<JsonNode>() {
            
            public void onReady(WebSocket.In<JsonNode> in, WebSocket.Out<JsonNode> out){                
                try { 
                    GameHall.join(user, in, out);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };		
	}

	// ################### Points (AJAX) #################

	public static Result points() {
		return ok(views.html.admin.ajaxpoints.render(
				getUMS().getSessionLanguage(session()), GameHall.playground,
				PlaygroundUtils.getExistingPlaygrounds(), PlaygroundUtils.getExistingPoints()));
	}

	public static Result createPoint(Integer x, Integer y) {

		// add the point
		Point point = new Point(x, y);
		point.save();

		return ok(Long.toString(point.id));
	}

	public static Result savePoint(Long id, Integer x, Integer y) {
		new PlaygroundServiceImpl().savePoint(GameHall.playground, id, x, y);
		load(GameHall.playground.name);
		return returnPreviewJson();
	}

	public static Result deletePoint(Long id) {
		new PlaygroundServiceImpl().deletePoint(GameHall.playground, id);
		return returnPreviewJson();
	}

	// ################### Paths (AJAX) #################

	public static Result paths() {
		return ok(views.html.admin.ajaxpaths
				.render(getUMS().getSessionLanguage(session()), GameHall.playground,
						PlaygroundUtils.getExistingPlaygrounds(), GameHall.playground.paths,
						PlaygroundUtils.getExistingPoints()));
	}

	public static Result createPath(Long from, Long to) {

		Point fromPoint = Point.findById(Long.toString(from));
		Point toPoint = Point.findById(Long.toString(to));

		// add the point
		Path path = new Path(fromPoint, toPoint);
		path.save();

		GameHall.playground.addPath(path);
		GameHall.playground.save();

		return ok(Long.toString(path.id));
	}

	public static Result savePath(Long id, Long fromId, Long toId) {
		new PlaygroundServiceImpl().savePath(id, fromId, toId);
		load(GameHall.playground.name);
		return returnPreviewJson();
	}

	public static Result deletePath(Long id) {
		new PlaygroundServiceImpl().deletePath(GameHall.playground, id);
		return returnPreviewJson();
	}

	// ############### pathNpoints (Play Forms) ##################
	
	static Form<Point> pointForm = Form.form(Point.class);
	static Form<Path> pathNpointCreateForm = Form.form(Path.class);

	public static Result pathNpoints() {

		Formatters.register(Point.class, new PointFormatter());

		if (GameHall.playground == null)
			GameHall.playground = PlaygroundUtils.getExistingPlaygrounds().get(2);

		return ok(views.html.admin.forms.render(getUMS()
				.getSessionLanguage(session()), GameHall.playground,
				PlaygroundUtils.getExistingPlaygrounds(), getPathEditForms(),
				pathNpointCreateForm));
	}

	public static List<Form<Path>> getPathEditForms() {
		List<Form<Path>> pathEditForms = new ArrayList<Form<Path>>();
		for (Path path : GameHall.playground.paths)
			pathEditForms.add(new Form<Path>(Path.class).fill(path));
		return pathEditForms;
	}

	public static Result createPathNpoint() {

		Form<Path> filledForm = pathNpointCreateForm.bindFromRequest();

		if (filledForm.hasErrors())
			return badRequest(views.html.admin.forms.render(getUMS()
					.getSessionLanguage(session()), GameHall.playground,
					PlaygroundUtils.getExistingPlaygrounds(), getPathEditForms(),
					pathNpointCreateForm));
		else {
			new PlaygroundServiceImpl()
					.createPath(GameHall.playground, filledForm.get());
			return redirect(controllers.admin.routes.Playgrounds.paths());
		}
	}

	public static Result savePathNpoint() {

		Request r = request();
		
		Form<Path> pathForm = new Form<Path>(Path.class).bindFromRequest();

		if (pathForm.hasErrors())
			return badRequest(views.html.admin.forms.render(getUMS()
					.getSessionLanguage(session()), GameHall.playground,
					PlaygroundUtils.getExistingPlaygrounds(), getPathEditForms(),
					pathNpointCreateForm));
		else {
			new PlaygroundServiceImpl().savePath(GameHall.playground, pathForm.get());
			return redirect(controllers.admin.routes.Playgrounds.paths());
		}
	}

	public static Result deletePathNpoint(Long id) {
		new PlaygroundServiceImpl().deletePath(GameHall.playground, id);
		return redirect(controllers.admin.routes.Playgrounds.paths());
	}
	
	// ################### Comet #################
	
	public static Result comet() {
		return TODO;
	}
	
	// ######### Helpers #####################
	private static UserManagementService ums = null;

	private static UserManagementService getUMS() {
		if (ums == null)
			ums = new UserManagementImpl();
		return ums;
	}

	public static Result load(String name) {
		PlaygroundService playgroundService = new PlaygroundServiceImpl();
		GameHall.playground = playgroundService.findByName(name);
		return redirect(controllers.admin.routes.Playgrounds.playgrounds());
	}

}
