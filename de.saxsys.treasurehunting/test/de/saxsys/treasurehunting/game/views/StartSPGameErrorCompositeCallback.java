/**
 * 
 */
package de.saxsys.treasurehunting.game.views;

import de.saxsys.treasurehunting.common.views.LoginSuccessCallback;
import de.saxsys.treasurehunting.views.callbacks.CompositeCallback;

/**
 * @author stefan.illgen
 */
public class StartSPGameErrorCompositeCallback extends CompositeCallback {

	public StartSPGameErrorCompositeCallback() {
		addCallback(new LoginSuccessCallback());
		addCallback(new StartSPGameErrorCallback());
	}

}
