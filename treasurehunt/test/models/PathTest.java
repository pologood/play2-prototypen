package models;

import static org.junit.Assert.*;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Testklasse für 'Path'
 * 
 * @author justus.markert
 *
 */
public class PathTest {

	@Before
	public void startApp() {

		start(fakeApplication(inMemoryDatabase()));

	}

	/**
	 * Teste das Anlegen eines Paths sowie das Eagerfetching der Punkte
	 */
	@Test
	public void testCreateAndRetrieveEager() {

		Point fromPoint = new Point(20, 30);
		Point toPoint = new Point(40, 50);
		
		fromPoint.save();
		toPoint.save();

		Path path = new Path(fromPoint, toPoint);

		path.save();

		List<Path> resultPaths = Path.findByAll();
		assertEquals(1, resultPaths.size());
		Path resultPath = resultPaths.get(0);

		assertEquals(20, resultPath.fromPoint.x);
		assertEquals(30, resultPath.fromPoint.y);

		assertEquals(40, resultPath.toPoint.x);
		assertEquals(50, resultPath.toPoint.y);

	}

	/**
	 * Teste den FromPointFinder sowie das Lazyfetching der Punkte
	 */
	@Test
	public void testFindByFromPointLazy() {

		Point fromPoint = new Point(20, 30);
		Point toPoint = new Point(40, 50);
		
		fromPoint.save();
		toPoint.save();

		Path path = new Path(fromPoint, toPoint);

		path.save();

		List<Path> resultPaths = Path.findByFromPointLazy(fromPoint);
		assertEquals(1, resultPaths.size());
		Path resultPath = resultPaths.get(0);

		assertEquals(0, resultPath.fromPoint.x);
	}

	/**
	 * Teste den FromPointFinder sowie das Eagerfetching der Punkte
	 */
	@Test
	public void testFindByFromPoint() {

		Point fromPoint = new Point(20, 30);
		Point toPoint = new Point(40, 50);
		
		fromPoint.save();
		toPoint.save();

		Path path = new Path(fromPoint, toPoint);

		path.save();

		List<Path> resultPaths = Path.findByFromPoint(fromPoint);
		assertEquals(1, resultPaths.size());
		Path resultPath = resultPaths.get(0);

		assertEquals(20, resultPath.fromPoint.x);
		assertEquals(30, resultPath.fromPoint.y);

		assertEquals(40, resultPath.toPoint.x);
		assertEquals(50, resultPath.toPoint.y);
	}
}
