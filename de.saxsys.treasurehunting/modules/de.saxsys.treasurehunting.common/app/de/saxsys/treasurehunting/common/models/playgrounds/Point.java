package de.saxsys.treasurehunting.common.models.playgrounds;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import de.saxsys.treasurehunting.common.models.actions.Action;
import de.saxsys.treasurehunting.common.models.game.Counter;

/**
 * Represents a point on the {@link Playground}, onto which multiple
 * {@link Counter}s may be positioned.
 * 
 * @author justus.markert
 */
@Entity
public class Point extends Model implements Comparable<Point> {

	private static final long serialVersionUID = 1846958748629611213L;

	/**
	 * Internal Id.
	 */
	@Id
	public long id;

	/**
	 * Position x on the {@link Playground}.
	 */
	@Required
	public int x;

	/**
	 * Position y on the {@link Playground}.
	 */
	@Required
	public int y;

	/**
	 * A {@link Point} may have multiple {@link Action}s related.
	 * 
	 * 
	 */
	@ManyToMany(cascade=CascadeType.ALL)
	public List<Action> actions = new ArrayList<Action>();

	/**
	 * Implementation for {@link Comparable} interface.
	 */
	@Override
	public int compareTo(Point o) {
		if (o.id > this.id)
			return -1;
		else if (o.id < this.id)
			return 1;
		else
			return 0;
	}
}
