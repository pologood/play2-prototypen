/**
 * 
 */
package de.saxsys.treasurehunting.common.models.actions;

import java.util.List;

/**
 * This class represents an action response, which is sent from the server to
 * the client.
 * 
 * @author stefan.illgen
 * 
 */
public class ActionResponse {
	
	/**
	 * The initializing action given by its type.
	 */
	public int initializer;

	/**
	 * The additional data returned to the client.
	 */
	public Object data;

	/**
	 * A {@link List} of following {@link Action}s to be applied by the client.
	 */
	public List<Action> followers;

}
