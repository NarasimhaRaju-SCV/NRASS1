/**
 * 
 */
package Screens;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * @author Narasimha Raju
 *
 */
public class CommonPage {

	protected final RemoteWebDriver driver;

	//constructor returns the driver instance
	public CommonPage(RemoteWebDriver driver) {
		this.driver = driver;
	}

}
