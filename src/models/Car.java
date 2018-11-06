package models;

public class Car {
	int licensePlate;
	String make;
	String model;
	int makeYear;
	public String getMake() {
		return make;
	}
	public void setMake(String make) {
		this.make = make;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public int getMakeYear() {
		return makeYear;
	}
	public void setMakeYear(int makeYear) {
		this.makeYear = makeYear;
	}
	int cId;
	String dateOfService;
	String lastServiceType;
	String dateOfPurchase;
	int lastMileage;
	public int getLicensePlate() {
		return licensePlate;
	}
	public void setLicensePlate(int licensePlate) {
		this.licensePlate = licensePlate;
	}
	
	public int getcId() {
		return cId;
	}
	public void setcId(int cId) {
		this.cId = cId;
	}
	public String getDateOfService() {
		return dateOfService;
	}
	public void setDateOfService(String dateOfService) {
		this.dateOfService = dateOfService;
	}
	public String getLastServiceType() {
		return lastServiceType;
	}
	public void setLastServiceType(String lastServiceType) {
		this.lastServiceType = lastServiceType;
	}
	public String getDateOfPurchase() {
		return dateOfPurchase;
	}
	public void setDateOfPurchase(String dateOfPurchase) {
		this.dateOfPurchase = dateOfPurchase;
	}
	public int getLastMileage() {
		return lastMileage;
	}
	public void setLastMileage(int lastMileage) {
		this.lastMileage = lastMileage;
	}
}
