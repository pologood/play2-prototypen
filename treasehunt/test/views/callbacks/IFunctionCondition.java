/**
 * 
 */
package views.callbacks;

import org.openqa.selenium.WebElement;

/**
 * Kondition zur Entscheidung auf Erreichen des gewünschten Zustands eines
 * {@link WebElement}s, der zum Abbruch des Wartens auf ein asynchronen 
 * 
 * @author stefan.illgen
 * 
 */
public interface IFunctionCondition {

	/**
	 * Wendet die Bedingung auf das {@link WebElement} an.
	 * 
	 * @param e
	 * @return
	 */
	public boolean apply(WebElement e);

}
