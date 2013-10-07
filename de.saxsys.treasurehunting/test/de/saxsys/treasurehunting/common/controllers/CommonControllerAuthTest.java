/**
 * 
 */
package de.saxsys.treasurehunting.common.controllers;

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
public class CommonControllerAuthTest {

	/**
	 * This operation runs the authorization test.
	 */
	@Test
	public void index() {		
		Helpers.running(Helpers.fakeApplication(), new Runnable() {
            @Override
            public void run() {
        		Result result = Helpers.callAction(routes.ref.CommonController.index());
        		assertThat(result).isNotNull();
        		assertThat(Helpers.status(result)).isEqualTo(Helpers.OK);
                assertThat(Helpers.contentType(result)).isEqualTo("text/html");
                assertThat(Helpers.charset(result)).isEqualTo("utf-8");
                assertThat(Helpers.contentAsString(result)).contains("div-start");
            }
		});
	}

}
