/**
 * 
 */
package security.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import play.Logger;
import play.mvc.Http;
import play.mvc.Http.Context;

import be.objectify.deadbolt.core.DeadboltAnalyzer;
import be.objectify.deadbolt.core.models.Permission;
import be.objectify.deadbolt.core.models.Subject;
import be.objectify.deadbolt.java.AbstractDynamicResourceHandler;
import be.objectify.deadbolt.java.DeadboltHandler;
import be.objectify.deadbolt.java.DynamicResourceHandler;

/**
 * @author Stefan Illgen
 *
 */
public class TreaseDynamicResourceHandler implements DynamicResourceHandler {

	private static final Map<String, DynamicResourceHandler> HANDLERS = new HashMap<String, DynamicResourceHandler>();

	static
    {
        HANDLERS.put("pureLuck",
                     new AbstractDynamicResourceHandler()
                     {
                         public boolean isAllowed(String name, 
                                                  String meta, 
                                                  DeadboltHandler deadboltHandler,
                                                  Http.Context context)
                         {
                             return System.currentTimeMillis() % 2 == 0;
                         }
                     });
        HANDLERS.put("viewProfile",
                     new AbstractDynamicResourceHandler()
                     {
                         public boolean isAllowed(String name,
                                                  String meta,
                                                  DeadboltHandler deadboltHandler,
                                                  Http.Context context)
                         {
                             Subject subject = deadboltHandler.getSubject(context);
                             boolean allowed;
                             if (DeadboltAnalyzer.hasRole(subject, "admin"))
                             {
                                 allowed = true;
                             }
                             else
                             {
                                 // a call to view profile is probably a get request, so
                                 // the query string is used to provide info
                                 Map<String, String[]> queryStrings = context.request().queryString();
                                 String[] requestedNames = queryStrings.get("userName");
                                 allowed = requestedNames != null
                                           && requestedNames.length == 1
                                           && requestedNames[0].equals(subject.getIdentifier());
                             }

                             return allowed;
                         }
                     });
    }
	
	@Override
	public boolean isAllowed(String name, String meta,
			DeadboltHandler deadboltHandler, Context ctx) {
		
		DynamicResourceHandler handler = HANDLERS.get(name);
		boolean result = false;
        if (handler == null)
        {
            Logger.error("No handler available for " + name);
        }
        else
        {
            result = handler.isAllowed(name,
                                       meta,
                                       deadboltHandler,
                                       ctx);
        }
        return result;
	}

	@Override
	public boolean checkPermission(String permissionValue,
			DeadboltHandler deadboltHandler, Context ctx) {
		
		boolean permissionOk = false;
        Subject subject = deadboltHandler.getSubject(ctx);
        
        if (subject != null)
        {
            List<? extends Permission> permissions = subject.getPermissions();
            for (Iterator<? extends Permission> iterator = permissions.iterator(); !permissionOk && iterator.hasNext(); )
            {
                Permission permission = iterator.next();
                permissionOk = permission.getValue().contains(permissionValue);
            }
        }
        
        return permissionOk;
	}

}
