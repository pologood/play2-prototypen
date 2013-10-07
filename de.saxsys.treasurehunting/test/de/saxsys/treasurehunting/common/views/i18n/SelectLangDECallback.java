/**
 * 
 */
package de.saxsys.treasurehunting.common.views.i18n;

import static org.fest.assertions.Assertions.assertThat;
import de.saxsys.treasurehunting.views.callbacks.Callback;
import play.i18n.Lang;
import play.i18n.Messages;
import play.test.TestBrowser;

/**
 * This class provides a {@link Callback} for testing the german language
 * selection.
 * 
 * @author stefan.illgen
 */
public class SelectLangDECallback extends Callback {

	@Override
	public void invoke(TestBrowser browser) throws Throwable {
		browser.goTo("http://localhost:3333/").await().atMost(10000);
		// select german language
		browser.click("#a-de").await().atMost(10000);
		// assert
		assertThat(browser.text("#div-lbl-lang")).contains(
				Messages.get(Lang.forCode("de"), "common.lang.label"));
	}
}
