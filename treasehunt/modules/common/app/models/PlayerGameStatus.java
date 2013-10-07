package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

/**
 * Repräsentiert den Status eines Mitspielers in einem Spiel 
 * 
 * @author justus.markert
 *
 */
@Entity
public class PlayerGameStatus extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4744406446190693885L;

	/**
	 * Interne Id
	 */
	@Id
	long id;

	/**
	 * Der Spieler
	 */
	@Required
	public Player player;
	
	/**
	 * Die aktuelle Position des Spielers auf dem Spielfeld
	 */
	Point currentPosition;
	
	/**
	 * Seine bereits besuchten Punkte
	 */
	List<Point> reachedPoints = new ArrayList<Point>();

	/**
	 * Konstruktor
	 * 
	 * @param player
	 */
	public PlayerGameStatus(Player player) {

		this.player = player;
	}
}
