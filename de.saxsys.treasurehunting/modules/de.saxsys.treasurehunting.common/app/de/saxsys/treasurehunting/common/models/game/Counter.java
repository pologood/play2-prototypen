/**
 * 
 */
package de.saxsys.treasurehunting.common.models.game;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.omg.CORBA.Current;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import de.saxsys.treasurehunting.common.models.playgrounds.Point;
import de.saxsys.treasurehunting.common.models.user.User;

/**
 * The {@link Counter} of a {@link User} is identified by its {@link #color},
 * the amount of collected {@link #cards} and its {@link Current}
 * {@link #position}. Further it is related to exactly one user.
 * 
 * @author stefan.illgen
 * 
 */
@Entity
public class Counter extends Model {

	private static final long serialVersionUID = 8369627631260566579L;
	
	/**
	 * Default color red.
	 */
	public static Integer COUNTER_COLOR_RED = Integer.valueOf(0xff0000);
	
	/**
	 * Default color green.
	 */
	public static Integer COUNTER_COLOR_GREEN = Integer.valueOf(0xff00);
	
	/**
	 * Default color blue.
	 */
	public static Integer COUNTER_COLOR_BLUE = Integer.valueOf(0xff);

	/**
	 * Internal ID.
	 */
	@Id
	public long id;

	/**
	 * The {@link Counter}s color.
	 */
	@Required
	public int color;

	/**
	 * The {@link Counter}s amount of collected cards.
	 */
	@Required
	public int cards;

	/**
	 * The current position of the {@link Counter} represented by a
	 * {@link Point} .
	 */
	@ManyToOne
	public Point position;
	
	/**
	 * The bidirectional user.
	 */
	@ManyToOne
	public User user;
}
