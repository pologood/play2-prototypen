/**
 * 
 */
package de.saxsys.treasurehunting.common.views;

import static org.fest.assertions.Assertions.assertThat;
import play.test.TestBrowser;
import de.saxsys.treasurehunting.views.callbacks.Callback;

/**
 * This callback implements a successful login procedure and may be reused for
 * composite test callbacks.
 * 
 * @author stefan.illgen
 */
public class LoginSuccessCallback extends Callback {

	@Override
	public void invoke(TestBrowser browser) throws Throwable {

		browser.goTo("http://localhost:3333/").await().atMost(10000);
		// select german language
		browser.click("#a-de").await().atMost(10000);
		// fill the user name
		browser.fill("#username").with("stefan");
		browser.await().atMost(10000);
		// submit the form
		browser.click("#btn-start").await().atMost(10000);
		browser.await().atMost(10000);
		// redirect to /game
		assertThat(browser.url()).isEqualTo("http://localhost:3333/game");
	}

}
