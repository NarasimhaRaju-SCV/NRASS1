/**
 * 
 */
package Screens;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;


import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
/**
 * @author Narasimha Raju
 *
 */
public class HomePage extends CommonPage {

	By searchTxtbox = By.cssSelector("input[name='q']");
	By searchBtn= By.cssSelector("input[value='Get quotes']");
	
	public HomePage(RemoteWebDriver driver) {
		super(driver);
		// check page load by verifying presence of a control on the destination page 
		try {
			driver.findElement(searchTxtbox);
		} catch (NoSuchElementException e) {
			Assert.fail("Home page is not loaded\n"+e.toString());
		}
	}

	public HomePage enterStockCode(String stockcode)
	{
		driver.findElement(searchTxtbox).clear();
		driver.findElement(searchTxtbox).sendKeys(stockcode);
		return new HomePage(driver);
	} 
	
	public CompanyStockPage clickSearch()
	{
		driver.findElement(searchBtn).click();
		return new CompanyStockPage(driver);
	} 
}
