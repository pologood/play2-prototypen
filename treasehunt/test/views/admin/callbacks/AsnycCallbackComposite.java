/**
 * 
 */
package views.admin.callbacks;

import views.callbacks.CompositeCallback;
import views.common.callbacks.LoginCallback;
import views.common.callbacks.LogoutCallback;
import views.common.callbacks.RegisterCallback;

/**
 * @author stefan.illgen
 *
 */
public class AsnycCallbackComposite extends CompositeCallback {
	
	public AsnycCallbackComposite() {
		addCallback(new RegisterCallback());
		addCallback(new LoginCallback());
		addCallback(new AsyncPerformanceCallback());
		addCallback(new LogoutCallback());
	}

}
