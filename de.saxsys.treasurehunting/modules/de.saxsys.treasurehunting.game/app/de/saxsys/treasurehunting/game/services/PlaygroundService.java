/**
 * 
 */
package de.saxsys.treasurehunting.game.services;

import java.util.ArrayList;
import java.util.List;

import play.db.ebean.Model;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;

import de.saxsys.treasurehunting.common.models.playgrounds.Playground;

/**
 * This class provides the logic for managing playgrounds, paths and points.
 * 
 * @author stefan.illgen
 * 
 */
public abstract class PlaygroundService {

	private static Model.Finder<String, Playground> find = new Model.Finder<String, Playground>(
			String.class, Playground.class);

	/**
	 * Returns a list of all playground names.
	 * 
	 * @return Returns a list of all playground names.
	 */
	public static List<String> getAllPlaygroundNames() {
		return new ArrayList<String>() {
			private static final long serialVersionUID = -3717111435695650045L;
			{
				for(Playground playground : Ebean.find(Playground.class).findList())
					add(playground.name);
			}
		};
	}

	/**
	 * Finds a Playground by its name (== id).
	 * 
	 * @param playgroundName
	 *            The name of the playground.
	 * @return Returns the {@link Playground} or null, if no {@link Playground}
	 *         was found.
	 */
	public static Playground findPlayground(String playgroundName) {

		if (playgroundName != null && find.where() != null) {
			ExpressionList<Playground> expL = find.where().eq("name",
					playgroundName);
			return expL.findUnique();
		}

		return null;
	}

}
