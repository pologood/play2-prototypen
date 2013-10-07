/**
 * 
 */
package de.saxsys.treasurehunting.common.views.nav;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import play.test.TestBrowser;
import de.saxsys.treasurehunting.views.callbacks.Callback;

/**
 * This {@link Callback} checks the containment of the text value
 * <code>stefan</code> given by the HTML element <code>#lbl-logged-uname</code>.
 * 
 * @author stefan.illgen
 */
public class NavUsernameCallback extends Callback {

	@Override
	public void invoke(TestBrowser browser) throws Throwable {
		List<String> sUnames = browser.text("#lbl-logged-uname");
		assertThat(sUnames.get(0)).contains("stefan");
	}

}
