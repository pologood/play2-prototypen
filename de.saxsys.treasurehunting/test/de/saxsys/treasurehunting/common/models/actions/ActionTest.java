/**
 * 
 */
package de.saxsys.treasurehunting.common.models.actions;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import play.i18n.Lang;
import play.i18n.Messages;

import com.avaje.ebean.Ebean;

import de.saxsys.treasurehunting.common.models.BaseModelTest;
import de.saxsys.treasurehunting.common.models.playgrounds.Point;

/**
 * Test for testing basic CRUD operations.
 * 
 * @author stefan.illgen
 * 
 */
public class ActionTest extends BaseModelTest {

	/**
	 * Tests individual creation without associating with a specific point.
	 */
	@Test
	public void testCreate() {
		Action startGame = new Action();
		startGame.heading = Messages.get(Lang.forCode("en"),
				"common.actions.StartGame.heading");
		startGame.info = Messages.get(Lang.forCode("en"),
				"common.actions.StartGame.info");
		startGame.button = Messages.get(Lang.forCode("en"),
				"common.actions.StartGame.button");
		startGame.save();
		assertThat(startGame.id).isNotNull();
		// delete specific action from all points
		deleteManyToMany(startGame);
		startGame.delete();
	}

	/**
	 * Tests read.
	 */
	@Test
	public void testRead() {
		List<Action> allActions = Ebean.find(Action.class).findList();
		assertThat(allActions).isNotEmpty();
		for (Action action : allActions) {
			assertThat(action.heading).isNotNull();
			assertThat(action.info).isNotNull();
			assertThat(action.button).isNotNull();
		}
	}

	/**
	 * Tests update.
	 * 
	 * If the update of {@link Action} with heading "Spielstart" to
	 * "Spielstart1" is successful, there is no exception thrown while saving
	 * the updated action.
	 */
	@Test
	public void testUpdate() {
		List<Action> allActions = Ebean.find(Action.class).findList();
		for (Action action : allActions) {
			if (action.heading.compareTo("Spielstart") == 0) {
				action.heading = "Spielstart1";
				action.save();
				// If there is no exception thrown, the update was successful.
				return;
			}
		}

		// we should not come here
		Assert.assertEquals(
				"Action with heading \"Spielstart\" was not found!", true,
				false);
	}

	/**
	 * Tests deletion.
	 */
	@Test
	public void testDelete() {

		// delete the action
		List<Action> allActions = Ebean.find(Action.class).findList();
		for (Action action : allActions) {
			if (action.heading.compareTo("Spielstart") == 0) {

				// delete specific action from all points
				deleteManyToMany(action);

				// delete the action
				action.delete();
				
				// check if association from Point is updated
				for (Point point : Ebean.find(Point.class).findList()) {
					assertThat(action).isNotIn(point.actions);
				}

				return;
			}
		}

		// we should not come here
		Assert.assertEquals(
				"Action with heading \"Spielstart\" was not found!", true,
				false);
	}

	private void deleteManyToMany(Action action) {
		for (Point point : Ebean.find(Point.class).findList()) {
			for (Action anAction : point.actions) {
				if (anAction.id == action.id) {
					point.deleteManyToManyAssociations("actions");
				}
			}
		}
	}

}
