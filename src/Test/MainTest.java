/**
 * 
 */
package Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;




import com.perfectomobile.selenium.util.EclipseConnector;

import Screens.HomePage;
import Utilities.Common;
import Utilities.DButil;
import Utilities.PerfectoLabUtils;


/**
 * @author Narasimha Raju
 *
 */
@Test
public class MainTest {
	private RemoteWebDriver driver;
	private DesiredCapabilities capabilities;
	private String reportPath;
	
	 @Parameters({ "host", "user" , "password", "persona", "platformName", "manufacturer", "model", "appURL"})
	   @BeforeMethod 
	   public void beforeClass(String host, String user, String password, String persona, String platformName, String manufacturer, String model, String appURL) throws IOException, InterruptedException{
	         
	        capabilities = new DesiredCapabilities("mobileOS", "", Platform.ANY);
	        capabilities.setCapability("automationName", "Appium");
	        capabilities.setCapability("platformName", platformName);	        
	        capabilities.setCapability("manufacturer", manufacturer);
	        capabilities.setCapability("model", model);
	        capabilities.setCapability("user", user);
	        capabilities.setCapability("password", password);
		    capabilities.setCapability("windTunnelPersona", persona);
        
		    setExecutionIdCapability(capabilities, host);
	        driver = new RemoteWebDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
	        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);	        
	        
			setReportPath();			
			driver.get(appURL);	
			Thread.sleep(4000);
	   }
	
	
	 @Test(dataProvider="nyseInput")
	 public void compareNYSEStocks(String stockcode1, String stockcode2) throws MalformedURLException {
	   
		HashMap<String, String> stockMap1 = new HomePage(driver)
												.enterStockCode(stockcode1)
													.clickSearch()
														.verifyStockPage(stockcode1)
															.getStockDetails();
		
		String companyName1 = stockMap1.get("CompanyName");
		String currentPrice1 = stockMap1.get("CurrentPrice");
		String w52High1 = stockMap1.get("52WeekHigh");
		String w52Low1 = stockMap1.get("52WeekLow");
		String eps1 = stockMap1.get("EPS");
     	
		String consoleOut = "====  "+Common.compareCurrntPriceAnd52Week(companyName1, stockcode1, currentPrice1, w52Low1, w52High1);
		Reporter.log(consoleOut);
		System.out.println(consoleOut);
		
		
		HashMap<String, String> stockMap2 = new HomePage(driver)
												.enterStockCode(stockcode2)
													.clickSearch()
														.verifyStockPage(stockcode2)
															.getStockDetails();
		
		String companyName2 = stockMap2.get("CompanyName");
		String eps2 = stockMap2.get("EPS");
		
		consoleOut = "====  "+Common.compareEPS(companyName1, stockcode1, companyName2, stockcode2, eps1, eps2);
		Reporter.log(consoleOut);
		System.out.println(consoleOut);
     	     	     	
	 }
	 
		@AfterMethod
		   public void afterClass() {
		     try{
		         // Close the browser
		         driver.close();
		             
		         // Download a pdf version of the execution report		         
		         PerfectoLabUtils.downloadReport(driver, "pdf", reportPath+"\\report.pdf");
		         }
		         catch(Exception e){
		             e.printStackTrace();
		         }
		     driver.quit();
		   }
	 
	 
	 
	   private void setReportPath() {
		   reportPath = System.getProperty("user.dir")+"\\reports\\DeviceID-"+getDeviceID()+"_TimeStamp-"+Common.getTimeStamp();
	        new File(reportPath).mkdirs();	
	   }
	   private String getDeviceID()
	   {
		   Map<String, Object> params = new HashMap<>();
		   params.put("property", "deviceId");
		   return (String)driver.executeScript("mobile:handset:info", params);	
	   }
	 
	   @DataProvider(name="nyseInput")
		public Iterator<Object[]> nyseInputDataWorkbook() throws FileNotFoundException, IOException, ClassNotFoundException, SQLException {				
			
			ArrayList<Object[]> da = new ArrayList<Object[]>();
			
			DButil dbObj = new DButil("localhost", "3306", "testdatadb", "narasimha", "qatest");
			List<HashMap<String, Object>> resulSet = dbObj.executeQuery("select * from nysedata where isenabled = 1;");
			
			for(HashMap<String, Object> dataRow: resulSet)			
				da.add(new Object[]{dataRow.get("stockcode1"), dataRow.get("stockcode2")});		
			
			return da.iterator();			
		}
		
	    private static void setExecutionIdCapability(DesiredCapabilities capabilities, String host) throws IOException  {
	        EclipseConnector connector = new EclipseConnector();
	        String eclipseHost = connector.getHost();
	        if ((eclipseHost == null) || (eclipseHost.equalsIgnoreCase(host))) {
	            String executionId = connector.getExecutionId();
	            capabilities.setCapability(EclipseConnector.ECLIPSE_EXECUTION_ID, executionId);
	        }
	    }
}
