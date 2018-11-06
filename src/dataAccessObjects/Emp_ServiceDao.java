package dataAccessObjects;

import java.util.ArrayList;

import databaseUtilities.DatabaseUtil;

public class Emp_ServiceDao {
	int eId;
	String role;
	int serviceCenterId;
	
	public int geteId() {
		return eId;
	}
	
	public void seteId(int eId) {
		this.eId = eId;
	}
	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
	
	public int getServiceCenterId() {
		return serviceCenterId;
	}
	
	void populate() throws Exception{
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		ArrayList<String> res = db.getAll("EMP_SERVICE", "EID", Integer.toString(this.eId), true);
		
		if(res.size() == 0)
			throw new Exception("Object Doesn't exists in the database");
		
		int idx = 0;
		for(String s : res) {
			if(idx == 0)
				eId = Integer.parseInt(s);
			else if(idx == 1)
				role = s;
			else
				serviceCenterId = Integer.parseInt(s);
			
			idx++;
		}
		db.closeConnection();
	}
	
	public void setServiceCenterId(int serviceCenterId) {
		this.serviceCenterId = serviceCenterId;
		
	}

	public Emp_ServiceDao(int eId, String role, int serviceCenterId) {
		this.eId = eId;
		this.role = role;
		this.serviceCenterId = serviceCenterId;
	}

	public Emp_ServiceDao(int eId) throws Exception {
		this.eId = eId;
		populate();
	}
	
	public void insert() {
		String qry = "INSERT INTO EMP_SERVICE VALUES(" + eId + ",'" + role + "'," + serviceCenterId + ")";
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		db.runQuery(qry);
		db.closeConnection();
	}
	
	public void update() {
		String qry = "UPDATE EMP_SERVICE set ROLE = '" + role + "' , SERVICECENTERID = " + serviceCenterId + " WHERE EID = " + eId;
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		db.runQuery(qry);
		db.closeConnection();
	}
	
	public void delete() {
		String qry = "DELETE FROM EMP_SERVICE WHERE EID = " + eId;
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		db.runQuery(qry);
		db.closeConnection();
	}
	
	
}
