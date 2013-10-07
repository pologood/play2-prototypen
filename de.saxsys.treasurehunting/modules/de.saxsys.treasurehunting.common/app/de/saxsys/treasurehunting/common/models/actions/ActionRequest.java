/**
 * 
 */
package de.saxsys.treasurehunting.common.models.actions;

/**
 * This class represents an action request, which is sent from the client to the
 * server.
 * 
 * @author stefan.illgen
 * 
 */
public class ActionRequest {

	/**
	 * The initializing action given by its type. This attribute identifies the
	 * type of {@link Action} performed by the client.
	 */
	public int initializer;

	/**
	 * This attribute represent the data, sent from the client to the server,
	 * which is correlated to the initializer.
	 */
	public Object[] data;

}
