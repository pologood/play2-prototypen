/**
 * 
 */
package views.common.callbacks;

import views.callbacks.CompositeCallback;

/**
 * @author stefan.illgen
 *
 */
public class SimpleAuthCallbackComposite extends CompositeCallback {

	public SimpleAuthCallbackComposite() {
		addCallback(new RegisterCallback());
		addCallback(new LoginCallback());
		addCallback(new LogoutCallback());		
	}

}
