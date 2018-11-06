package dataAccessObjects;

import java.util.ArrayList;

import databaseUtilities.DatabaseUtil;

public class ServiceCenterDao {
	int id;
	String name;
	String address;
	long phone;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
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
		ArrayList<String> res = db.getAll("SERVICECENTER", "ID", Integer.toString(this.id), true);
		
		if(res.size() == 0)
			throw new Exception("Object Doesn't exists in the database");
		
		int idx = 0;
		for(String s : res) {
			if(idx == 0)
				id = Integer.parseInt(s);
			else if(idx == 1)
				name = s;
			else if(idx == 2)
				address = s;
			else
				phone = Long.parseLong(s);
			
			idx++;
		}
		db.closeConnection();
	}
	
	public ServiceCenterDao(int id, String name, String address, long phone) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.phone = phone;
	}

	public ServiceCenterDao(int id) throws Exception{
		this.id = id;
		populate();
	}
	
	public void insert() {
		String qry = "INSERT INTO SERVICECENTER VALUES(" + id + ",'" + name + "','" + address + "'," + phone + ")";
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		db.runQuery(qry);
		db.closeConnection();
	}
	
	public void update() {
		String qry = "UPDATE SERVICECENTER set NAME = '" + name + "' , ADDRESS = '" + address + "' , PHONE = " + phone + " WHERE ID = " + id;
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		db.runQuery(qry);
		db.closeConnection();
	}
	
	public void delete() {
		String qry = "DELETE FROM SERVICECENTER WHERE ID = " + id;
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		db.runQuery(qry);
		db.closeConnection();
	}
	
	
}
