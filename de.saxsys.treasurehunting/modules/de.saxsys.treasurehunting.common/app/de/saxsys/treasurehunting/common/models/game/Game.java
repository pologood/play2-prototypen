/**
 * 
 */
package de.saxsys.treasurehunting.common.models.game;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import de.saxsys.treasurehunting.common.models.playgrounds.Playground;

/**
 * A {@link Game} represents exactly one game with an arbitrary horizontal game
 * mode. It encapsulated the state of the game and enables it to be persisted.
 * 
 * @author stefan.illgen
 */
@Entity
public class Game extends Model {

	private static final long serialVersionUID = 3767961911878891102L;

	/**
	 * Game state ready.
	 */
	public static final int STATE_CREATED = 1;

	/**
	 * Game state ready.
	 */
	public static final int STATE_READY = 2;

	/**
	 * Game state active.
	 */
	public static final int STATE_ACTIVE = 3;

	/**
	 * Game state dicing (sub state of active).
	 */
	public static final int STATE_DICING = 31;

	/**
	 * Game state moving (sub state of active).
	 */
	public static final int STATE_MOVING = 32;

	/**
	 * Game state for performing a POI action (sub state of active).
	 */
	public static final int STATE_PERFORMING_POI = 33;

	/**
	 * Game state for performing a Dice Repeat action (sub state of active).
	 */
	public static final int STATE_PERFORMING_DICE_REPEAT = 34;

	/**
	 * Game state finished.
	 */
	public static final int STATE_FINISHED = 4;

	/**
	 * Game state paused.
	 */
	public static final int STATE_PAUSED = 5;

	/**
	 * Horizontal (stand alone) game mode for single player games.
	 */
	public static final int H_MODE_SINGLEPLAYER = 1;

	/**
	 * Internal ID.
	 */
	@Id
	public long id;

	/**
	 * The name of the {@link Game}.
	 */
	@Required
	public String name;

	/**
	 * The super state of the {@link Game}.
	 */
	@Required
	public int superState;

	/**
	 * The sub state of the {@link Game}.
	 */
	@Required
	public int subState;

	/**
	 * The horizontal mode flag of the {@link Game}.
	 */
	@Required
	public int hMode;

	/**
	 * The flag indicating the active counter in the queue of counters.
	 */
	public int activeCounter;

	/**
	 * The queue of counters which is traversed in a round trip manner.
	 */
	@OneToMany(cascade = CascadeType.PERSIST)
	public List<Counter> counters = new ArrayList<Counter>();

	/**
	 * The {@link Playground} the {@link Game} is related to.
	 */
	@ManyToOne
	public Playground playground;
}
