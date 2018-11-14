package dataAccessObjects;

import java.util.ArrayList;

import databaseUtilities.DatabaseUtil;

public class DistributorDao {
	int did;
	String name;
	int deliveryTime;
	
	public int getDid() {
		return did;
	}
	
	public void setDid(int did) {
		this.did = did;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getDeliveryTime() {
		return deliveryTime;
	}
	
	public void setDeliveryTime(int deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	void populate() throws Exception{
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		ArrayList<String> res = db.getAll("DISTRIBUTOR", "DID", Integer.toString(this.did), true);
		
		if(res.size() == 0)
			throw new Exception("Object Doesn't exists in the database");
		
		int idx = 0;
		for(String s : res) {
			if(idx == 0)
				did = Integer.parseInt(s);
			else if(idx == 1)
				name = s;
			else
				deliveryTime = Integer.parseInt(s);
			idx++;
		}
		db.closeConnection();
	}
	
	public DistributorDao(int did, String name, int deliveryTime) {
		this.did = did;
		this.name = name;
		this.deliveryTime = deliveryTime;
	}

	public DistributorDao(int did) throws Exception {
		this.did = did;
		populate();
	}
	
	public void insert() {
		String qry = "INSERT INTO DISTRIBUTOR VALUES(" + did + ",'" + name + "'," + deliveryTime + ")";
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		db.runQuery(qry);
		db.closeConnection();
	}
	
	public void update() {
		String qry = "UPDATE DISTRIBUTOR set NAME = '" + name + "' , DELIVERYTIME = " + deliveryTime + " WHERE DID = " + did;
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		db.runQuery(qry);
		db.closeConnection();
	}
	
	public void delete() {
		String qry = "DELETE FROM DISTRIBUTOR WHERE DID = " + did;
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		db.runQuery(qry);
		db.closeConnection();
	}
	
}
