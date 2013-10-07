package models;

import static org.junit.Assert.assertEquals;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;

import java.util.List;

import models.Point;

import org.junit.Before;
import org.junit.Test;

/**
 * Testklasse für Basic CRUD Funktionen sowie Finder der Modelklasse Point.
 * 
 * @author justus.markert
 * 
 */
public class PointTest {

	@Before
	public void startApp() {

		start(fakeApplication(inMemoryDatabase()));

	}

	/**
	 * Teste das Anlegen, Speichern und Laden eines Punktes
	 */
	@Test
	public void testCreation() {

		new Point(20, 10).save();

		List<Point> points = Point.findByAll();

		assertEquals(1, points.size());
		assertEquals(points.get(0).x, 20);
		assertEquals(points.get(0).y, 10);

	}

	/**
	 * Teste das Anlegen, Speichern und Laden eines Punktes
	 */
	@Test
	public void testFindByXandY() {

		new Point(20, 10).save();

		List<Point> points = Point.findByXandY(20, 10);

		assertEquals(1, points.size());
		assertEquals(points.get(0).x, 20);
		assertEquals(points.get(0).y, 10);

	}
}
