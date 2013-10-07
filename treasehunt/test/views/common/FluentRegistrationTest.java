/**
 * 
 */
package views.common;

import static play.test.Helpers.FIREFOX;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;
import static play.test.Helpers.testServer;

import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import views.common.callbacks.SimpleAuthCallbackComposite;

/**
 * Einfache Testklasse zur Demonstration der Fuent Lenium API unter Verwendung
 * der Browser Firefox (v17-), Chrome (aktuell) und Internet Explorer.
 * 
 * @author stefan.illgen
 * 
 */
public class FluentRegistrationTest {

	@Test
	public void simpleAuthFirefox() {
		running(testServer(3333, fakeApplication(inMemoryDatabase())), FIREFOX,
				new SimpleAuthCallbackComposite());
	}
	
	/**
	 * ACHTUNG: Installation des chromedrivers erforderlich!
	 * (siehe https://code.google.com/p/selenium/wiki/ChromeDriver)
	 */
	@Test
	public void simpleAuthChrome() {
		System.setProperty("webdriver.chrome.driver", "d:/DEVEL/ide/chromedriver_win32_2.0/chromedriver.exe");
		running(testServer(3333, fakeApplication(inMemoryDatabase())), ChromeDriver.class,
				new SimpleAuthCallbackComposite());
	}
	
	@Test
	public void simpleAuthIE() {
		running(testServer(3333, fakeApplication(inMemoryDatabase())), InternetExplorerDriver.class,
				new SimpleAuthCallbackComposite());
	}

}
