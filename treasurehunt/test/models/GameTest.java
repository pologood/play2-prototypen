package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Testklasse für Game
 * 
 * @author justus.markert
 * 
 */
public class GameTest {

	@Before
	public void startApp() {

		start(fakeApplication(inMemoryDatabase()));

	}

	/**
	 * Testet das Anlegen eines Spiels
	 */
	@Test
	public void testCreationAndRetrieve() {
		Playground playground = new Playground("Testgelände");

		Point a = point(10, 10);
		Point b = point(10, 20);
		Point c = point(20, 20);
		Point d = point(30, 20);

		playground.addPath(path(a, b));
		playground.addPath(path(b, c));
		playground.addPath(path(c, d));
		playground.save();

		Game game = new Game(playground);
		
		game.addPlayer(player("Tom"));
		game.addPlayer(player("Jerry"));
		game.save();

		List<Game> gameList = Game.findByAll();
		assertEquals(1, gameList.size());
		Game resultGame = gameList.get(0);
		assertEquals(2, resultGame.playerGameStatus.size());
		assertTrue("Testgelände".equals(resultGame.playground.name));

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

	private Player player(String name) {
		
		Player player = new Player(name);
		player.save();
		return player;		
	}
}
