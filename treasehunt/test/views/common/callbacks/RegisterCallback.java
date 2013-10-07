/**
 * 
 */
package views.common.callbacks;

import static org.fest.assertions.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import play.test.TestBrowser;
import views.callbacks.Callback;

/**
 * Der {@link Callback} für das automatische Testen des Registrierens.
 * 
 * @author stefan.illgen
 * 
 */
public class RegisterCallback extends Callback {

	/**
	 * Der Testfall verlangt nach erfolgreicher Registrierung das Vorhandensein
	 * von "registrationSuccess" im HTML-Content.
	 */
	@Override
	public void invoke(TestBrowser browser) throws Throwable {

		browser.goTo("http://localhost:3333/signup");

		browser.fill("#name").with("stefan");
		browser.fill("#email").with("neglli@gmx.de");
		browser.fill("#password").with("55555");
		browser.fill("#repeatPassword").with("55555");
		syncWaitFor(1);

		browser.submit("#registerBtn");
		syncWaitFor(1);

		String verificationURL = getVerifikationURL();
		System.out.println("Verification-URL: " + verificationURL);
		browser.goTo(verificationURL);
		syncWaitFor(1);

		assertThat(browser.pageSource()).contains("registrationSuccess");
	}

	/**
	 * Liest die Log-Datei zur Extraktion der URL zur Verifikation aus.
	 * 
	 * @return
	 */
	private String getVerifikationURL() {

		final String verifyURL = "http://localhost:3333/accounts/verify/";
		final int tokenLength = 36;
		List<String> allLines = new ArrayList<String>();

		try {
			BufferedReader in = new BufferedReader(new FileReader(
					"logs/application.log"));
			String nextLine = in.readLine();
			do {
				allLines.add(nextLine);
				nextLine = in.readLine();
			} while (nextLine != null);
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = allLines.size() - 1; i >= 0; i--) {
			String aLine = allLines.get(i);
			int firstOccurence = aLine.indexOf(verifyURL);
			if (firstOccurence != -1) {
				String result = new String();
				int to = firstOccurence + tokenLength + verifyURL.length();
				result = aLine.substring(firstOccurence, to);
				return result;
			}
		}

		return null;
	}

}
