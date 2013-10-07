package controllers;

import models.BaseModelTest;
import models.Path;
import models.Playground;
import models.Point;

import org.junit.Test;

import play.mvc.Result;

/**
 * Testklasse für den PlaygroundController
 * 
 * @author justus.markert
 * 
 */
public class PlaygroundControllerTest extends BaseModelTest {

	/**
	 * TODO Evaluierung des JSON
	 * 
	 */
	@Test
	public void testLoadPlaygroundJSON() {

		Playground playground = new Playground("Testgelände");

		Point a = point(10, 10);
		Point b = point(10, 20);
		Point c = point(20, 20);
		Point d = point(30, 20);

		playground.addPath(path(a, b));
		playground.addPath(path(b, c));
		playground.addPath(path(c, d));

		playground.save();

		// TODO Fix: Setze FakeRequest mit Attributname 'name' und Wert
		// 'Testgelände'
		Result result = PlaygroundController.loadPlayground();

		// System.out.println(result.getWrappedResult());
	}

	private Point point(int x, int y) {

		Point point = new Point(x, y);
		point.save();
		return point;
	}

	private Path path(Point from, Point to) {

		Path path = new Path(from, to);
		path.save();
		return path;
	}
}
