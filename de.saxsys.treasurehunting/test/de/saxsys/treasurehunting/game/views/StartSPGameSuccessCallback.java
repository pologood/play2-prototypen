/**
 * 
 */
package de.saxsys.treasurehunting.game.views;

import static org.fest.assertions.Assertions.assertThat;
import play.test.TestBrowser;
import de.saxsys.treasurehunting.views.callbacks.Callback;

/**
 * @author stefan.illgen
 *
 */
public class StartSPGameSuccessCallback extends Callback {

	@Override
	public void invoke(TestBrowser browser) throws Throwable {
		browser.fill("#gameName").with("Spiel 4");
		browser.await().atMost(10000);
		browser.click("#btn-startSPGame").await().atMost(10000);
		assertThat(browser.url()).startsWith(
				"http://localhost:3333/game/singleplayer");
	}

}
