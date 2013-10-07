package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

/**
 * Referenziert einen Spielpunkt auf der Karte auf dem sich der Spieler bewegen
 * kann.
 * 
 * @author justus.markert
 * 
 */
@Entity
public class Point extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1846958748629611213L;

	/**
	 * Interne Id des Punkts 
	 */
	@Id
	public long id;

	/**
	 * Position X auf dem Spielfeld
	 */
	@Required
	public int x;

	/**
	 * Position Y auf dem Spielfeld
	 */
	@Required
	public int y;

	/**
	 * Handelt es sich um eine Stadt
	 */
	@Required
	public boolean city = false;
	
	/**
	 * Standard C´tor
	 * 
	 * @param x
	 * @param y
	 */
	public Point(int x, int y) {

		this.x = x;
		this.y = y;
	}

	/**
	 * Play-Definition eines Finders. Der Zugriff findet dann über die public-Methoden statt.
	 */
	private static Finder<String, Point> find = new Finder<String, Point>(
			String.class, Point.class);

	/**
	 * Gib mir alle existierenden Spielpunkte
	 * 
	 * @return List<Point>
	 */
	public static List<Point> findByAll() {

		return find.all();
	}

	/**
	 * Gib mir alle existierenden Spielpunkte für diese Koordinaten
	 * 
	 * @return List<Point>
	 */
	public static List<Point> findByXandY(int x, int y) {

		return find.where().eq("x", x).eq("y", y).findList();
	}
}
