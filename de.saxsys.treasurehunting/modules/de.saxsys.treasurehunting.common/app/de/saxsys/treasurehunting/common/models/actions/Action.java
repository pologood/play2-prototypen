/**
 * 
 */
package de.saxsys.treasurehunting.common.models.actions;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

/**
 * An {@link Action} represent an action, which is identified by a
 * {@link #heading}, an {@link #info} and a {@link #button} text.
 * 
 * @author stefan.illgen
 */
@Entity
public class Action extends Model {

	private static final long serialVersionUID = 7792547412100770281L;

	/**
	 * Type of {@link Action} identifying the initialization of a game.
	 */
	public static final int TYPE_INITIALIZE_GAME = 0;

	/**
	 * Type of {@link Action} identifying the reinitialization of a game due to
	 * a connection loss and/or due to the resetting of the language.
	 */
	public static final int TYPE_REINITIALIZE_GAME = 3;

	/**
	 * Type of {@link Action} identifying the start of a game.
	 */
	public static final int TYPE_START_GAME = 1;

	/**
	 * Type of {@link Action} identifying the finish of a game.
	 */
	public static final int TYPE_FINISH_GAME = 2;
	
	/**
	 * Type of {@link Action} identifying the move of a game.
	 */
	public static final int TYPE_MOVE = 4;
	
	/**
	 * Type of {@link Action} identifying the throw dice of a game.
	 */
	public static final int TYPE_THROW_DICE = 5;
	
	/**
	 * Type of {@link Action} identifying the dice repeat of a game.
	 */
	public static final int TYPE_DICE_REPEAT = 6;
	
	/**
	 * Type of {@link Action} identifying the restart of a game.
	 */
	public static final int TYPE_RESTART_GAME = 7;
	
	/**
	 * Type of {@link Action} identifying the pause of a game.
	 */
	public static final int TYPE_PAUSE_GAME = 8;
	
	/**
	 * Type of {@link Action} identifying the resume of a game.
	 */
	public static final int TYPE_RESUME_GAME = 9;
	
	/**
	 * Type of {@link Action} identifying an point of interest (POI).
	 */
	public static final int TYPE_POI = 10;
	
	/**
	 * Type of {@link Action} identifying the canceling of a game.
	 */
	public static final int TYPE_CANCEL_GAME = 11;

	/**
	 * Internal ID.
	 */
	@Id
	public long id;

	/**
	 * The heading of the {@link Action}.
	 */
	@Required
	public String heading;

	/**
	 * The info text of the {@link Action}.
	 */
	@Required
	public String info;

	/**
	 * The label of the button to confirm the {@link Action}.
	 */
	@Required
	public String button;
	
	/**
	 * The type of the {@link Action}.
	 */
	public int type;
}
