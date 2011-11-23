package SFDCObjects;

public class SalesPersonReport {
	
	private double MTD;
	private double WTD;
	private double YTD;
	private String Name;
	private String EmployeeID;
	
	public SalesPersonReport(){}
	
	public double getMTD() {
		return MTD;
	}
	public void setMTD(double mTD) {
		MTD = mTD;
	}
	public double getWTD() {
		return WTD;
	}
	public void setWTD(double wTD) {
		WTD = wTD;
	}
	public double getYTD() {
		return YTD;
	}
	public void setYTD(double yTD) {
		YTD = yTD;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getEmployeeID() {
		return EmployeeID;
	}
	public void setEmployeeID(String employeeID) {
		EmployeeID = employeeID;
	}

	@Override
	public String toString() {
		return "SalesPersonReport [MTD=" + MTD + ", WTD=" + WTD + ", YTD="
				+ YTD + ", Name=" + Name + ", EmployeeID=" + EmployeeID + "]";
	}
	
	
	
}
