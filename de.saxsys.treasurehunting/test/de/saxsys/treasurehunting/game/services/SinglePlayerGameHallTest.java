/**
 * 
 */
package de.saxsys.treasurehunting.game.services;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.avaje.ebean.Ebean;

import de.saxsys.treasurehunting.common.models.BaseModelTest;
import de.saxsys.treasurehunting.common.models.game.Game;
import de.saxsys.treasurehunting.game.services.exceptions.GameCreationException;
import de.saxsys.treasurehunting.game.services.singleplayer.SinglePlayerGameHall;

/**
 * Tests the following behaviour of {@link SinglePlayerGameHall}.
 * 
 * <ul>
 * 	<li>error prone and successful creation of a {@link Game}</li>
 * </ul>
 * 
 * @author stefan.illgen
 * 
 */
public class SinglePlayerGameHallTest extends BaseModelTest {

	/**
	 * Successful creation test.
	 */
	@Test
	public void createGameSuccess() {

		try {
			long gameID = SinglePlayerGameHall.createGame("stefan", "Game One",
					0xff0000, "Big Playground");
			assertThat(Ebean.find(Game.class, gameID)).isNotNull();
		} catch (GameCreationException e) {
			// program logic must no reach this point
		}
	}

	/**
	 * Tests exception handling for an error prone user name.
	 */
	@Test
	public void createGameUsernameError() {

		String expectedMessage = "Parameter username must not be null and must "
				+ "be a trimmed non empty String value.";

		try {
			SinglePlayerGameHall.createGame(null, "Game One", 0xff0000,
					"Big Playground");
		} catch (GameCreationException e) {
			assertThat(e.getMessage()).isEqualTo(expectedMessage);
		}

		try {
			SinglePlayerGameHall.createGame("", "Game One", 0xff0000,
					"Big Playground");
		} catch (GameCreationException e) {
			assertThat(e.getMessage()).isEqualTo(expectedMessage);
		}

		try {
			SinglePlayerGameHall.createGame("  ", "Game One", 0xff0000,
					"Big Playground");
		} catch (GameCreationException e) {
			assertThat(e.getMessage()).isEqualTo(expectedMessage);
		}
	}

	/**
	 * Tests exception handling for an error prone user name.
	 */
	@Test
	public void createGameGamenameError() {

		String expectedMessage = "Parameter gamename must not be null and must "
				+ "be a trimmed non empty String value.";

		try {
			SinglePlayerGameHall.createGame("stefan", null, 0xff0000,
					"Big Playground");
		} catch (GameCreationException e) {
			assertThat(e.getMessage()).isEqualTo(expectedMessage);
		}

		try {
			SinglePlayerGameHall.createGame("stefan", "", 0xff0000,
					"Big Playground");
		} catch (GameCreationException e) {
			assertThat(e.getMessage()).isEqualTo(expectedMessage);
		}

		try {
			SinglePlayerGameHall.createGame("stefan", "  ", 0xff0000,
					"Big Playground");
		} catch (GameCreationException e) {
			assertThat(e.getMessage()).isEqualTo(expectedMessage);
		}
	}

	/**
	 * Tests exception handling for an error prone user name.
	 */
	@Test
	public void createGameCountercolorError() {

		String expectedMessage = "Integer value for countercolor must be in the "
				+ "range from 0 to 0xffffff.";

		try {
			SinglePlayerGameHall.createGame("stefan", "Game One", -1,
					"Big Playground");
		} catch (GameCreationException e) {
			assertThat(e.getMessage()).isEqualTo(expectedMessage);
		}

		try {
			SinglePlayerGameHall.createGame("stefan", "Game One", 0x1ffffff,
					"Big Playground");
		} catch (GameCreationException e) {
			assertThat(e.getMessage()).isEqualTo(expectedMessage);
		}

	}

	/**
	 * Tests exception handling for an error prone user name.
	 */
	@Test
	public void createGamePlaygroundnameError() {

		String expectedMessage = "Parameter playgroundname must not be null "
				+ "and must be a trimmed non empty String value.";

		try {
			SinglePlayerGameHall.createGame("stefan", "Game One", 0xff0000,
					null);
		} catch (GameCreationException e) {
			assertThat(e.getMessage()).isEqualTo(expectedMessage);
		}

		try {
			SinglePlayerGameHall.createGame("stefan", "Game One", 0xff0000,
					"");
		} catch (GameCreationException e) {
			assertThat(e.getMessage()).isEqualTo(expectedMessage);
		}

		try {
			SinglePlayerGameHall.createGame("stefan", "Game One", 0xff0000,
					"  ");
		} catch (GameCreationException e) {
			assertThat(e.getMessage()).isEqualTo(expectedMessage);
		}

	}

}
