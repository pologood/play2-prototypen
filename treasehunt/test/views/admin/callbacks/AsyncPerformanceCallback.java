package views.admin.callbacks;

import static org.fest.assertions.Assertions.assertThat;

import org.openqa.selenium.WebElement;

import play.Logger;
import play.mvc.Result;
import play.test.FakeRequest;
import play.test.Helpers;
import play.test.TestBrowser;
import views.callbacks.Callback;
import views.callbacks.IFunctionCondition;

public class AsyncPerformanceCallback extends Callback {

	static int TEST_RUNS = 3;
	
	@Override
	public void invoke(TestBrowser browser) throws Throwable {

		// Play Forms
		long fResult = 0;
		for (int i = 0; i < TEST_RUNS; i++)
			fResult += invokePlayFormsTest(browser);
		String formsResult = "Play Forms Performanz: " + (fResult / TEST_RUNS) + " ms";

		// Ajax
		fResult = 0;
		for (int i = 0; i < TEST_RUNS; i++)
			fResult += invokeAjaxTest(browser);
		String ajaxResult = "Ajax Performanz: " + (fResult / TEST_RUNS) + " ms";

		// Websocket
		fResult = 0;
		for (int i = 0; i < TEST_RUNS; i++)
			fResult += invokeWebsocketTest(browser);
		String websocketResult = "Websocket Performanz: " + (fResult / TEST_RUNS)
				+ " ms";

		// Comet
		fResult = 0;
		for (int i = 0; i < TEST_RUNS; i++)
			fResult += invokeCometTest(browser);
		String cometResult = "Comet Performanz: " + (fResult / TEST_RUNS) + " ms";

		// Report 2 Log-File
		Logger.info("Antwortzeit: \n" + formsResult + "\n" + ajaxResult + "\n"
				+ websocketResult + "\n" + cometResult + "\n");

		// T(s)
		// String tsFormsResult = invokePlayFormsServerTest(browser);
		//
		// Logger.info("T(s):\n" + tsFormsResult + "\n");
	}

	/*
	 * Problem: ...
	 * 
	 * @param browser
	 */
	private long invokePlayFormsTest(TestBrowser browser) {

		selectPlaygroundFoo(browser);

		browser.goTo("http://localhost:3333/admin/playgrounds/pathNpoints");
		asyncWaitFor(browser, "jsonresponse", 120, 1000,
				new IFunctionCondition() {
					@Override
					public boolean apply(WebElement e) {
						return e.getText().startsWith("Spielfeldname");
					}
				});

		browser.click("#pointSelectX_From_22");
		browser.click("#pointSelectX_From_22 > option[value='10']");

		// Startzeit
		long zstbefore = System.currentTimeMillis();
		browser.click("#savePath_13");

		asyncWaitFor(browser, "jsonresponse", 120, 10,
				new IFunctionCondition() {
					@Override
					public boolean apply(WebElement e) {
						return e.getText().startsWith("Spielfeldname");
					}
				});

		// Endzeit
		long zstAfter = System.currentTimeMillis();
		long result = zstAfter - zstbefore;

		assertThat(result > 0);

		return result;
	}

	/*
	 * Problem: ...
	 * 
	 * @param browser
	 */
	private long invokeAjaxTest(TestBrowser browser) {

		selectPlaygroundFoo(browser);

		browser.goTo("http://localhost:3333/admin/playgrounds/points");
		browser.click("#pointSelectX_22");
		browser.click("#pointSelectX_22 > option[value='10']");

		// Startzeit
		long zstbefore = System.currentTimeMillis();
		browser.click("#submitSavePoint_22");

		asyncWaitFor(browser, "jsonresponse", 120, 10,
				new IFunctionCondition() {
					@Override
					public boolean apply(WebElement e) {
						return e.getText().startsWith("Spielfeldname");
					}
				});

		// Endzeit
		long zstAfter = System.currentTimeMillis();
		long result = zstAfter - zstbefore;

		assertThat(result > 0);

		return result;
	}

