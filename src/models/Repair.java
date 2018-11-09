package models;

import java.sql.Timestamp;
import java.util.Date;

public class Repair {
	int rId;
	int cId;
	float fees;
	Car car;
	int mechanicId;
	String faults;
	String serviceType;
	public Car getCar() {
		return car;
	}
	public void setCar(Car car) {
		this.car = car;
	}
	String timeSlot;
	int mid;
	Date rdate;
	int centerId;
	String status;
	String InvoiceNumber;
	String mechanicName;
	Date startTime;
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	Date endTime;
	public String getMechanicName() {
		return mechanicName;
	}
	public void setMechanicName(String mechanicName) {
		this.mechanicName = mechanicName;
	}
	public int getrId() {
		return rId;
	}
	public void setrId(int rId) {
		this.rId = rId;
	}
	public int getcId() {
		return cId;
	}
	public void setcId(int cId) {
		this.cId = cId;
	}
	public float getFees() {
		return fees;
	}
	public void setFees(float fees) {
		this.fees = fees;
	}
	
	public int getMechanicId() {
		return mechanicId;
	}
	public void setMechanicId(int mechanicId) {
		this.mechanicId = mechanicId;
	}
	public String getFaults() {
		return faults;
	}
	public void setFaults(String faults) {
		this.faults = faults;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getTimeSlot() {
		return timeSlot;
	}
	public void setTimeSlot(String timeSlot) {
		this.timeSlot = timeSlot;
	}
	public int getMid() {
		return mid;
	}
	public void setMid(int mid) {
		this.mid = mid;
	}
	public Date getRdate() {
		return rdate;
	}
	public void setRdate(Date rdate) {
		this.rdate = rdate;
	}
	public int getCenterId() {
		return centerId;
	}
	public void setCenterId(int centerId) {
		this.centerId = centerId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getInvoiceNumber() {
		return InvoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		InvoiceNumber = invoiceNumber;
	}
	
	
	
}
