/**
 * 
 */
package de.saxsys.treasurehunting.common.models.user;

import org.junit.Assert;
import org.junit.Test;

import com.avaje.ebean.Ebean;

import de.saxsys.treasurehunting.common.models.BaseModelTest;

/**
 * Test for testing basic CRUD operations.
 * 
 * @author stefan.illgen
 *
 */
public class UserTest extends BaseModelTest {

	/**
	 * Tests creation.
	 */
	@Test
	public void testCreate() {
		
	}
	
	/**
	 * Tests read.
	 */
	@Test
	public void testRead() {
		User user = Ebean.find(User.class, "stefan");
		Assert.assertNotNull(user);
		Assert.assertEquals(user.name, "stefan");
	}

	/**
	 * Tests update.
	 */
	@Test
	public void testUpdate() {
		
	}

	/**
	 * Tests deletion.
	 */
	@Test
	public void testDelete() {
		
	}
	
}
