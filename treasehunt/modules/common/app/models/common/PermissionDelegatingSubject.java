/**
 * 
 */
package models.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.ManyToMany;

import play.db.ebean.Model;
import be.objectify.deadbolt.core.models.Permission;
import be.objectify.deadbolt.core.models.Subject;

/**
 * @author Stefan Illgen
 *
 */
@javax.persistence.MappedSuperclass
public abstract class PermissionDelegatingSubject extends Model implements Subject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String ROLES = "roles";
	
	@ManyToMany
	public List<SecurityRole> roles;
	
	@Override
	public List<SecurityRole> getRoles() {
		return roles;
	}

	/**
	 * Delegating.
	 */
	@Override
	public List<? extends Permission> getPermissions() {

		Set<UserPermission> result = new HashSet<UserPermission>();
		
		for(SecurityRole r : roles){
			for(UserPermission p : r.getPermissions()){
				result.add(p);
			}
		}
		
		return new ArrayList<UserPermission>(result);
	}	

}
