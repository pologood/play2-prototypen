package controllers;

import static play.test.Helpers.HTMLUNIT;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;
import static play.test.Helpers.testServer;
import models.Path;
import models.Playground;
import models.Point;

import org.junit.Test;

import play.libs.F.Callback;
import play.test.TestBrowser;

public class PlaygroundControllerIntegrationTest {

    /**
     * add your integration test here
     * in this example we just check if the welcome page is being shown
     */   
    @Test
    public void test() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
            	
//            	createPlayground();
            	// TODO Fix
                browser.goTo("http://localhost:3333/playground?name=test");
//                System.out.println(browser.pageSource());
//                assertThat(browser.pageSource()).contains("Your new application is ready.");
            }
        });
    }
	
	public void createPlayground() {

		Playground playground = new Playground("test");

		Point a = point(10, 10);
		Point b = point(10, 20);
		Point c = point(20, 20);
		Point d = point(30, 20);

		playground.addPath(path(a, b));
		playground.addPath(path(b, c));
		playground.addPath(path(c, d));

		playground.save();

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