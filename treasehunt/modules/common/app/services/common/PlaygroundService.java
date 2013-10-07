package services.common;

import java.util.List;

import models.Path;
import models.Playground;
import models.Point;

public interface PlaygroundService {

	Playground findByName(String name);

	/**
	 * Finde alle verfügbaren {@link Playground}s.
	 * 
	 * @return
	 */
	List<Playground> findAll();

	/**
	 * Finde alle {@link Point}s, die zu einem {@link Playground} gehören.
	 * 
	 * @param name
	 * @return
	 */
	List<Point> findAllPoints(String name);

	/**
	 * Füge dem {@link Playground} einen neuen {@link Path} hinzu.
	 * 
	 * @param playground
	 * @param path
	 */
	void createPath(Playground playground, Path path);

	/**
	 * Lösche einen {@link Path} (kein Löschen der {@link Point}s).
	 * 
	 * @param playground
	 * @param id
	 */
	void deletePath(Playground playground, Long id);

	/**
	 * Speichert einen bereits existierenden Pfad für den gegebenen
	 * {@link Playground}.
	 * 
	 * @param playground
	 * @param path
	 */
	void savePath(Playground playground, Path path);

	/**
	 * Speichert einen Pfad unabhängig von einem spezifischen Spielfeld.
	 * 
	 * @param playground
	 * @param id
	 * @param fromId
	 * @param toId
	 */
	void savePath(Long id, Long fromId, Long toId);

	/**
	 * Löscht einen Punkt und alle Pfade, die den Punkt besitzen.
	 * 
	 * @param playground
	 * @param id
	 */
	void deletePoint(Playground playground, Long id);

	/**
	 * Speichert einen Punkt sowie alle Eltern für einen gegeben
	 * {@link Playground}.
	 * 
	 * @param id
	 * @param x
	 * @param y
	 */
	void savePoint(Playground playground, Long id, Integer x, Integer y);

}
