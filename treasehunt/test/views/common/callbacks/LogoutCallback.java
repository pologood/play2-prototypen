/**
 * 
 */
package views.common.callbacks;

import play.test.TestBrowser;
import views.callbacks.Callback;

/**
 * @author stefan.illgen
 *
 */
public class LogoutCallback extends Callback {

	@Override
	public void invoke(TestBrowser browser) throws Throwable {
		
		browser.goTo("http://localhost:3333");
		browser.click("#userMenuDropdown");
		browser.click("#logoutBtn");
		// assertThat("").contains("");
		syncWaitFor(3);
	}

}
