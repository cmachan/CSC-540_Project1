package models;

import java.util.Date;

public class Car {
	String licensePlate;
	String make;
	String model;
	int makeYear;
	int carTypeID;
	public int getCarTypeID() {
		return carTypeID;
	}
	public void setCarTypeID(int carTypeID) {
		this.carTypeID = carTypeID;
	}
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
	public int getNewMileage() {
		return newMileage;
	}
	public void setNewMileage(int newMileage) {
		this.newMileage = newMileage;
	}
	int cId;
	Date dateOfService;
	String lastServiceType;
	Date dateOfPurchase;
	int lastMileage;
	int newMileage;
	public String getLicensePlate() {
		return licensePlate;
	}
	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}
	
	public int getcId() {
		return cId;
	}
	public void setcId(int cId) {
		this.cId = cId;
	}
	public Date getDateOfService() {
		return dateOfService;
	}
	public void setDateOfService(Date dateOfService) {
		this.dateOfService = dateOfService;
	}
	public String getLastServiceType() {
		return lastServiceType;
	}
	public void setLastServiceType(String lastServiceType) {
		this.lastServiceType = lastServiceType;
	}
	public Date getDateOfPurchase() {
		return dateOfPurchase;
	}
	public void setDateOfPurchase(Date dateOfPurchase) {
		this.dateOfPurchase = dateOfPurchase;
	}
	public int getLastMileage() {
		return lastMileage;
	}
	public void setLastMileage(int lastMileage) {
		this.lastMileage = lastMileage;
	}
}
