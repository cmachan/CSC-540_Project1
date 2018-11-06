package dataAccessObjects;

import java.util.ArrayList;

import databaseUtilities.DatabaseUtil;

public class CustomerDao {
	int cId;
	String cName;
	String email;
	long phone;
	String address;
	
	public int getcId() {
		return cId;
	}
	
	public void setcId(int cId) {
		this.cId = cId;
	}
	
	public String getcName() {
		return cName;
	}
	
	public void setcName(String cName) {
		this.cName = cName;
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
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	void populate() throws Exception{
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		ArrayList<String> res = db.getAll("CUSTOMER", "cId", Integer.toString(this.cId), true);
		
		if(res.size() == 0)
			throw new Exception("Object Doesn't exists in the database");
		
		int idx = 0;
		for(String s : res) {
			if(idx == 0)
				cId = Integer.parseInt(s);
			else if(idx == 1)
				cName = s;
			else if(idx == 2)
				email = s;
			else if(idx == 3)
				phone = Long.parseLong(s);
			else
				address = s;
			
			idx++;
		}
		db.closeConnection();
	}

	public CustomerDao(int cId, String cName, String email, long phone, String address) {
		this.cId = cId;
		this.cName = cName;
		this.email = email;
		this.phone = phone;
		this.address = address;
	}

	public CustomerDao(int cId) throws Exception {
		this.cId = cId;
		populate();
	}
	
	public void insert() {
		String qry = "INSERT INTO CUSTOMER VALUES(" + cId + ",'" + cName + "','" + email + "'," + phone + ",'" + address + "')";
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		db.runQuery(qry);
		db.closeConnection();
	}
	
	public void update() {
		String qry = "UPDATE CUSTOMER set CNAME = '" + cName + "' , EMAIL = '" + email + "' , PHONE = " + phone + " , ADDRESS = '" + address + "' WHERE CID = " + cId;
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		db.runQuery(qry);
		db.closeConnection();
	}
	
	public void delete() {
		String qry = "DELETE FROM CUSTOMER WHERE CID = " + cId;
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		db.runQuery(qry);
		db.closeConnection();
	}
}
