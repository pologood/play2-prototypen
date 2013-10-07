package views.admin;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;
import static play.test.Helpers.testServer;

import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;

import views.admin.callbacks.AsnycCallbackComposite;

public class FluentAsyncTest {

	@Test
	public void simpleAuthChrome() {

//		Map<String,String> additionalConfiguration = inMemoryDatabase();
//		FakeApplication fa = fakeApplication();
//		play.api.Configuration iConf = fa.getWrappedApplication().configuration();
//		scala.collection.immutable.Map<String, Object> conf = fa
//				.getWrappedApplication().additionalConfiguration();
		
		System.setProperty("webdriver.chrome.driver",
				"conf/selenium/chromedriver_win32_2.0/chromedriver.exe");
		running(testServer(3333, fakeApplication(inMemoryDatabase())), ChromeDriver.class,
				new AsnycCallbackComposite());
	}

}
