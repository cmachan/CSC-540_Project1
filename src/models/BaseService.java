package models;

public class BaseService {
	int bId;
	String labourCharge;
	boolean warranty;
	int hour;
	Part part;
	public int getbId() {
		return bId;
	}
	public void setbId(int bId) {
		this.bId = bId;
	}
	public String getLabourCharge() {
		return labourCharge;
	}
	public void setLabourCharge(String labourCharge) {
		this.labourCharge = labourCharge;
	}
	public boolean isWarranty() {
		return warranty;
	}
	public void setWarranty(boolean warranty) {
		this.warranty = warranty;
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public Part getPart() {
		return part;
	}
	public void setPart(Part part) {
		this.part = part;
	}
	
}
