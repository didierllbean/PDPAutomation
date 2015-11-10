package Util;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Set;

import javax.swing.JOptionPane;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.annotations.BeforeTest;

public class Settings {
	
	public static WebDriver driver;
	public static EventFiringWebDriver eventFiringWebDriver;
	public static String mainWindowHandle;
	public static String page;
	public static Set<String> productPages;
	public static boolean production; //false if it's on stage
	public static String llbssCookieValue; //A for old PDP, B for new PDP, BOTH to run in both types of pdp
	
	@BeforeTest
	public void testSetUp(){
		
		//Prints all the messages and errors to output.txt
		/*try {
			System.setOut(new PrintStream(new FileOutputStream(System.getProperty("user.dir")+"\\extra-files\\output.txt")));
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Couldn't find output.txt");
	    	System.out.println(e);
	    	System.exit(1);
		}*/
		
		//Sets up the Chrome driver
		String url = System.getProperty("user.dir")+"\\extra-files\\chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", url);
		System.out.println("Driver found in: "+System.getProperty("webdriver.chrome.driver"));
		driver = new ChromeDriver(DesiredCapabilities.chrome());
		eventFiringWebDriver = new EventFiringWebDriver (driver);
		//Deletes all cookies from the browser
		driver.manage().deleteAllCookies();
		
	}
	

}
