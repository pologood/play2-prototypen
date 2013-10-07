package models.yaml;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

/**
 * Validiert die YAML-Testdaten Einträge.
 * 
 * @author andre.tschirch
 * 
 */
public class YamlTest extends BaseYamlTest {

	/**
	 * Lädt conf/test-data.yml und prüft den Inhalt. Test funktioniert nicht in
	 * Eclipse-IDE, aber in play-console!
	 */
	@Test
	public void existsYamlEntries() {

		// exists an entry
		assertThat(yamlData.containsKey("playgrounds")).isTrue();
		// exists playground 'foo'
		assertThat(yamlData.get("playgrounds")).isNotEmpty();
		// TODO some more tests
	}

}
