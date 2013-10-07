/**
 * 
 */
package de.saxsys.treasurehunting.admin.controllers;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import play.mvc.Result;
import play.test.Helpers;

/**
 * This test class tests the redirect to the start page of the application if
 * the user is'n authorized.
 * 
 * @author stefan.illgen
 * 
 */
public class AdminControllerAuthTest {

	/**
	 * This operation runs the authorization test.
	 */
	@Test
	public void index() {		
		Helpers.running(Helpers.fakeApplication(), new Runnable() {
            @Override
            public void run() {
        		Result result = Helpers.callAction(routes.ref.AdminController.index());
        		assertThat(Helpers.redirectLocation(result)).isEqualTo("/");
            }
		});
	}
	
}
