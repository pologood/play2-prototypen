package de.saxsys.treasurehunting;

import java.util.LinkedHashMap;
import java.util.List;

import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.libs.Yaml;

import com.avaje.ebean.Ebean;

import de.saxsys.treasurehunting.common.services.UserService;
import de.saxsys.treasurehunting.game.services.PlaygroundService;

/**
 * This class configures the following global settings for this project.
 * 
 * <ul>
 * <li>loading initial or test data depending on the application mode test or
 * non-test (dev, prod)</li>
 * </ul>
 * 
 * @author stefan.illgen
 * 
 */
public class Global extends GlobalSettings {

	@Override
	public void onStart(Application app) {
		loadModelData(app);
	}

	@SuppressWarnings("unchecked")
	private void loadModelData(Application app) {

		LinkedHashMap<String, List<Object>> map = null;

		/*
		 * If the application is not in test mode (DEV (play run) or PROD (play
		 * start|stage)) and test data do not exist load model by
		 * conf/initial-data.yml.
		 */
		if (PlaygroundService.getAllPlaygroundNames().size() == 0) {

			if (!app.isTest()) {
				Logger.info("Loading YAML test data from conf/initial-data.yml.");
				map = (LinkedHashMap<String, List<Object>>) Yaml
						.load("initial-data.yml");
				Ebean.save(map.get("actions"));
				Ebean.save(map.get("points"));
				Ebean.save(map.get("paths"));
				Ebean.save(map.get("playgrounds"));
			}

			/*
			 * If the application is in test mode and test data does not exist
			 * load model by conf/test-data.yml.
			 */
			if (app.isTest() && UserService.findUser("stefan") == null) {
				Logger.info("Loading YAML test data from conf/test-data.yml.");
				map = (LinkedHashMap<String, List<Object>>) Yaml
						.load("test-data.yml");
				Ebean.save(map.get("actions"));
				Ebean.save(map.get("points"));
				Ebean.save(map.get("paths"));
				Ebean.save(map.get("playgrounds"));
				Ebean.save(map.get("users"));
				Ebean.save(map.get("games"));
			}
		}
	}
};
