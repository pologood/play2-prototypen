import java.util.LinkedHashMap;
import java.util.List;

import models.Point;
import play.Application;
import play.GlobalSettings;
import play.libs.Yaml;

import com.avaje.ebean.Ebean;

/**
 * Klasse, die beim Starten der Play-Applikation aufgerufen wird.
 * 
 * @author andre.tschirch
 * 
 */
public class Global extends GlobalSettings {

	@SuppressWarnings("unchecked")
	@Override
	public void onStart(Application app) {

		/*
		 * Speichere YAML-Daten in die DB beim Applikationsstart, falls
		 * Applikation sich nicht im Testmodus befindet. Applikation ist u.a. im
		 * Testmodus, wenn die JUnit-Tests ausgeführt werden. Siehe
		 * FakeApplication in BaseModelTest.
		 */
		if (!app.isTest() && Point.findByAll().isEmpty()) {
			LinkedHashMap<String, List<Object>> map = (LinkedHashMap<String, List<Object>>) Yaml
					.load("initial-data.yml");
			Ebean.save(map.get("points"));
			Ebean.save(map.get("paths"));
			Ebean.save(map.get("playgrounds"));
		}
	}

}
