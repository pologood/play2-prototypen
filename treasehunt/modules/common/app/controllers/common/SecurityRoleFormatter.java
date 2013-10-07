/**
 * 
 */
package controllers.common;

import java.text.ParseException;
import java.util.Locale;

import models.common.SecurityRole;

import play.data.format.Formatters.SimpleFormatter;

/**
 * @author Stefan Illgen
 *
 */
public class SecurityRoleFormatter extends SimpleFormatter<SecurityRole> {

	@Override
	public SecurityRole parse(String arg0, Locale arg1) throws ParseException {
		return SecurityRole.findByRoleName(arg0);
	}

	@Override
	public String print(SecurityRole t, Locale locale) {				
		return t.getName();
	}

}
