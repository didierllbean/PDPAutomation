package defaultPackage;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;

/*Code to check the old Product Pages*/
public class OldPDP extends Util.Settings implements PDP {

	public String getPDPType(){
		return "Old PDP";
	}
	
	public boolean inStock() {
		
		Boolean inStock = true;
		try {
			//Gets the PriceContainer
			WebElement priceCont = driver.findElement(By
					.cssSelector(Selector.ITM_PRICE));
			//Gets the the selector that indicates that the product is sold out.
			WebElement redPrice = priceCont.findElement(By
					.cssSelector(Selector.SOLD_OUT));
			//Validates if the css selector is present because the product is sold out
			String price = redPrice.getText();
			price = price.replaceAll("\\s", "");
			price = price.toLowerCase();
			if (price.equals("soldout")) {
				Reporter.log("<span style=\"color:red\">Product is Sold Out</span><br>");
				inStock = false;
			}
		} catch (NoSuchElementException n) {
			//If the block goes to the exception, it means that the css selector is not present,
			// therefore, the product is not sold out 
		}
	
		return inStock;
	}

	//Validates if the size chart is present
	public boolean validateSizeChart() {
		Boolean sizeChart = false;
		try {
			driver.findElement(By.cssSelector(Selector.SCHART));
			sizeChart = true;
		} catch (NoSuchElementException n) {
			/*If the block goes to the exception, it means that the css selector is not present,
			* therefore, the size chart is not present */
			Reporter.log("<span style=\"color:red\">Size Chart not available</span><br>");
		}
		return sizeChart;

	}
	
	// Validates if the hero image and the alternate views are being displayed
	public boolean verifyImage(String pageNumber) {
		
		Boolean HImage = true;
		try {
			//Obtains the src of the hero image
			String heroImage = driver.findElement(
					By.xpath("id('backImageSjElement4_img')"))
					.getAttribute("src");

			try {
				//If the hero image link is broken
				if (heroImage.contains("img_not_avail")) {
					Reporter.log("<span style=\"color:red\">Hero image is broken</span><br>");
					HImage = false;
				}
			} catch (NullPointerException e) {
				Reporter.log("<span style=\"color:red\">Source not found</span><br>");
			}
			
			//Obtains the alternate views
			List<WebElement> AVimages = driver.findElements(By
					.xpath("id('ppAlternateViews')/div"));

			/* Goes through the list of alternate views, checking if
			 * any image is not available, then adding it to the reporter */
			for (int count = 0; count < AVimages.size(); count++) {
				int alt = count + 1;
				String AV = driver.findElement(
						By.xpath("id('ppAlternateViews')/div[" + alt
								+ "]/a/img")).getAttribute("src");
				if (AV.contains("IMG_not_avail_"))
					Reporter.log("<span style=\"color:red\">Alternate View " + alt
							+ " is not available</span><br>");
			}
		} catch (NoSuchElementException n) {
			Reporter.log("<span style=\"color:red\">Image Not loaded</span><br>");
		}
		return HImage;
	}

	public boolean validateCopyExist() { 
        Boolean copy = false; 
        try { 
                driver.findElement(By.cssSelector(Selector.COPY)); 
                copy = true; 
        } catch (NoSuchElementException n) { 
                Reporter.log("<span style=\"color:red\">Copy not found</span><br>");
        } 
        return copy; 
    }
	
	// Validates if the breadcrumbs are present 
	public boolean validateBreadcrum() {
		Boolean breadC = false;
		try {
			driver.findElement(By.cssSelector(Selector.BREADC));
			breadC = true;
		} catch (NoSuchElementException n) {
			/*If the block goes to the exception, it means that the css selector is not present,
			* therefore, the breadcrumbs are not present */
			Reporter.log("<span style=\"color:red\">Breadcrumbs Not available</span><br>");
		}
		return breadC;

	}
	
	public boolean isProductAvailable() {
		Boolean prodAvailable = true;
		try {
			//Validates if there's a css selector with tag .ppItemUnavailable
			driver.findElement(By.cssSelector(Selector.PROD_AVAIL));
			Reporter.log("<span style=\"color:red\">Product is not available</span><br>");
			prodAvailable = false;

		} catch (NoSuchElementException n) {
			/*If the block goes to the exception, it means that the css selector is not present,
			* therefore, the product is available */
		}
		return prodAvailable;
	}
}
