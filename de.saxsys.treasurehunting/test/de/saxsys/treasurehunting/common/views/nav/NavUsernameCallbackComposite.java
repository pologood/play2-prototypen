/**
 * 
 */
package de.saxsys.treasurehunting.common.views.nav;

import de.saxsys.treasurehunting.common.views.LoginSuccessCallback;
import de.saxsys.treasurehunting.views.callbacks.CompositeCallback;

/**
 * Composite Callback for testing the correct presentation of the users name.
 * 
 * @author stefan.illgen
 */
public class NavUsernameCallbackComposite extends CompositeCallback {

	/**
	 * Composite callback construction.
	 */
	public NavUsernameCallbackComposite() {
		addCallback(new LoginSuccessCallback());
		addCallback(new NavUsernameCallback());
	}
}
