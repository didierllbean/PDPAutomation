package defaultPackage;

/*Interface with the basic structure of the Product Pages */
public interface PDP {

	public String getPDPType();
	public boolean inStock();
	public boolean validateSizeChart();
	public void VerifySwatches();
	public boolean verifyImage(String page);
	public boolean validateCopyExist();
	public boolean validateBreadcrum();
	public boolean isProductAvailable();
	
	
}