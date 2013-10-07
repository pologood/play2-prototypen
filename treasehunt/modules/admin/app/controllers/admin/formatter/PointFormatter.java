/**
 * 
 */
package controllers.admin.formatter;

import java.text.ParseException;
import java.util.Locale;

import models.Point;
import play.data.format.Formatters.SimpleFormatter;

/**
 * @author Stefan Illgen
 *
 */
public class PointFormatter extends SimpleFormatter<Point> {

	@Override
	public Point parse(String arg0, Locale arg1) throws ParseException {
		return Point.findById(arg0);
	}

	@Override
	public String print(Point t, Locale locale) {				
		return Long.toString(t.id);
	}

}