	private long invokeWebsocketTest(TestBrowser browser) {

		selectPlaygroundFoo(browser);

		// Websockets Test: Wenn im Element mit der ID 'submitSavePointsWSInfo'
		// der Text 'Websockets Success' auftaucht, dann ist das ein Erfolg.
		browser.goTo("http://localhost:3333/admin/playgrounds/pointsWS");
		browser.click("#pointSelectX_22");
		browser.click("#pointSelectX_22_10");
		syncWaitFor(5);

		// Startzeit
		long zstbefore = System.currentTimeMillis();
		browser.click("#submitSavePointsWS");

		// Warte solange auf das WebElement mit der ID "websocketInfo" bis es
		// den Inhalt "Websocket Success" ist.
		asyncWaitFor(browser, "websocketInfo", 120, 10,
				new IFunctionCondition() {
					@Override
					public boolean apply(WebElement e) {
						return e.getText().compareTo("Websocket Success") == 0;
					}
				});

		// Endzeit
		long zstAfter = System.currentTimeMillis();
		long result = (zstAfter - zstbefore);
		assertThat(result > 0);

		return result;
	}

	private long invokeCometTest(TestBrowser browser) {

		// selectPlaygroundFoo(browser);

		return 0;
	}

	/*
	 * Spielfeldadministration betreten.
	 */
	private void selectPlaygroundFoo(TestBrowser browser) {
		browser.goTo("http://localhost:3333/admin/playgrounds");
		browser.click("#pgSelectorDropDown");
		browser.click("#fooPgSelector");
		asyncWaitFor(browser, "jsonresponse", 120, 1000,
				new IFunctionCondition() {
					@Override
					public boolean apply(WebElement e) {
						return e.getText().startsWith("Spielfeldname");
					}
				});
	}

	/**
	 * Testet ausschließlich die Bearbeitungszeit des Servers.
	 * 
	 * TODO: Für einen einfachen GET-Request "/" funktioniert es. Wird jedoch
	 * ein komplexer GET-Request wie
	 * 
	 * "/admin/playgrounds/pathNpoints/savePath?id=13&fromPoint.x=10&fromPoint.y=130&fromPoint.id=22&toPoint.x=180&toPoint.y=180&toPoint.id=24"
	 * 
	 * abgesetzt, wird als Ergebnis eine 303 zurückgeliefert. Das bedeutet, dass
	 * der Request fehlschlägt.
	 * 
	 * Nach Untersuchung des HTTP-Requests gibt es folgende Abweichungen:
	 * 
	 * * session * body * cookies
	 * 
	 * Aus diesem Grund, wird der Servertest derzeitig noch nicht gefahren.
	 * 
	 * @param browser
	 * @return
	 */
	private String invokePlayFormsServerTest(TestBrowser browser) {

		selectPlaygroundFoo(browser);

		// FakeRequest erstellen
		FakeRequest fr = new play.test.FakeRequest(
				Helpers.GET,
				"/admin/playgrounds/pathNpoints/savePath?id=13&fromPoint.x=10&fromPoint.y=130&fromPoint.id=22&toPoint.x=180&toPoint.y=180&toPoint.id=24");
		// fr.withSession(name, value);
		// fr.withCookies(new Coo)

		// GEHT
		// FakeRequest fr = new play.test.FakeRequest(
		// Helpers.GET,
		// "/");

		// Startzeit
		long zstbefore = System.currentTimeMillis();
		Result result1 = Helpers.route(fr);
		// Endzeit
		long zstAfter = System.currentTimeMillis();
		long lResult = zstAfter - zstbefore;
		String strResult = "Play Forms Performanz: " + (lResult) + " ms";

		// ERROR: 303
		assertThat(Helpers.status(result1)).isEqualTo(Helpers.OK);

		return strResult;
	}
}
