/**
 * 
 */
package de.saxsys.treasurehunting.common.views.nav;

import java.io.File;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;

import play.test.Helpers;
import play.test.TestBrowser;
import play.test.TestServer;

/**
 * This test class tests the navigation for different browsers.
 * 
 * @author stefan.illgen
 * 
 */
public class CommonBrowserNavigationTest {

	// Commented out due to unresolvable behaviour for Firefox (see devdoc)
	// /**
	// * Test method for running a successful login and the right presentation
	// of
	// * the username for browser Firefox (v17+).
	// *
	// *
	// */
	// @Test
	// public void navUsernameFirefox() {
	// Helpers.running(
	// Helpers.testServer(3333,
	// Helpers.fakeApplication()),
	// Helpers.FIREFOX, new NavUsernameCallbackComposite());
	// }

	/**
	 * Test method for running a successful login and the right presentation of
	 * the username for browser Chrome/Chromium.
	 */
	@Test
	public void navUsernameChrome() {
		System.setProperty("webdriver.chrome.driver",
				"test/de/saxsys/treasurehunting/webdrivers/chromedriver.exe");
		Helpers.running(Helpers.testServer(3333, Helpers.fakeApplication()),
				ChromeDriver.class, new NavUsernameCallbackComposite());
	}

	/**
	 * Test method for running a successful login for browser IE.
	 */
	@Test
	public void navUsernameIE() {
		System.setProperty("webdriver.ie.driver",
				"test/de/saxsys/treasurehunting/webdrivers/IEDriverServer.exe");
		Helpers.running(Helpers.testServer(3333, Helpers.fakeApplication()),
				InternetExplorerDriver.class,
				new NavUsernameCallbackComposite());
	}

	/**
	 * Test method for running a successful login and the right presentation of
	 * the activity state of the game navigation link, which must be active, for
	 * browser Firefox (v17+).
	 */
	@Test
	public void navGameFirefox() {
		Helpers.running(Helpers.testServer(3333, Helpers.fakeApplication()),
				Helpers.FIREFOX, new NavGameCallbackComposite());
	}

	/**
	 * Test method for running a successful login and the right presentation of
	 * the activity state of the game navigation link, which must be active, for
	 * browser Chrome/Chromium.
	 */
	@Test
	public void navGameChrome() {
		System.setProperty("webdriver.chrome.driver",
				"test/de/saxsys/treasurehunting/webdrivers/chromedriver.exe");
		Helpers.running(Helpers.testServer(3333, Helpers.fakeApplication()),
				ChromeDriver.class, new NavGameCallbackComposite());
	}

	/**
	 * Test method for running a successful login and the right presentation of
	 * the activity state of the game navigation link, which must be active, for
	 * browser IE.
	 */
	@Test
	public void navGameIE() {
		System.setProperty("webdriver.ie.driver",
				"test/de/saxsys/treasurehunting/webdrivers/IEDriverServer.exe");
		Helpers.running(Helpers.testServer(3333, Helpers.fakeApplication()),
				InternetExplorerDriver.class, new NavGameCallbackComposite());
	}

	/**
	 * Test method for running a successful login and the right presentation of
	 * the activity state of the admin navigation link, which must be active,
	 * for browser Firefox (v17+).
	 */
	@Test
	public void navAdminFirefox() {
		Helpers.running(Helpers.testServer(3333, Helpers.fakeApplication()),
				Helpers.FIREFOX, new NavAdminCallbackComposite());
	}
	
	/**
	 * @author andre.tschirch
	 * 
	 * Example test method for using specific Firefox version e.g. v17. 
	 *   
	 * Test method for running a successful login and the right presentation of
	 * the activity state of the admin navigation link, which must be active,
	 * for browser Firefox.
	 */
//	@Test
	public void navAdminFirefox17() {
		ProfilesIni profile = new ProfilesIni();
		FirefoxProfile firefoxProfile = profile.getProfile("firefox17");
		WebDriver driver = new FirefoxDriver(new FirefoxBinary(new File("D:/schatzsuche/firefox17/firefox.exe")), firefoxProfile);
		TestBrowser browser = Helpers.testBrowser(driver, 3333);
		
		TestServer server = Helpers.testServer(3333, Helpers.fakeApplication());
		TestServer startedServer = null;
		try {
			server.start();
			startedServer = server;
			new NavAdminCallbackComposite().invoke(browser);
		} catch(Throwable t) {
            throw new RuntimeException(t);
        } finally {
            if(browser != null) {
                browser.quit();
            }
            if(startedServer != null) {
                startedServer.stop();
            }
        }
	}
	
	/**
	 * Test method for running a successful login and the right presentation of
	 * the activity state of the admin navigation link, which must be active,
	 * for browser Chrome/Chromium.
	 */
	@Test
	public void navAdminChrome() {
		System.setProperty("webdriver.chrome.driver",
				"test/de/saxsys/treasurehunting/webdrivers/chromedriver.exe");
		Helpers.running(Helpers.testServer(3333, Helpers.fakeApplication()),
				ChromeDriver.class, new NavAdminCallbackComposite());
	}
	
	/**
	 * Test method for running a successful login and the right presentation of
	 * the activity state of the admin navigation link, which must be active, for
	 * browser IE.
	 */
	@Test
	public void navAdminIE() {
		System.setProperty("webdriver.ie.driver",
				"test/de/saxsys/treasurehunting/webdrivers/IEDriverServer.exe");
		Helpers.running(Helpers.testServer(3333, Helpers.fakeApplication()),
				InternetExplorerDriver.class, new NavAdminCallbackComposite());
	}

}
