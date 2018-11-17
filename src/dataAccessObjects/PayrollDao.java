package dataAccessObjects;

import java.util.ArrayList;

import databaseUtilities.DatabaseUtil;

public class PayrollDao {
	int eId;
	String startDate;
	double wages;
	int frequency;
	
	public int geteId() {
		return eId;
	}
	
	public void seteId(int eId) {
		this.eId = eId;
	}
	
	public String getStartDate() {
		return startDate;
	}
	
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	public double getWages() {
		return wages;
	}
	
	public void setWages(double wages) {
		this.wages = wages;
	}
	
	public int getFrequency() {
		return frequency;
	}
	
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	void populate() throws Exception{
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		ArrayList<String> res = db.getAll("PAYROLL", "EID", Integer.toString(this.eId), true);
		
		if(res.size() == 0)
			throw new Exception("Object Doesn't exists in the database");
		
		int idx = 0;
		for(String s : res) {
			if(idx == 0)
				eId = Integer.parseInt(s);
			else if(idx == 1)
				startDate = s;
			else if(idx == 2)
				wages = Double.parseDouble(s);
			else
				frequency = Integer.parseInt(s);
				
			idx++;
		}
		db.closeConnection();
	}
	
	public PayrollDao(int eId, String startDate, double wages, int frequency) {
		this.eId = eId;
		this.startDate = startDate;
		this.wages = wages;
		this.frequency = frequency;
	}

	public PayrollDao(int eId) throws Exception{
		this.eId = eId;
		populate();
	}
	
	public void insert() {
		String qry = "INSERT INTO PAYROLL VALUES(" + eId + ", TO_DATE ('" + startDate + "','YYYY-MM-DD')" + "," + wages + "," + frequency + ")";
		System.out.println(qry);
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		db.runQuery(qry);
		db.closeConnection();
	}
	
	public void update() {
		String qry = "UPDATE PAYROLL set STARTDATE = TO_DATE ('" + startDate + "','YYYY-MM-DD') , WAGES = " + wages +" , FREQUENCY = " + frequency + " WHERE EID = " + eId;
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		db.runQuery(qry);
		db.closeConnection();
	}
	
	public void delete() {
		String qry = "DELETE FROM PAYROLL WHERE EID = " + eId;
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		db.runQuery(qry);
		db.closeConnection();
	}
	
	
}
