package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.avaje.ebean.Ebean;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

/**
 * Repräsentiert ein laufendes Spiel
 * 
 * @author justus.markert
 * 
 */
@Entity
public class Game extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4975452788426314864L;

	/**
	 * Interne Id
	 */
	@Id
	long id;

	/**
	 * Das benutzte Spielfeld
	 */
	@Required
	@ManyToOne
	public Playground playground;

	/**
	 * Die Teilnehmenden Spieler und ihr Forschritt im Spiel
	 */
    @ManyToMany(cascade=CascadeType.ALL)
	public List<PlayerGameStatus> playerGameStatus = new ArrayList<PlayerGameStatus>();

	/**
	 * Standard C'tor
	 * 
	 * @param playground
	 */
	public Game(Playground playground) {

		this.playground = playground;
	}

	/**
	 * Play-Definition eines Finders. Der Zugriff findet dann über die
	 * public-Methoden statt.
	 */
	private static Finder<String, Game> find = new Finder<String, Game>(
			String.class, Game.class);

	/**
	 * Gib mir alle vorhandenen Spiele
	 * 
	 * @return List<Game>
	 */
	public static List<Game> findByAll() {

		return Ebean.find(Game.class).fetch("playerGameStatus")
				.fetch("playground").findList();
	}

	/**
	 * Füge einen Spieler hinzu.
	 * 
	 * @param playerGameStatus
	 */
	public void addPlayer(Player player) {

		PlayerGameStatus playerGameStatus = new PlayerGameStatus(player);
		// setze currentPoint auf Startpunkt etc.
		playerGameStatus.save();
		this.playerGameStatus.add(playerGameStatus);
	}
}
