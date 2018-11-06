package dataAccessObjects;

import java.util.ArrayList;

import databaseUtilities.DatabaseUtil;

public class Mech_HoursDao {
	int eId;
	int hours;
	
	public int geteId() {
		return eId;
	}
	
	public void seteId(int eId) {
		this.eId = eId;
	}
	
	public int getHours() {
		return hours;
	}
	
	public void setHours(int hours) {
		this.hours = hours;
	}
	
	void populate() throws Exception{
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		ArrayList<String> res = db.getAll("MECH_HOURS", "EID", Integer.toString(this.eId), true);
		
		if(res.size() == 0)
			throw new Exception("Object Doesn't exists in the database");
		
		int idx = 0;
		for(String s : res) {
			if(idx == 0)
				eId = Integer.parseInt(s);
			else
				hours = Integer.parseInt(s);
			
			idx++;
		}
		db.closeConnection();
	}

	public Mech_HoursDao(int eId, int hours) {
		this.eId = eId;
		this.hours = hours;
	}

	public Mech_HoursDao(int eId) throws Exception {
		this.eId = eId;
		populate();
	}
	
	public void insert() {
		String qry = "INSERT INTO MECH_HOURS VALUES(" + eId + "," + hours +")";
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		db.runQuery(qry);
		db.closeConnection();
	}
	
	public void update() {
		String qry = "UPDATE MECH_HOURS set HOURS = " + hours +  " WHERE EID = " + eId;
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		db.runQuery(qry);
		db.closeConnection();
	}
	
	public void delete() {
		String qry = "DELETE FROM MECH_HOURS WHERE EID = " + eId;
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		db.runQuery(qry);
		db.closeConnection();
	}
}
