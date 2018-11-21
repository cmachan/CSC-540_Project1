package models;

import java.util.Date;

public class Employee {
	int eId;
	String eName;
	String email;
	long phone;
	int serviceCenter;
	Date sDate;
	Date startTime;
	Date endTime;
	String address;
	double wage;
	String role;
	String pass;
		public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public double getWage() {
		return wage;
	}

	public void setWage(double wage) {
		this.wage = wage;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(java.util.Date date2) {
		this.startTime = date2;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Date getsDate() {
		return sDate;
	}
	public void setsDate(Date sDate) {
		this.sDate = sDate;
	}
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
