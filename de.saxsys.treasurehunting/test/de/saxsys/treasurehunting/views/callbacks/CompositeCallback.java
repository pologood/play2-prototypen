/**
 * 
 */
package de.saxsys.treasurehunting.views.callbacks;

import java.util.ArrayList;
import java.util.List;

import play.test.TestBrowser;

/**
 * Kompositer {@link Callback} mit einer beliebigen Anzahl von Kindern vom Typ
 * {@link Callback}.
 * 
 * @author stefan.illgen
 * 
 */
public class CompositeCallback extends Callback {

	private List<Callback> children = new ArrayList<Callback>();

	/**
	 * Fügt einen {@link Callback} als Kind hinzu.
	 * 
	 * @param callback Der hinzuzufügende {@link Callback}.
	 * @return Gibt <code>true</code> zurück, wenn der Callback hinzugefügt werden konnte.
	 */
	public boolean addCallback(Callback callback) {
		return children.add(callback);
	}

	/**
	 * Gibt einen bestimmten {@link Callback} per Index zurück.
	 * 
	 * @param index Der Index des zurückzugebenden {@link Callback}s.
	 * @return Der gesuchte {@link Callback}.
	 */
	public Callback getCallback(int index) {
		return children.get(index);
	}

	/**
	 * Ruft die Methode {@link Callback#invoke(TestBrowser)} für alle Kinder vom
	 * Typ {@link Callback} unter Verwendung desselben {@link TestBrowser}s auf.
	 * 
	 * @param a Der {@link TestBrowser}.
	 * @throws Throwable Das geworfene Throwable.
	 * 
	 * @see play.libs.F.Callback
	 */
	@Override
	public void invoke(TestBrowser a) throws Throwable {
		for (Callback callback : children)
			callback.invoke(a);
	}

}
