package de.saxsys.treasurehunting.common.models.playgrounds;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

/**
 * A {@link Path} represents a directed graph between two {@link Point}s.
 * 
 * @author justus.markert
 */
@Entity
public class Path extends Model {

	private static final long serialVersionUID = -5967830143439716451L;

	/**
	 * Internal ID.
	 */
	@Id
	public long id;

	/**
	 * Source {@link Point} of the directed graph.
	 * 
	 * Problem: If we use CascadeType.ALL the Point is automatically deleted
	 * too. But what if it is at the same time the startpoint of a playground?
	 */
	@Required
	@ManyToOne
	public Point fromPoint;

	/**
	 * Sink {@link Point} of the directed graph.
	 */
	@Required
	@ManyToOne
	public Point toPoint;

}
