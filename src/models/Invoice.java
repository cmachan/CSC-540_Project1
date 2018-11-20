package models;

public class Invoice {
	String id;
	int partId;
	String partName;
	public String getPartName() {
		return partName;
	}
	public void setPartName(String partName) {
		this.partName = partName;
	}
	boolean warranty;
	float cost;
	boolean isFirst;
	int bid;
	public int getBid() {
		return bid;
	}
	public void setBid(int bid) {
		this.bid = bid;
	}
	public boolean isFirst() {
		return isFirst;
	}
	public void setFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getPartId() {
		return partId;
	}
	public void setPartId(int partId) {
		this.partId = partId;
	}
	public boolean isWarranty() {
		return warranty;
	}
	public void setWarranty(boolean warranty) {
		this.warranty = warranty;
	}
	public float getCost() {
		return cost;
	}
	public void setCost(float cost) {
		this.cost = cost;
	}
}
