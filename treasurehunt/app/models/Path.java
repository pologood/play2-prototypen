package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

import com.avaje.ebean.Ebean;

/**
 * Ein Pfad stellt eine Verbindung zwischen zwei Punkten dar, die ein Spieler
 * benutzen darf. Der Pfad ist gerichtet (von - nach).
 * 
 * Achtung zum Finder:
 * Hier gibt es in Play keine brauchbare Möglichkeit für Ebean die Pointklassen
 * beim Laden mitzuholen (eager fetching); deshalb weicht man auf eine
 * Hilfklasse aus:
 * 
 * Quelle:
 * http://stackoverflow.com/questions/13171476/eager-fetch-does-not-seem-to-join
 * 
 * Also nicht: return find.where().eq("fromPoint", point).findList();
 * 
 * Die Umstellung auf JPA wird das Problem beseitigen
 * 
 * @author justus.markert
 * 
 */
@Entity
public class Path extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5967830143439716451L;

	/**
	 * Interne Id
	 */
	@Id
	long id;

	/**
	 * Punkt 'von'
	 */
	@Required
	@ManyToOne
	public Point fromPoint;

	/**
	 * Punkt 'nach'
	 */
	@Required
	@ManyToOne
	public Point toPoint;

	/**
	 * Standard C'tor
	 * 
	 * @param fromPoint
	 * @param toPoint
	 */
	public Path(Point fromPoint, Point toPoint) {

		this.fromPoint = fromPoint;
		this.toPoint = toPoint;
	}

	/**
	 * Play-Definition eines Finders. Der Zugriff findet dann über die
	 * public-Methoden statt.
	 */
	private static Finder<String, Path> find = new Finder<String, Path>(
			String.class, Path.class);

	/**
	 * Gib mir alle existierenden Pfade
	 * 
	 * @return List<Path>
	 */
	public static List<Path> findByAll() {

		return Ebean.find(Path.class).fetch("fromPoint").fetch("toPoint")
				.findList();
	}

	/**
	 * Gib mir alle existierenden Pfade die ab diesem Punkt starten
	 * 
	 * @return List<Path>
	 */
	public static List<Path> findByFromPoint(Point point) {

		return Ebean.find(Path.class).fetch("fromPoint").fetch("toPoint")
				.where().eq("fromPoint", point).findList();
	}

	/**
	 * Gib mir alle existierenden Pfade die ab diesem Punkt starten
	 * lazy loading
	 * 
	 * @return List<Path>
	 */
	public static List<Path> findByFromPointLazy(Point point) {

		return find.where().eq("fromPoint", point).findList();
	}

	/**
	 * Gib mir alle existierenden Pfade zu diesem Punkt hinführen
	 * 
	 * @return List<Path>
	 */
	public static List<Path> findByToPoint(Point point) {

		return Ebean.find(Path.class).fetch("fromPoint").fetch("toPoint")
				.where().eq("toPoint", point).findList();
	}
}
