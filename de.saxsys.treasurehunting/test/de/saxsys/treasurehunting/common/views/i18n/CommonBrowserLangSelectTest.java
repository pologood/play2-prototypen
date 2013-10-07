package de.saxsys.treasurehunting.common.views.i18n;

import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import play.test.Helpers;

/**
 * This class provides Tests for language selection.
 * 
 * @author stefan.illgen
 */
public class CommonBrowserLangSelectTest {

	/**
	 * Test method for selecting the german language for browser Firefox (v17+).
	 */
	@Test
	public void langSelectDEFirefox() {
		Helpers.running(Helpers.testServer(3333, Helpers.fakeApplication()),
				Helpers.FIREFOX, new SelectLangDECallback());
	}

	/**
	 * Test method for selecting the german language for browser
	 * Chrome/Chromium.
	 */
	@Test
	public void langSelectDEChrome() {
		System.setProperty("webdriver.chrome.driver",
				"test/de/saxsys/treasurehunting/webdrivers/chromedriver.exe");
		Helpers.running(Helpers.testServer(3333, Helpers.fakeApplication()),
				ChromeDriver.class, new SelectLangDECallback());
	}

	/**
	 * Test method for selecting the german language for browser IE.
	 */
	@Test
	public void langSelectDEIE() {
		System.setProperty("webdriver.ie.driver",
				"test/de/saxsys/treasurehunting/webdrivers/IEDriverServer.exe");
		Helpers.running(Helpers.testServer(3333, Helpers.fakeApplication()),
				InternetExplorerDriver.class, new SelectLangDECallback());
	}

	/**
	 * Test method for selecting the english language for browser Firefox
	 * (v17+).
	 */
	@Test
	public void langSelectENFirefox() {
		Helpers.running(Helpers.testServer(3333, Helpers.fakeApplication()),
				Helpers.FIREFOX, new SelectLangENCallback());
	}

	/**
	 * Test method for selecting the english language for browser
	 * Chrome/Chromium.
	 */
	@Test
	public void langSelectENChrome() {
		System.setProperty("webdriver.chrome.driver",
				"test/de/saxsys/treasurehunting/webdrivers/chromedriver.exe");
		Helpers.running(Helpers.testServer(3333, Helpers.fakeApplication()),
				ChromeDriver.class, new SelectLangENCallback());
	}

	/**
	 * Test method for selecting the english language for browser IE.
	 */
	@Test
	public void langSelectENIE() {
		System.setProperty("webdriver.ie.driver",
				"test/de/saxsys/treasurehunting/webdrivers/IEDriverServer.exe");
		Helpers.running(Helpers.testServer(3333, Helpers.fakeApplication()),
				InternetExplorerDriver.class, new SelectLangENCallback());
	}

}
