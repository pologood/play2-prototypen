/**
 * 
 */
package de.saxsys.treasurehunting.game.controllers;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import play.mvc.Result;
import play.test.Helpers;

/**
 * This test class tests the redirect to the start page of the application if
 * the user is'n authorized.
 * 
 * @author stefan.illgen
 * 
 */
public class GameControllerAuthTest {

	/**
	 * This operation runs the authorization test.
	 */
	@Test
	public void index() {
		Helpers.running(Helpers.fakeApplication(), new Runnable() {
			@Override
			public void run() {
				Result result = Helpers.callAction(routes.ref.GameController
						.index());
				assertThat(Helpers.redirectLocation(result)).isEqualTo("/");
			}
		});
	}

	/**
	 * This operation runs the authorization test.
	 */
	@Test
	public void singelplayer() {
		Helpers.running(Helpers.fakeApplication(), new Runnable() {
			@Override
			public void run() {
				Result result = Helpers.callAction(routes.ref.GameController
						.singleplayer(1));
				assertThat(Helpers.redirectLocation(result)).isEqualTo("/");
			}
		});
	}

	/**
	 * This operation runs the authorization test.
	 */
	@Test
	public void startSPGame() {
		Helpers.running(Helpers.fakeApplication(), new Runnable() {
			@Override
			public void run() {
				Result result = Helpers.callAction(routes.ref.GameController
						.startSPGame());
				assertThat(Helpers.redirectLocation(result)).isEqualTo("/");
			}
		});
	}

	/**
	 * This operation runs the authorization test.
	 */
	@Test
	public void visitorarena() {
		Helpers.running(Helpers.fakeApplication(), new Runnable() {
			@Override
			public void run() {
				Result result = Helpers.callAction(routes.ref.GameController
						.visitorarena());
				assertThat(Helpers.redirectLocation(result)).isEqualTo("/");
			}
		});
	}

	/**
	 * This operation runs the authorization test.
	 */
	@Test
	public void startVAGame() {
		Helpers.running(Helpers.fakeApplication(), new Runnable() {
			@Override
			public void run() {
				Result result = Helpers.callAction(routes.ref.GameController
						.startVAGame());
				assertThat(Helpers.redirectLocation(result)).isEqualTo("/");
			}
		});
	}
}
