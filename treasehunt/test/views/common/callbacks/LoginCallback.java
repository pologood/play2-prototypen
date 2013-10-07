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
public class LoginCallback extends Callback {

	@Override
	public void invoke(TestBrowser browser) throws Throwable {

		browser.goTo("http://localhost:3333/login");
		browser.fill("#email").with("neglli@gmx.de");
		browser.fill("#password").with("55555");
		syncWaitFor(1);
		browser.submit("#loginBtn");
		// assertThat("").contains("");
		syncWaitFor(3);
	}

}
