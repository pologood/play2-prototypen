/**
 * 
 */
package de.saxsys.treasurehunting.views.callbacks;

import org.openqa.selenium.WebElement;

/**
 * Kondition zur Entscheidung auf Erreichen des gewünschten Zustands eines
 * {@link WebElement}s.
 * 
 * @author stefan.illgen
 * 
 */
public interface IFunctionCondition {

	/**
	 * Wendet die Bedingung auf das {@link WebElement} an.
	 * 
	 * @param e Das {@link WebElement}.
	 * @return Gibt <code>true</code> zurück, wenn die Bedingung wahr ist.
	 */
	boolean apply(WebElement e);

}
