/**
 * 
 */
package de.saxsys.treasurehunting.common.services.user;

import org.junit.Assert;
import org.junit.Test;

import de.saxsys.treasurehunting.common.models.BaseModelTest;
import de.saxsys.treasurehunting.common.models.user.User;
import de.saxsys.treasurehunting.common.services.UserService;

/**
 * This class tests the service class {@link User}.
 * 
 * @author stefan.illgen
 * 
 */
public class UserServiceTest extends BaseModelTest {

	/**
	 * This operation tests the operation {@link UserService#findUser(String)}
	 * to find a user inside by using {@link UserService} class.
	 */
	@Test
	public void findUser() {
		User user = UserService.findUser("stefan");
		Assert.assertNotNull(user);
		Assert.assertTrue(user.name.compareTo("stefan") == 0);
	}
}
