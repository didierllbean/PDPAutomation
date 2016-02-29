package defaultPackage;

import java.io.IOException;

import java.util.Set;

import org.testng.Reporter;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;

/* Class where the tests are executed */
public class TestingProductPage extends Util.Settings {
	
	@Test
	public void test() throws IOException, FileNotFoundException{
		
		//Sets the url to production or stage accordingly
		String url = "http://"+ (production?"www":"ecwebs01") + ".llbean.com/llb/shop/";
		
		//Gets the HP to set the cookies
		driver.get(url+0);
		
		//Checks if it needs to process both PDP
		//boolean bothPDP = processBothPDP();
		
		//Creates the PDP to be used with either old, new or both
		//PDP pdp;
		//Creates a second set of product pages in case of have to do it for both pdp 
		//Set<String> productPages2 = new LinkedHashSet<String>();
		/*
		if(bothPDP){
			//Fills the second set of data
			productPages2.addAll(productPages);
			//Starts with the old PDP
			pdp = new OldPDP();
			setLLBSSCookie("A");
			
		}else{
			//Checks if the cookie is set to evaluate new or old PDP, and sets the cookie
		pdp = (llbssCookieValue.equals("A"))? new OldPDP() : new NewPDP();
			setLLBSSCookie(llbssCookieValue);
		}
		*/
		Reporter.log("<br><b>Processing " + productPages.size() +" pages from: " + url +" </b><br>");
		
		process(productPages,url);
		/*
		if(bothPDP){
			//After doing oldPDP now it can start with newPDP
			pdp = new NewPDP();
			driver.get(url+0);
			setLLBSSCookie("B");
			Reporter.log("<br><b>Processing " + productPages.size() +" pages from: " + url + " with " + pdp.getPDPType() +" </b><br>");
			process(pdp,productPages2,url);
		}

		Reporter.log("<br>END OF AUTOMATION");*/
	}

	private void process(Set<String> pages, String url){
		
		for (String pageNumber : pages){
			
			//Gets the web page
			driver.get(url + pageNumber);
			
			//In case a pop-in interrupter appears, it gets ignored
			mainWindowHandle = driver.getWindowHandles().iterator()
					.next();
			
			Reporter.log("<br>********* Processing page: <a href= \" "+url + pageNumber +" \" target=\"_blank\" >" + pageNumber
					+ "</a> *********<br>");
			
			//Validates the page
			if (isPageAvailable() == true) {
				
				NewPDP newpdp = new NewPDP();
				newpdp.inStock();
				newpdp.isProductAvailable();
				newpdp.validateSizeChart();
				newpdp.validateBreadcrum();
				newpdp.verifySwatches();
				newpdp.verifyImage(pageNumber);
				newpdp.validateCopyExist();
				newpdp.salePrice(pageNumber);
				
				
			}
			
			Reporter.log("********* Page: " + pageNumber
					+ " completed *****<br>");
		}
	}
	
	/*
	private boolean processBothPDP(){
		/* Sets the LLBSS cookie that changes
		 * from the old PDP to the new PDP
		 * A = old. B= new. */
		
		/*If cookie is set to BOTH, it means that both pdp have to be processed 
		return llbssCookieValue.equals("BOTH");
	}
	
	private void setLLBSSCookie(String value){
		try{
			Cookie cookie = new Cookie("LLBSS",value);
			driver.manage().addCookie(cookie);
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, "Couldn't set cookie", "Cookie not set", JOptionPane.ERROR_MESSAGE);
			System.out.println(e);
		}
	}
	
	*/
	private boolean isPageAvailable() {
		Boolean available = true;
		String notAvailTitle = "L.L.Bean: Page Not Available";

		// looks if the page number is showing the Page Not Available title
		if (driver.getTitle().equalsIgnoreCase(notAvailTitle)) {
			available = false;
			Reporter.log("<span style=\"color:red\"> Page is Not Available</span><br>");
		}
		return available;
	}
	
	

}