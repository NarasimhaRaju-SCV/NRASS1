/**
 * 
 */
package Screens;

import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;


/**
 * @author Narasimha Raju
 *
 */
public class CompanyStockPage extends CommonPage{

	By companyName = By.xpath("//*[@id='companyheader']/div[1]/h3");
	String currentNYSECode = "//*[@id='companyheader']/div[1]/h3[contains(following-sibling::text(), '$nysecode$')]";
	By currentPrice = By.xpath("//div[@id='price-panel']//span[@class='pr']");
	By fiftytwoWeek = By.xpath("//tr[td[@data-snapfield='range_52week']]/td[@class='val']");
	By eps = By.xpath("//tr[td[@data-snapfield='eps']]/td[@class='val']");
	
	public CompanyStockPage(RemoteWebDriver driver) {
		super(driver);
		// check if the page has loaded by verifying value of a field in the page.
		try {
			driver.findElement(companyName);
		} catch (NoSuchElementException e) {
			Assert.fail("Company stock Page has not loaded \n"+e.toString());
		}
	}
	
   public CompanyStockPage verifyStockPage(String stockcode)
   {
	   currentNYSECode = currentNYSECode.replace("$nysecode$", stockcode);
	   // check if the stock detail page has loaded with given company stock details
	   try {
			driver.findElement(By.xpath(currentNYSECode));
		} catch (NoSuchElementException e) {
			Assert.fail("Stock Detail for the company code '"+stockcode+"' seems to be not loaded\n"+e.toString());
		}
	   
	   return new CompanyStockPage(driver);
   }
	
   private String getfiftytwoWeekHigh()
	{
		return getFiftyTwoWeekHighLow().split("-")[1].trim();
	}
	
	private String getfiftytwoWeekLow()
	{
		return getFiftyTwoWeekHighLow().split("-")[0].trim();
	}
	
	private String getFiftyTwoWeekHighLow()
	{
		return driver.findElement(fiftytwoWeek).getText().trim();
	}
	
	private String getEPS()
	{
		return driver.findElement(eps).getText().trim();
	}
	private String getCompanyName()
	{
		return driver.findElement(companyName).getText().trim();
	}
		
	private String getCurrentPrice()
	{
		return driver.findElement(currentPrice).getText().trim();
	}

	public HashMap<String, String> getStockDetails()
	{
		HashMap<String, String> retMap = new HashMap<String, String>();
		
		retMap.put("CompanyName", getCompanyName());
		retMap.put("CurrentPrice", getCurrentPrice());
		retMap.put("52WeekHigh", getfiftytwoWeekHigh());
		retMap.put("52WeekLow", getfiftytwoWeekLow());
		retMap.put("EPS", getEPS());				
		return retMap;
	}
	
	
}
