/**
 * 
 */
package de.saxsys.treasurehunting.common.models.playgrounds;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import com.avaje.ebean.Ebean;

import de.saxsys.treasurehunting.common.models.BaseModelTest;
import de.saxsys.treasurehunting.common.models.actions.Action;

/**
 * Test for testing basic CRUD operations.
 * 
 * @author stefan.illgen
 * 
 */
public class PointTest extends BaseModelTest {

	/**
	 * Tests creation.
	 */
	@Test
	public void testCreate() {
		Point point = new Point();
		point.x = 100;
		point.y = 100;
		point.save();
		assertThat(point.id).isNotNull();
		point.delete();
	}

	/**
	 * Tests read.
	 */
	@Test
	public void testRead() {
		List<Point> allPoints = Ebean.find(Point.class).findList();
		assertThat(allPoints).isNotEmpty();
		for (Point point : allPoints) {
			assertThat(point.x).isNotNull();
			assertThat(point.y).isNotNull();
			assertThat(point.id).isNotNull();
		}
	}

	/**
	 * Tests update.
	 * 
	 * For a {@link Point} with an start Action associated, change x coordinate
	 * and save changes.
	 */
	@Test
	public void testUpdate() {
		List<Point> allPoints = Ebean.find(Point.class).findList();
		for (Point point : allPoints) {
			// For a {@link Point} with an start Action associated
			for (Action action : point.actions)
				if (action.heading.compareTo("Spielstart")==0) {
					// change x coordinate
					point.x = 120;
					// save changes
					point.save();
					// If there is no exception thrown, the update was
					// successful.
					return;
				}
		}
	}

	/**
	 * Tests the deletion of a {@link Point}.
	 */
	@Test
	public void testDelete() {
		
	}

}
