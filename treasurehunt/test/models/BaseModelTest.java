package models;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import play.test.FakeApplication;
import play.test.Helpers;

/**
 * Basisklasse, die für die Model-Tests eine {@link FakeApplication} zur
 * Verfügung stellt.
 * 
 * @author andre.tschirch
 * 
 */
public class BaseModelTest {

	public static FakeApplication fakeApplication;

	/**
	 * Initialisiert eine {@link FakeApplication} mit InMemory-DB.
	 */
	@BeforeClass
	public static void startFakeApplication() {

		fakeApplication = Helpers.fakeApplication(Helpers.inMemoryDatabase());
		Helpers.start(fakeApplication);
	}

	/**
	 * Stoppt die {@link FakeApplication}.
	 */
	@AfterClass
	public static void stopFakeApplication() {

		Helpers.stop(fakeApplication);
	}

}
