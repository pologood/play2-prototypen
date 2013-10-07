package models.yaml;

import static org.fest.assertions.Assertions.assertThat;

import java.util.LinkedHashMap;
import java.util.List;

import models.BaseModelTest;

import org.junit.BeforeClass;
import org.junit.Test;

import play.libs.Yaml;
import play.test.FakeApplication;

/**
 * Basisklasse, welche für Model-Testklassen, die betreffenden YAML Testdaten
 * bereitstellt. Erbt von {@link BaseModelTest}, welche die nötige
 * {@link FakeApplication} bereitstellt. Funktioniert nicht in Eclipse IDE.
 * 
 * @author andre.tschirch
 * 
 */
public class BaseYamlTest extends BaseModelTest {

	/**
	 * YAML Testdaten
	 */
	public static LinkedHashMap<String, List<Object>> yamlData;

	/**
	 * Lädt conf/test-data.yml. Test funktioniert nicht in Eclipse-IDE, aber in
	 * play-console!
	 */
	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void loadYamlDataFile() {
		yamlData = (LinkedHashMap<String, List<Object>>) Yaml
				.load("test-data.yml");
	}

	/**
	 * YAML-Datei korrekt geladen?
	 */
	@Test
	public void yamlDataFileNotEmpty() {
		// is not null
		assertThat(yamlData).isNotNull();
		// is not empty
		assertThat(yamlData).isNotEmpty();
	}

}
