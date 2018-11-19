package models;

import java.util.ArrayList;
import java.util.Date;

public class BaseService {
	int bId;
	int labourCharge;
	
	float hour;
	String name;
	Date lastService;

	public Date getLastService() {
		return lastService;
	}
	public void setLastService(Date lastService) {
		this.lastService = lastService;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	ArrayList<Part> parts;
	Part part;
	public int getbId() {
		return bId;
	}
	public ArrayList<Part> getParts() {
		return parts;
	}
	public void setParts(ArrayList<Part> parts) {
		this.parts = parts;
	}
	public void setbId(int bId) {
		this.bId = bId;
	}
	public int getLabourCharge() {
		return labourCharge;
	}
	public void setLabourCharge(int labourCharge) {
		this.labourCharge = labourCharge;
	}
	
	public float getHour() {
		return hour;
	}
	public void setHour(float hour) {
		this.hour = hour;
	}
	public Part getPart() {
		return part;
	}
	public void setPart(Part part) {
		this.part = part;
	}
	
}
