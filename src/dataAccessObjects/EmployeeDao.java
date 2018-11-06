package dataAccessObjects;

import java.util.ArrayList;

import databaseUtilities.DatabaseUtil;

public class EmployeeDao {
	int eId;
	String eName;
	String email;
	long phone;
	
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

	void populate() throws Exception{
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		ArrayList<String> res = db.getAll("EMPLOYEE", "EID", Integer.toString(this.eId), true);
		
		if(res.size() == 0)
			throw new Exception("Object Doesn't exists in the database");
		
		int idx = 0;
		for(String s : res) {
			if(idx == 0)
				eId = Integer.parseInt(s);
			else if(idx == 1)
				eName = s;
			else if(idx == 2)
				email = s;
			else
				phone = Long.parseLong(s);
			
			idx++;
		}
		db.closeConnection();
	}
	
	public EmployeeDao(int eId, String eName, String email, long phone) {
		this.eId = eId;
		this.eName = eName;
		this.email = email;
		this.phone = phone;
	}
	
	public EmployeeDao(int eId) throws Exception{
		this.eId = eId;
		populate();
	}
	
	public void insert() {
		String qry = "INSERT INTO EMPLOYEE VALUES(" + eId + ",'" + eName + "','" + email + "'," + phone + ")";
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		db.runQuery(qry);
		db.closeConnection();
	}
	
	public void update() {
		String qry = "UPDATE EMPLOYEE set ENAME = '" + eName + "' , EMAIL = '" + email + "' , PHONE = " + phone + " WHERE EID = " + eId;
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		db.runQuery(qry);
		db.closeConnection();
	}
	
	public void delete() {
		String qry = "DELETE FROM EMPLOYEE WHERE EID = " + eId;
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		db.runQuery(qry);
		db.closeConnection();
	}
	
}
