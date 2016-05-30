package defaultPackage;

import java.util.List;

import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;

import java.awt.image.BufferedImage;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import java.net.URL;
import java.net.URLConnection;
import java.io.File;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.PixelGrabber;

/*Code to check the new Product Pages*/
public class NewPDP extends Util.Settings implements PDP {

	public String getPDPType() {
		return "New PDP";
	}

	public boolean inStock() {

		Boolean inStock = true;
		try {
			// Gets the PriceContainer
			WebElement priceCont = driver.findElement(By
					.cssSelector(Selector.ITM_PRICE));
			// Gets the the selector that indicates that the product is sold
			// out.
			WebElement redPrice = priceCont.findElement(By
					.cssSelector(Selector.SOLD_OUT));
			// Validates if the css selector is present because the product is
			// sold out
			String price = redPrice.getText();
			price = price.replaceAll("\\s", "");
			price = price.toLowerCase();
			if (price.equals("soldout")) {
				Reporter.log("<span style=\"color:red\">Product is Sold Out</span><br>");
				inStock = false;
			}
		} catch (NoSuchElementException n) {
			// If the block goes to the exception, it means that the css
			// selector is not present,
			// therefore, the product is not sold out
		}

		return inStock;
	}

	// Validates if the size chart is present
	public boolean validateSizeChart() {
		Boolean sizeChart = false;
		try {
			/* Validates if the product page needs Size Chart */
			JavascriptExecutor js = (JavascriptExecutor) driver;
			Boolean element = (Boolean) js
					.executeScript("return pdpJson.displaySizeChart");

			if (element == true) {
				driver.findElement(By.partialLinkText(Selector.NEWSCHART));
				sizeChart = true;
			}
			return sizeChart;

		} catch (NoSuchElementException n) {
			/*
			 * If the block goes to the exception, it means that the css
			 * selector is not present, therefore, the size chart is not present
			 */
			Reporter.log("<span style=\"color:red\">Size Chart not available</span><br>");
		}
		return sizeChart;

	}

