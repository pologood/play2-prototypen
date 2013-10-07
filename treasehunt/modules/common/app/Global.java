import models.common.SecurityRole;
import models.common.UserPermission;
import play.Application;
import play.GlobalSettings;
import play.mvc.Call;
import services.common.ApplicationService;
import services.common.ApplicationServiceImpl;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.PlayAuthenticate.Resolver;
import com.feth.play.module.pa.exceptions.AccessDeniedException;
import com.feth.play.module.pa.exceptions.AuthException;

import controllers.common.routes;

public class Global extends GlobalSettings {

	public void onStart(Application app) {
		
		// Treasehunt
		ApplicationService applicationService = new ApplicationServiceImpl();
		applicationService.deleteTestData();
		applicationService.createInitialTestData();
		System.out.println("Testdata has been inserted!!!");
		
		// Play Authenticate
		PlayAuthenticate.setResolver(new Resolver() {

			@Override
			public Call login() {
				// Your login page
				return routes.Application.login();
			}

			@Override
			public Call afterAuth() {
				// The user will be redirected to this page after authentication
				// if no original URL was saved
				return routes.Application.index();
			}

			@Override
			public Call afterLogout() {
				return routes.Application.index();
			}

			@Override
			public Call auth(final String provider) {
				// You can provide your own authentication implementation,
				// however the default should be sufficient for most cases
				return com.feth.play.module.pa.controllers.routes.Authenticate
						.authenticate(provider);
			}

			@Override
			public Call askMerge() {
				return routes.Account.askMerge();
			}

			@Override
			public Call askLink() {
				return routes.Account.askLink();
			}

			@Override
			public Call onException(final AuthException e) {
				if (e instanceof AccessDeniedException) {
					return controllers.common.routes.Signup
							.oAuthDenied(((AccessDeniedException) e)
									.getProviderKey());
				}

				// more custom problem handling here...
				return super.onException(e);
			}
		});

		initialData();
	}
	
	/**
	 * Enter security roles here.
	 */
	private void initialData() {
		if (SecurityRole.find.findRowCount() == 0) {
			
			// PLAYER
			final SecurityRole rolePlayer = new SecurityRole();
			rolePlayer.roleName = security.common.Roles.ROLE_PLAYER;
			
			final UserPermission ps = new UserPermission();
			ps.value = "playgrounds.show";
			ps.save();
			
			rolePlayer.permissions.add(ps);
			rolePlayer.save();
			
			// ADMIN
			final SecurityRole roleAdmin = new SecurityRole();
			roleAdmin.roleName = security.common.Roles.ROLE_ADMIN;
			
			final UserPermission pw = new UserPermission();
			pw.value = "player.write";
			pw.save();
			
			final UserPermission pr = new UserPermission();
			pr.value = "player.read";
			pr.save();
			
			// ebean save cascade
			roleAdmin.children.add(rolePlayer);			
			roleAdmin.permissions.add(pw);
			roleAdmin.permissions.add(pr);
			roleAdmin.save();		
			
//			for (final String roleName : Arrays
//					.asList(
////							controllers.common.Application.USER_ROLE,
//							security.common.Roles.ROLE_PLAYER,
//							security.common.Roles.ROLE_ADMIN
//							)) {
//				final SecurityRole role = new SecurityRole();
//				role.roleName = roleName;
//				
//				
//				
//				role.save();
//			}
		}
	}
}