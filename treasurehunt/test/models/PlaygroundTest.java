package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;

import org.junit.Before;
import org.junit.Test;

/**
 * Testklasse für Playground
 * 
 * @author justus.markert
 * 
 */
public class PlaygroundTest {

	@Before
	public void startApp() {

		start(fakeApplication(inMemoryDatabase()));

	}

	/**
	 * Test für grundlegende CRUD Funktion
	 */
	@Test
	public void testCreateAndRetrieve() {

		String name = "Testgelände";
		new Playground(name).save();

		Playground playground = Playground.findByName(name);
		assertEquals(name, playground.name);
	}

	/**
	 * Test für byName-Finder
	 */
	@Test
	public void testByNameNotFound() {

		String name = "Testgelände";
		new Playground(name).save();

		Playground playground = Playground.findByName(name);
		assertEquals(name, playground.name);

		playground = Playground.findByName("Quark");
		assertNull(playground);

	}

	/**
	 * Teste Anlegen eines Spielfelds mit Pfaden und Punkten
	 */
	@Test
	public void testCreationWithPathsAndPoints() {

		Playground playground = new Playground("Testgelände");

		Point a = point(10, 10);
		Point b = point(10, 20);
		Point c = point(20, 20);
		Point d = point(30, 20);

		playground.addPath(path(a, b));
		playground.addPath(path(b, c));
		playground.addPath(path(c, d));

		playground.save();

		Playground resultPlayground = Playground
				.findByName("Testgelände");
		assertEquals(3, resultPlayground.paths.size());
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
