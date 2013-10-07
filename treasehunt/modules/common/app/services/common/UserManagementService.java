/**
 * 
 */
package services.common;

import java.util.List;

import play.api.i18n.Lang;
import play.mvc.Http.Session;

/**
 * @author Stefan Illgen
 * 
 */
public interface UserManagementService {

	/**
	 * Gib alle unterstützten Sprachen zurück, die in der application.conf
	 * definiert wurden.
	 * 
	 * @return
	 */
	public List<Lang> getConfiguredLanguages();

	/**
	 * Gib den aktuell eingeloggten Nutzer zurück oder <code>null</code> wenn
	 * kein Nutzer eingeloggt.
	 * 
	 * @return
	 */
	public models.common.User getLoggedUser(Session session);
	
	/**
	 * Gib die aktuell gewählte Sprache zurück.
	 * 
	 * @param session
	 * @return
	 */
	public Lang getSessionLanguage(Session session);

}
