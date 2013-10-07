/**
 * 
 */
package de.saxsys.treasurehunting.views.callbacks;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;

import play.test.TestBrowser;
import com.google.common.base.Function;

/**
 * Abstrakte Basisklasse zur Implementierung eines {@link play.libs.F.Callback}s
 * für {@link TestBrowser}.
 * 
 * 
 * @author stefan.illgen
 * 
 */
public abstract class Callback implements play.libs.F.Callback<TestBrowser> {

	/**
	 * Warte auf ein asynchrones Abbrucherreignis, welches durch eine
	 * {@link IFunctionCondition} auf einem {@link WebElement} beschrieben wird.
	 * 
	 * Beispielhafte Verwendung:
	 * 
	 * waitFor(browser, "jsonresponse", 120, 1000, new IFunctionCondition(){
	 * 
	 * @Override public boolean apply(WebElement e) { return
	 *           e.getText().startsWith("Spielfeldname"); } });
	 * 
	 * 
	 * @param browser
	 * 
	 * @param timeOut
	 *            Maximale Dauer für das Warten auf das asynchrone
	 *            Abbrucherreignis (in Sekunden).
	 * @param iFunctionCondition
	 * @param pollingInterval
	 *            Polling-Intervall zur Überprüfung der übergebenen
	 *            {@link IFunctionCondition} (in Millisekunden).
	 */
	protected void asyncWaitFor(TestBrowser browser, final String elementID,
			int timeOut, int pollingInterval,
			final IFunctionCondition iFunctionCondition) {

		FluentWait<WebDriver> wait1 = browser.fluentWait()
				.withTimeout(timeOut, SECONDS)
				.pollingEvery(pollingInterval, MILLISECONDS)
				.ignoring(NoSuchElementException.class);
		
		wait1.until(new Function<WebDriver, WebElement>() {
			@Override
			public WebElement apply(WebDriver driver) {
				WebElement e = driver.findElement(By.id(elementID));
				if (iFunctionCondition.apply(e))
					return e;
				else
					throw new NoSuchElementException(
							"The Element was not found.");
			}
		});
	}
	
	/**
	 * Wartet synchron für die gegebene Anzahl von Sekunden.
	 * 
	 * @param seconds
	 */
	protected void syncWaitFor(int seconds) {
		try {
			Thread.sleep(seconds*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
