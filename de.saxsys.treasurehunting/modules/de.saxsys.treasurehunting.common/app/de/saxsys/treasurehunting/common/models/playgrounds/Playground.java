package de.saxsys.treasurehunting.common.models.playgrounds;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

/**
 * A Playground represents a ground for playing the game. It consists out of
 * {@link Path}s, a start- and an endpoint of type {@link Point}.
 * 
 * @author justus.markert
 */
@Entity
public class Playground extends Model {

	private static final long serialVersionUID = -4414282978639082476L;

	/**
	 * The name of the playground.
	 */
	@Id
	public String name;

	/**
	 * The background of the {@link Playground}.
	 */
	@Required
	public URI background;

	/**
	 * The width of the {@link Playground}.
	 */
	@Required
	public int width;

	/**
	 * The height of the {@link Playground}.
	 */
	@Required
	public int height;

	/**
	 * The {@link Path}s related to the {@link Playground}.
	 * 
	 * Propagates only persist to its children. Thus if the Playground is deleted the
	 * related {@link Path} won't be removed.
	 */
	@ManyToMany(cascade = CascadeType.ALL)
	public List<Path> paths = new ArrayList<Path>();

	/**
	 * The start point of the {@link Playground}.
	 */
	@Required
	@ManyToOne
	public Point startPoint;

	/**
	 * The end point of the {@link Playground}.
	 */
	@Required
	@ManyToOne
	public Point endPoint;

}