	public static BufferedImage decodeToImage(String imageString) {

		BufferedImage image = null;
		byte[] imageByte;
		try {
			BASE64Decoder decoder = new BASE64Decoder();
			imageByte = decoder.decodeBuffer(imageString);
			ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
			image = ImageIO.read(bis);
			bis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return image;
	}

	/**
	 * Encode image to string
	 * 
	 * @param image
	 *            The image to encode
	 * @param type
	 *            jpeg, bmp, ...
	 * @return encoded string
	 */
	public static String encodeToString(BufferedImage image, String type) {
		String imageString = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {
			ImageIO.write(image, type, bos);
			byte[] imageBytes = bos.toByteArray();

			BASE64Encoder encoder = new BASE64Encoder();
			imageString = encoder.encode(imageBytes);

			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imageString;
	}

	public void VerifySwatches() {

		BufferedImage image = null;
		try {

			String ImagePath = driver.findElement(By.className("swatch-bg-0")).getCssValue("background-image");
			ImagePath = ImagePath.replace(
					ImagePath.substring(ImagePath.length() - 1), "");
			ImagePath = ImagePath.replace(
					ImagePath.substring(ImagePath.length() - 1), "");
			String url = ImagePath.substring(4, ImagePath.indexOf('?') + 1);
			String params = ImagePath.substring(ImagePath.indexOf('?') + 1);

			String fullURL = url;

			URL url2 = new URL(fullURL + params);
			// read the url
			URLConnection conn1 = url2.openConnection();
			conn1.setRequestProperty(
					"User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
			InputStream in1 = conn1.getInputStream();
			image = ImageIO.read(in1);

			ImageIO.write(image, "jpg", new File(System.getProperty("user.dir")
					+ "\\imagecompare.jpg"));

			/*
			 * BufferedImage imgMala = ImageIO.read(new
			 * File(System.getProperty("user.dir")+"\\PDPimagecompare.jpg"));
			 * BufferedImage imgWEB = ImageIO.read(new
			 * File(System.getProperty("user.dir")+"\\imagecompare.jpg"));
			 * 
			 * String imgstr; String imgstr2; imgstr = encodeToString(imgMala,
			 * "png"); imgstr2 = encodeToString(imgWEB, "png");
			 * 
			 * if (imgstr.equals(imgstr2)) Reporter.log("Swatches are broken");
			 */
			processImage(System.getProperty("user.dir")
					+ "\\PDPimagecompare.jpg", System.getProperty("user.dir")
					+ "\\imagecompare.jpg");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchElementException n){
			Reporter.log("<span style=\"color:red\">Swatch is not present</span><br>");
		}
		

	}

	public void processImage(String file1, String file2) {
		
		Boolean MSwatch = false;

		Image image1 = Toolkit.getDefaultToolkit().getImage(file1);
		Image image2 = Toolkit.getDefaultToolkit().getImage(file2);

		try {

			PixelGrabber grab1 = new PixelGrabber(image1, 0, 0, -1, -1, false);
			PixelGrabber grab2 = new PixelGrabber(image2, 0, 0, -1, -1, false);

			int[] data1 = null;

			if (grab1.grabPixels()) {
				int width = grab1.getWidth();
				int height = grab1.getHeight();
				data1 = new int[width * height];
				data1 = (int[]) grab1.getPixels();
			}

			int[] data2 = null;

			if (grab2.grabPixels()) {
				int width = grab2.getWidth();
				int height = grab2.getHeight();
				data2 = new int[width * height];
				data2 = (int[]) grab2.getPixels();
			}
			MSwatch = java.util.Arrays.equals(data1, data2);

			if (MSwatch == true)
				Reporter.log("<span style=\"color:red\">Swatches are broken</span><br>");
			// Reporter.log("Pixels equal: " + java.util.Arrays.equals(data1,
			// data2));

		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

	}

	// Validates if the hero image and the alternate views are being displayed
	public boolean verifyImage(String pageNumber) {
		Boolean HImage = true;
		try {
			// Obtains the src of the hero image

			String heroImage = driver
					.findElement(
							By.cssSelector("div[id='backImageSjElement4']>img[id='backImageSjElement4_img']"))
					.getAttribute("src");
			// .getAttribute("alt");

			try {
				// If the hero image link is broken
				if (heroImage.contains("img_not_avail")) {
					Reporter.log("<span style=\"color:red\">Hero image is broken</span><br>");
					HImage = false;
				}
			} catch (NullPointerException e) {
				Reporter.log("<span style=\"color:red\">Source not found</span><br>");
			}

			// Obtains the alternate views
			List<WebElement> AVimages = driver.findElements(By
					.cssSelector("ul[class='item-media-alternate']>li"));

			/*
			 * Goes through the list of alternate views, checking if any image
			 * is not available, then adding it to the reporter
			 */
			for (int count = 0; count < AVimages.size(); count++) {
				int alt = count + 1;
				String AV = driver.findElement(
						By.xpath("//*[@id='product-item-" + pageNumber
								+ "']/article[2]/ul/li[" + alt + "]/a/img"))
						.getAttribute("src");

				if (AV.contains("IMG_not_avail_"))
					Reporter.log("<span style=\"color:red\">Alternate View "
							+ alt + " is not available</span><br>");
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
			/*
			 * If the block goes to the exception, it means that the css
			 * selector is not present, therefore, the breadcrumbs are not
			 * present
			 */
			Reporter.log("<span style=\"color:red\">Breadcrumbs Not available</span><br>");
		}
		return breadC;

	}

	public boolean isProductAvailable() {
		Boolean prodAvailable = true;
		try {
			// Validates if there's a css selector with tag .ppItemUnavailable
			driver.findElement(By.cssSelector(Selector.PROD_AVAIL));
			Reporter.log("<span style=\"color:red\">Product is not available</span><br>");
			prodAvailable = false;

		} catch (NoSuchElementException n) {
			/*
			 * If the block goes to the exception, it means that the css
			 * selector is not present, therefore, the product is available
			 */
		}
		return prodAvailable;
	}

	public boolean salePrice(String pageNumber) {
		Boolean salePr = true;

		try {

			// looks for the product item inside the main price div
			String PPItem = driver.findElement(
					By.xpath("//*[@id='product-item-" + pageNumber
							+ "']/div[1]/span")).getText();
			PPItem = PPItem.substring(2);
			// Using the product item number the price classes are searched
			String Price = driver.findElement(
					By.xpath("//*[@id='item-order-box-" + PPItem
							+ "-0']/div[2]/span")).getAttribute("class");

			if (Price.contains("item-price-regular"))
				return salePr;
			else {
				driver.findElement(By.className("item-price-reduced"));
				salePr = false;
			}

		} catch (NoSuchElementException n) {
			Reporter.log("<span style=\"color:red\">Sale Price Not available</span><br>");
		}
		return salePr;
	}

}