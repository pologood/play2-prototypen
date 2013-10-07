/**
 * 
 */
package admin.utils;

import java.util.List;

import models.Playground;
import models.Point;
import models.admin.GameHall;

import org.codehaus.jackson.map.ObjectMapper;

import services.common.PlaygroundService;
import services.common.PlaygroundServiceImpl;

/**
 * @author stefan.illgen
 *
 */
public class PlaygroundUtils {

	public static String getPlayground(String name) {

		PlaygroundService playgroundService = new PlaygroundServiceImpl();
		Playground playground = playgroundService.findByName(name);

		ObjectMapper mapper = new ObjectMapper();
		try {
			String sResult = mapper.writeValueAsString(playground);
			return sResult;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<Point> getExistingPoints() {
		PlaygroundService playgroundService1 = new PlaygroundServiceImpl();
		List<Point> points = null;
		if (GameHall.playground != null)
			points = playgroundService1.findAllPoints(GameHall.playground.name);
		return points;
	}

	public static List<Playground> getExistingPlaygrounds() {
		PlaygroundService playgroundService = new PlaygroundServiceImpl();
		List<Playground> playgrounds = playgroundService.findAll();
		return playgrounds;
	}

}
