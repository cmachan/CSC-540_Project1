package dataAccessObjects;

import java.util.ArrayList;

import databaseUtilities.DatabaseUtil;

public class Inventory_PartsDao {
	int partId;
	String name;
	double unitPrice;
	int distributorId;
	
	public int getPartId() {
		return partId;
	}
	
	public void setPartId(int partId) {
		this.partId = partId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public double getUnitPrice() {
		return unitPrice;
	}
	
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public int getDistributorId() {
		return distributorId;
	}
	
	public void setDistributorId(int distributorId) {
		this.distributorId = distributorId;
	}
	
	void populate() throws Exception{
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		ArrayList<String> res = db.getAll("INVENTORY_PARTS", "PARTID", Integer.toString(this.partId), true);
		
		if(res.size() == 0)
			throw new Exception("Object Doesn't exists in the database");
		
		int idx = 0;
		for(String s : res) {
			if(idx == 0)
				partId = Integer.parseInt(s);
			else if(idx == 1)
				name = s;
			else if(idx == 2)
				unitPrice = Double.parseDouble(s);
			else
				distributorId = Integer.parseInt(s);
			idx++;
		}
		db.closeConnection();
	}
	
	public Inventory_PartsDao(int partId, String name, double unitPrice, int distributorDid) {
		this.partId = partId;
		this.name = name;
		this.unitPrice = unitPrice;
		this.distributorId = distributorDid;
	}

	public Inventory_PartsDao(int partId) throws Exception {
		this.partId = partId;
		populate();
	}
	
	public void insert() {
		String qry = "INSERT INTO INVENTORY_PARTS VALUES(" + partId + ",'" + name + "'," + unitPrice + "," + distributorId + ")";
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		db.runQuery(qry);
		db.closeConnection();
	}
	
	public void update() {
		String qry = "UPDATE INVENTORY_PARTS set NAME = '" + name + "' , UNITPRICE = " + unitPrice + " , DISTRIBUTORID = " + distributorId + " WHERE PARTID = " + partId;
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		db.runQuery(qry);
		db.closeConnection();
	}
	
	public void delete() {
		String qry = "DELETE FROM INVENTORY_PARTS WHERE PARTID = " + partId;
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		db.runQuery(qry);
		db.closeConnection();
	}
	
}
