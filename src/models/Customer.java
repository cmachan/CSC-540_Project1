package models;

import java.util.ArrayList;

public class Customer {
	int cId;
	String cName;
	String email;
	long phone;
	String address;
	String password;
	int centerId;
	Repair service;
	public Repair getService() {
		return service;
	}
	public void setService(Repair service) {
		this.service = service;
	}
	public int getCenterId() {
		return centerId;
	}
	public void setCenterId(int centerId) {
		this.centerId = centerId;
	}
	ArrayList<Car> carsOwned;
	public int getcId() {
		return cId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public ArrayList<Car> getCarsOwned() {
		return carsOwned;
	}
	public void setCarsOwned(ArrayList<Car> carsOwned) {
		this.carsOwned = carsOwned;
	}
	public void setcId(int cId) {
		this.cId = cId;
	}
	public String getcName() {
		return cName;
	}
	public void setcName(String cName) {
		this.cName = cName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public long getPhone() {
		return phone;
	}
	public void setPhone(long phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public Customer(int cId) {
		this.cId = cId;
		
	}
}
