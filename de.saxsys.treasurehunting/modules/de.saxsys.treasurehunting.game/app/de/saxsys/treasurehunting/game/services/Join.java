/**
 * 
 */
package de.saxsys.treasurehunting.game.services;

import org.codehaus.jackson.JsonNode;

import play.mvc.WebSocket;
import de.saxsys.treasurehunting.common.models.game.Counter;

/**
 * Represents a {@link Join} to the game hall.
 * 
 * @author stefan.illgen
 * 
 */
public class Join {

	/**
	 * The game's ID.
	 */
	public final Long gameID;

	/**
	 * The counter's ID.
	 */
	public final Long counterID;

	/**
	 * The web socket's out channel.
	 */
	public final WebSocket.Out<JsonNode> out;

	/**
	 * Constructor creating a {@link Join} message used to join the game.
	 * 
	 * @param gameID
	 *            The {@link Counter}s ID.
	 * @param counterID
	 *            The {@link Counter}s ID.
	 * @param out
	 *            The web socket out channel.
	 */
	public Join(Long gameID, Long counterID, WebSocket.Out<JsonNode> out) {
		this.gameID = gameID;
		this.counterID = counterID;
		this.out = out;
	}
}
