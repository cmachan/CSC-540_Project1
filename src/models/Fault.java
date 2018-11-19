package models;

import java.util.ArrayList;

public class Fault {
	int fId;
	String fName;
	String diagnostic;
	int fee;
	ArrayList<BaseService> bs;
	public int getfId() {
		return fId;
	}
	public void setfId(int fId) {
		this.fId = fId;
	}
	public String getfName() {
		return fName;
	}
	public void setfName(String fName) {
		this.fName = fName;
	}
	public String getDiagnostic() {
		return diagnostic;
	}
	public void setDiagnostic(String diagnostic) {
		this.diagnostic = diagnostic;
	}
	public int getFee() {
		return fee;
	}
	public void setFee(int fee) {
		this.fee = fee;
	}
	public ArrayList<BaseService> getBs() {
		return bs;
	}
	public void setBs(ArrayList<BaseService> bs) {
		this.bs = bs;
	}
	
}
