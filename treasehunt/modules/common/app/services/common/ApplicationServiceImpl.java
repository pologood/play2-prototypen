package services.common;

//import play.api.i18n.Lang;
import models.Path;
import models.Playground;
import models.Point;

public class ApplicationServiceImpl implements ApplicationService {

	public void deleteTestData() {

		for (Playground p : Playground.findByAllLazy()) {
			p.delete();
		}

		for (Path p : Path.findByAll()) {
			p.delete();
		}

		for (Point p : Point.findByAll()) {
			p.delete();
		}
	}

	public void createInitialTestData() {

		Playground playground = new Playground("big");

		Point a = point(100, 100);
		Point b = point(130, 150);
		Point c = point(150, 300);
		Point d = point(170, 350);
		Point e = point(200, 200);
		Point f = point(210, 220);
		Point g = point(210, 300);
		Point h = point(250, 250);
		Point i = point(270, 200);

		playground.addPath(path(a, b));
		playground.addPath(path(b, c));
		playground.addPath(path(c, d));
		playground.addPath(path(d, g));
		playground.addPath(path(d, h));
		playground.addPath(path(h, i));
		playground.addPath(path(c, e));
		playground.addPath(path(c, f));

		playground.save();

		Playground playground2 = new Playground("small");

		Point a2 = point(100, 100);
		Point b2 = point(150, 150);
		Point c2 = point(150, 300);
		Point d2 = point(200, 350);
		Point e2 = point(200, 200);

		playground2.addPath(path(a2, b2));
		playground2.addPath(path(b2, c2));
		playground2.addPath(path(c2, d2));
		playground2.addPath(path(d2, e2));

		playground2.save();

		Playground playground3 = new Playground("foo");

		Point a3 = point(110, 130);
		Point b3 = point(180, 180);
		Point c3 = point(200, 250);
		Point e3 = point(300, 290); // Bautzen

		Point d3 = point(290, 330);
		Point f3 = point(400, 250);
		Point g3 = point(460, 280);
		Point h3 = point(500, 320);
		Point i3 = point(530, 370); // Görlitz

		Point j3 = point(490, 410);
		Point k3 = point(440, 450);
		Point l3 = point(450, 500);
		Point m3 = point(460, 560); // Zittau

		Point n3 = point(570, 310);
		Point o3 = point(620, 300);
		Point p3 = point(680, 320);
		Point q3 = point(760, 360); // Wroclaw
		Point r3 = point(800, 400); // Wroclaw

		playground3.addPath(path(a3, b3));
		playground3.addPath(path(b3, c3));
		playground3.addPath(path(c3, e3));
		playground3.addPath(path(e3, f3));
		playground3.addPath(path(e3, d3));
		playground3.addPath(path(f3, g3));

		playground3.addPath(path(g3, h3));
		playground3.addPath(path(h3, i3));
		playground3.addPath(path(i3, j3));
		playground3.addPath(path(j3, k3));
		playground3.addPath(path(k3, l3));
		playground3.addPath(path(l3, m3));

		playground3.addPath(path(i3, n3));
		playground3.addPath(path(n3, o3));
		playground3.addPath(path(o3, p3));
		playground3.addPath(path(p3, q3));
		playground3.addPath(path(q3, r3));

		playground3.save();
	}

	private Point point(int x, int y) {

		Point point = new Point(x, y);
		point.save();
		return point(x, y, false);
	}

	private Point point(int x, int y, boolean city) {

		Point point = new Point(x, y);
		point.city = city;
		point.save();
		return point;
	}

	private Path path(Point from, Point to) {

		Path path = new Path(from, to);
		path.save();
		return path;
	}
}
