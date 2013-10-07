package models;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import play.db.DB;

/**
 * Testet die DB Verbindung.
 * 
 * @author andre.tschirch
 * 
 */
public class DatasourceTest extends BaseModelTest {

	/**
	 * Kann eine Verbindung zur DB aufgebaut werden?
	 */
	@Test
	public void getConnection() {

		assertThat(DB.getConnection()).isNotNull();
	}

	/**
	 * Ist die Datasource vorhanden?
	 */
	@Test
	public void getDatasource() {

		assertThat(DB.getDataSource()).isNotNull();
	}
}
