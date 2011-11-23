package SFDCObjects;

public class NonPerformingAccounts {
	
	private String EmployeeName;
	private String AccountName;
	private String ProductType;
	private String Rating;
	private double CoveredSince;
	private String PerformingProducts;
	private String Employee__c;
	private String Account__c;
	
	
	public String getEmployee__c() {
		return Employee__c;
	}
	public void setEmployee__c(String employee__c) {
		Employee__c = employee__c;
	}
	public String getAccount__c() {
		return Account__c;
	}
	public void setAccount__c(String account__c) {
		Account__c = account__c;
	}
	public String getEmployeeName() {
		return EmployeeName;
	}
	public void setEmployeeName(String employeeName) {
		EmployeeName = employeeName;
	}
	public String getAccountName() {
		return AccountName;
	}
	public void setAccountName(String accountName) {
		AccountName = accountName;
	}
	public String getProductType() {
		return ProductType;
	}
	public void setProductType(String productType) {
		ProductType = productType;
	}
	public String getRating() {
		return Rating;
	}
	public void setRating(String rating) {
		Rating = rating;
	}
	public double getCoveredSince() {
		return CoveredSince;
	}
	public void setCoveredSince(double coveredSince) {
		CoveredSince = coveredSince;
	}
	public String getPerformingProducts() {
		return PerformingProducts;
	}
	public void setPerformingProducts(String performingProducts) {
		PerformingProducts = performingProducts;
	}
	
	@Override
	public String toString() {
		return "NonPerformingAccounts [EmployeeName=" + EmployeeName
				+ ", AccountName=" + AccountName + ", ProductType="
				+ ProductType + ", Rating=" + Rating + ", CoveredSince="
				+ CoveredSince + ", PerformingProducts=" + PerformingProducts
				+ "]";
	}	

}
