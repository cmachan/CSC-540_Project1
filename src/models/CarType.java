package models;

import java.util.ArrayList;

public class CarType {
	String make;
	String model;
	String Type;
	int miles;
	int cid;
	ArrayList<BaseService> baseService;
	public String getMake() {
		return make;
	}
	public ArrayList<BaseService> getBaseService() {
		return baseService;
	}
	public void setBaseService(ArrayList<BaseService> baseService) {
		this.baseService = baseService;
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
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	public int getMiles() {
		return miles;
	}
	public void setMiles(int miles) {
		this.miles = miles;
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	
}
