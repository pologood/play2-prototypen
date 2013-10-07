package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

/**
 * Spielerklasse
 * 
 * @author justus.markert
 * 
 */
@Entity
public class Player extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8530457492299214425L;

	/**
	 * Interne Id
	 */
	@Id
	long id;

	/**
	 * Spielername
	 */
	@Required
	String name;
	
	@Required
	public String language;

	/**
	 * Konstruktor
	 * 
	 * @param name
	 */
	public Player(String name) {

		this.name = name;
	}
}
