/**
 * 
 */
package de.saxsys.treasurehunting.game.views;

import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import play.test.Helpers;

/**
 * This {@link Test} tests the following aspects for the game index page.
 * 
 * <ul>
 * <li>Successful and error prone behavior of the user for starting a game in
 * single player mode.</li>
 * </ul>
 * 
 * @author stefan.illgen
 * 
 */
public class IndexPageTest {

	/**
	 * Tests successful start of a single player game.
	 */
	@Test
	public void startSPGameSuccessFirefox() {
		Helpers.running(Helpers.testServer(3333, Helpers.fakeApplication()),
				Helpers.FIREFOX, new StartSPGameSuccessCompositeCallback());
	}

	/**
	 * Tests successful start of a single player game.
	 */
	@Test
	public void startSPGameSuccessChrome() {
		System.setProperty("webdriver.chrome.driver",
				"test/de/saxsys/treasurehunting/webdrivers/chromedriver.exe");
		Helpers.running(Helpers.testServer(3333, Helpers.fakeApplication()),
				ChromeDriver.class, new StartSPGameSuccessCompositeCallback());
	}

	/**
	 * Tests successful start of a single player game.
	 */
	@Test
	public void startSPGameSuccessIE() {
		System.setProperty("webdriver.ie.driver",
				"test/de/saxsys/treasurehunting/webdrivers/IEDriverServer.exe");
		Helpers.running(Helpers.testServer(3333, Helpers.fakeApplication()),
				InternetExplorerDriver.class, new StartSPGameSuccessCompositeCallback());
	}

	/**
	 * Tests error prone start of a single player game.
	 */
	@Test
	public void startSPGameErrorFirefox() {
		Helpers.running(Helpers.testServer(3333, Helpers.fakeApplication()),
				Helpers.FIREFOX, new StartSPGameErrorCompositeCallback());
	}

	/**
	 * Tests error prone start of a single player game.
	 */
	@Test
	public void startSPGameErrorChrome() {
		System.setProperty("webdriver.chrome.driver",
				"test/de/saxsys/treasurehunting/webdrivers/chromedriver.exe");
		Helpers.running(Helpers.testServer(3333, Helpers.fakeApplication()),
				ChromeDriver.class, new StartSPGameErrorCompositeCallback());
	}

	/**
	 * Tests error prone start of a single player game.
	 */
	@Test
	public void startSPGameErrorIE() {
		System.setProperty("webdriver.ie.driver",
				"test/de/saxsys/treasurehunting/webdrivers/IEDriverServer.exe");
		Helpers.running(Helpers.testServer(3333, Helpers.fakeApplication()),
				InternetExplorerDriver.class, new StartSPGameErrorCompositeCallback());
	}

}
