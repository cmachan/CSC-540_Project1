package dataAccessObjects;

import java.util.ArrayList;

import databaseUtilities.DatabaseUtil;

public class InventoryDao {
	int partId;
	int serviceCenterId;
	int quantity;
	int minQtyOrder;
	int threshold;
	
	public int getPartId() {
		return partId;
	}
	
	public void setPartId(int partId) {
		this.partId = partId;
	}
	
	public int getServiceCenterId() {
		return serviceCenterId;
	}
	
	public void setServiceCenterId(int serviceCenterId) {
		this.serviceCenterId = serviceCenterId;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public int getMinQtyOrder() {
		return minQtyOrder;
	}
	
	public void setMinQtyOrder(int minQtyOrder) {
		this.minQtyOrder = minQtyOrder;
	}
	
	public int getThreshold() {
		return threshold;
	}
	
	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	void populate() throws Exception{
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		ArrayList<String> res = db.getAll("INVENTORY", "PARTID", Integer.toString(this.partId), true);
		
		if(res.size() == 0)
			throw new Exception("Object Doesn't exists in the database");
		
		int idx = 0;
		for(String s : res) {
			if(idx == 0)
				partId = Integer.parseInt(s);
			else if(idx == 1)
				serviceCenterId = Integer.parseInt(s);
			else if(idx == 2)
				quantity = Integer.parseInt(s);
			else if(idx == 3)
				minQtyOrder = Integer.parseInt(s);
			else
				threshold = Integer.parseInt(s);
			
			idx++;
		}
		db.closeConnection();
	}
	
	public InventoryDao(int partId, int serviceCenterId, int quantity, int minQtyOrder, int threshold) {
		this.partId = partId;
		this.serviceCenterId = serviceCenterId;
		this.quantity = quantity;
		this.minQtyOrder = minQtyOrder;
		this.threshold = threshold;
	}

	public InventoryDao(int partId) throws Exception {
		this.partId = partId;
		populate();
	}
	
	/*
	 * int partId;
	int serviceCenterId;
	int quantity;
	int minQtyOrder;
	int threshold;
	 */
	
	public void insert() {
		String qry = "INSERT INTO INVENTORY VALUES(" + partId + "," + serviceCenterId + "," + quantity + "," + minQtyOrder + "," + threshold + ")";
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		db.runQuery(qry);
		db.closeConnection();
	}
	
	public void update() {
		String qry = "UPDATE INVENTORY set SERVICECENTERID = " + serviceCenterId + " , QUANTITY = " + quantity + " , MINQTYORDER = " + minQtyOrder + " , THRESHOLD = " + threshold + " WHERE PARTID = " + partId;
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		db.runQuery(qry);
		db.closeConnection();
	}
	
	public void delete() {
		String qry = "DELETE FROM INVENTORY WHERE PARTID = " + partId;
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		db.runQuery(qry);
		db.closeConnection();
	}
	
	
	
	
}
