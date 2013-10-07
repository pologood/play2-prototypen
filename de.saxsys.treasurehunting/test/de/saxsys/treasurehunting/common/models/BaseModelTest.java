package de.saxsys.treasurehunting.common.models;

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
public abstract class BaseModelTest {

	/**
	 * Die {@link FakeApplication}.
	 */
	public static FakeApplication fakeApplication;

	/**
	 * Initialisiert eine {@link FakeApplication}.
	 */
	@BeforeClass
	public static void startFakeApplication() {
		fakeApplication = Helpers.fakeApplication();
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
