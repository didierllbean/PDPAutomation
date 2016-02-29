package defaultPackage;

import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;
import org.openqa.selenium.JavascriptExecutor;



/*Code to check the new Product Pages*/
public class NewPDP extends Util.Settings implements PDP {

	public String getPDPType(){
		return "New PDP";
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
			/*Validates if the product page needs  Size Chart*/
			JavascriptExecutor js = (JavascriptExecutor) driver;
			Boolean element = (Boolean)js.executeScript("return pdpJson.displaySizeChart");
			
				if(element == true)
				{
				driver.findElement(By.partialLinkText(Selector.NEWSCHART));
				sizeChart = true;
				} return sizeChart;
			
		} catch (NoSuchElementException n) {
			/*If the block goes to the exception, it means that the css selector is not present,
			* therefore, the size chart is not present */
			Reporter.log("<span style=\"color:red\">Size Chart not available</span><br>");
		}
		return sizeChart;

	}
	
	public boolean verifySwatches() {
		boolean hasSwatches = false;
		boolean workingSwatches = true;
		try{
			String styleAttr = driver.findElement(By.className("swatch-bg-0")).getCssValue("background-image");
			hasSwatches = true;
			
			
			int removePosition = styleAttr.indexOf("&defaultImage");
			String url = styleAttr.substring(5, styleAttr.indexOf('?')+1);
			String params = styleAttr.substring(styleAttr.indexOf('?')+1,removePosition);
			params = params.replace('{', '(');
			params = params.replace('}', ')');
			
			String fullURL = url+params;
			workingSwatches = brokenSwatchesNew(fullURL);
			
				driver.findElement(By.className("item-color")).click();
				
				String elementX = driver.findElement(By.className("display")).getText();
				if(elementX == "")
				{
					Reporter.log("<span style=\"color:red\">Swatches are broken</span><br>");
				}return workingSwatches;			
			
		}catch(Exception n){
			n.printStackTrace();
			Reporter.log("<span style=\"color:red\">Swatches are not present</span><br>");
		}
		
		return hasSwatches && workingSwatches;
	}
	
	private boolean brokenSwatchesNew(String url){
		
		 boolean workingSwatches = false;
		 try {
			
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(url);
			HttpResponse response = client.execute(request);
			
			Header[] header = response.getHeaders("Content-Type");
			
			workingSwatches = header[0].getValue().equals("image/jpeg")? true : false;

			System.out.println(header[0]+" "+workingSwatches);
			
			return workingSwatches;
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	return false;
	    }
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
					.xpath("//*[@id='product-item-"+pageNumber+"']/article[2]/ul"));

			/* Goes through the list of alternate views, checking if
			 * any image is not available, then adding it to the reporter */
			for (int count = 0; count < AVimages.size(); count++) {
				int alt = count + 1;
				String AV = driver.findElement(
						By.xpath("//*[@id='product-item-"+pageNumber+"']/article[2]/ul/li["+alt+"]/a/img")).getAttribute("src");
				
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
                driver.findElement(By.xpath(Selector.NEWCOPY)); 
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

	public boolean salePrice(String pageNumber){
		Boolean salePr = true;
		
		try{
			
			//looks for the product item inside the product page
			String PPItem = driver.findElement(By.xpath("//*[@id='product-item-"+pageNumber+"']/div[1]/span")).getText();
			PPItem = PPItem.substring(2);
			//Using the product item number the price classes are searched
			String Price = driver.findElement(By.xpath("//*[@id='item-order-box-"+PPItem+"-0']/div[2]/span")).getAttribute("class");
		
			
			if(Price.contains("item-price-regular"))
				return salePr;
			else{
				driver.findElement(By.className("item-price-reduced"));
				salePr = false;
			}
			
		} catch (NoSuchElementException n){
			Reporter.log("<span style=\"color:red\">Sale Price Not available</span><br>");
		}
		return salePr;
	}


}