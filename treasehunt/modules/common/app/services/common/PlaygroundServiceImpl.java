package services.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.Path;
import models.Playground;
import models.Point;

public class PlaygroundServiceImpl implements PlaygroundService {

	@Override
	public Playground findByName(String name) {

		Playground playground = Playground.findByName(name);

		return playground;
	}

	@Override
	public List<Playground> findAll() {
		List<Playground> all = Playground.findByAllLazy();
		return all;
	}

	@Override
	public List<Point> findAllPoints(String playgroundName) {

		Set<Point> resultSet = new HashSet<Point>();

		Playground pg = findByName(playgroundName);
		for (Path path : pg.paths) {
			resultSet.add(path.fromPoint);
			resultSet.add(path.toPoint);
		}

		List<Point> result = new ArrayList<Point>(resultSet);
		Collections.sort(result);
		
		return result;
	}

	@Override
	public void createPath(Playground playground, Path path) {

		path.fromPoint.save();
		path.toPoint.save();
		path.save();
		playground.addPath(path);
		playground.save();
	}

	@Override
	public void deletePath(Playground playground, Long id) {
		Path path = Path.findById(id);
		playground.deletePath(path);
		playground.save();
		path.delete();
	}

	@Override
	public void savePath(Playground playground, Path path) {

		Set<Path> pathsToSave = new HashSet<Path>();
		for (Playground p : findAll()) {
			for (Path pa : p.paths) {
				if (pa.fromPoint.id == path.fromPoint.id) {
					pa.fromPoint.x = path.fromPoint.x;
					pa.fromPoint.y = path.fromPoint.y;
					pa.fromPoint.save();
					pathsToSave.add(pa);
				}
				if (pa.toPoint.id == path.toPoint.id) {
					pa.toPoint.x = path.toPoint.x;
					pa.toPoint.y = path.toPoint.y;
					pa.toPoint.save();
					pathsToSave.add(pa);
				}
			}
		}

		for (Path pa : pathsToSave) {
			pa.save();
		}
	}

	@Override
	public void savePath(Long pathId, Long fromPointId,
			Long toPointId) {
		
		Path path = Path.findById(pathId);
		path.fromPoint = Point.findById(Long.toString(fromPointId));
		path.toPoint = Point.findById(Long.toString(toPointId));
		
		path.save();
	}

	@Override
	public void deletePoint(Playground playground, Long id) {

		// delete corresponding paths
		List<Path> toBeDeleted = new ArrayList<Path>();
		for (Path path : playground.paths) {
			// How To remove (Ebean vs. JPA)
			if (path.fromPoint.id == id || path.toPoint.id == id)
				toBeDeleted.add(path);
		}
		for (Path path : toBeDeleted) {
			playground.deletePath(path);
			playground.save();
			path.delete();
		}
		// delete point
		Point.delete(id);
		// Point.save();
		playground.save();
	}

	@Override
	public void savePoint(Playground playground, Long id, Integer x, Integer y) {

		Set<Path> pathsToSave = new HashSet<Path>();
		for (Playground p : findAll()) {
			for (Path pa : p.paths) {
				if (pa.fromPoint.id == id) {
					System.out.println(pa.fromPoint.x);
					pa.fromPoint.x = x;
					pa.fromPoint.y = y;
					pa.fromPoint.save();
					pathsToSave.add(pa);
				}
				if (pa.toPoint.id == id) {
					System.out.println(pa.toPoint.x);
					pa.toPoint.x = x;
					pa.toPoint.y = y;
					pa.toPoint.save();
					pathsToSave.add(pa);
				}
			}
		}

		for (Path pa : pathsToSave) {
			pa.save();
		}
	}

}
