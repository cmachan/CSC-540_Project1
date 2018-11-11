package models;

public class Employee {
	int eId;
	String eName;
	String email;
	long phone;
	int serviceCenter;
	public int geteId() {
		return eId;
	}
	public void seteId(int eId) {
		this.eId = eId;
	}
	public String geteName() {
		return eName;
	}
	public void seteName(String eName) {
		this.eName = eName;
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
	public int getServiceCenter() {
		return serviceCenter;
	}
	public void setServiceCenter(int serviceCenter) {
		this.serviceCenter = serviceCenter;
	}
}
