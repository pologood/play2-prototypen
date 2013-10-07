/**
 * 
 */
package de.saxsys.treasurehunting.game.services.exceptions;

/**
 * An Exception which is thrown during the users join to the game.
 * 
 * @author stefan.illgen
 * 
 */
public class GameJoinException extends Exception {

	private static final long serialVersionUID = -5284737509400188186L;

	/**
	 * Default Constructor taking a localized message.
	 * 
	 * @param localizedMessage
	 *            The localized message to show.
	 */
	public GameJoinException(String localizedMessage) {
		super(localizedMessage);
	}

}
