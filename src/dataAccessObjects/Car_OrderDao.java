package dataAccessObjects;

import java.util.ArrayList;

import databaseUtilities.DatabaseUtil;

public class Car_OrderDao {
	int oId;
	String oDate;
	int qty;
	int centerId;
	int distributorId;
	String status;
	int reqCenterId;
	String reqType;
	int partId;
	
	public int getoId() {
		return oId;
	}
	
	public void setoId(int oId) {
		this.oId = oId;
	}
	
	public String getoDate() {
		return oDate;
	}
	
	public void setoDate(String oDate) {
		this.oDate = oDate;
	}
	
	public int getQty() {
		return qty;
	}
	
	public void setQty(int qty) {
		this.qty = qty;
	}
	
	public int getCenterId() {
		return centerId;
	}
	
	public void setCenterId(int centerId) {
		this.centerId = centerId;
	}
	
	public int getDistributorId() {
		return distributorId;
	}
	
	public void setDistributorId(int distributorId) {
		this.distributorId = distributorId;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public int getReqCenterId() {
		return reqCenterId;
	}
	
	public void setReqCenterId(int reqCenterId) {
		this.reqCenterId = reqCenterId;
	}
	
	public String getReqType() {
		return reqType;
	}
	
	public void setReqType(String reqType) {
		this.reqType = reqType;
	}
	
	public int getPartId() {
		return partId;
	}
	
	public void setPartId(int partId) {
		this.partId = partId;
	}
	
	void populate() throws Exception{
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		ArrayList<String> res = db.getAll("CAR_ORDER", "OID", Integer.toString(this.oId), true);
		
		if(res.size() == 0)
			throw new Exception("Object Doesn't exists in the database");
		
		int idx = 0;
		for(String s : res) {
			if(idx == 0)
				oId = Integer.parseInt(s);
			else if(idx == 1) {
				oDate = (s.split(" "))[0];
			}
			else if(idx == 2)
				qty = Integer.parseInt(s);
			else if(idx == 3)
				centerId = Integer.parseInt(s);
			else if(idx == 4)
				distributorId = Integer.parseInt(s);
			else if(idx == 5)
				status = s;
			else if(idx == 6)
				reqCenterId = Integer.parseInt(s);
			else if(idx == 7)
				reqType = s;
			else
				partId = Integer.parseInt(s);
			
			idx++;
		}
		db.closeConnection();
	}

	public Car_OrderDao(int oId, String oDate, int qty, int centerId, int distributorId, String status, int reqCenterId,
			String reqType, int partId) {
		this.oId = oId;
		this.oDate = oDate;
		this.qty = qty;
		this.centerId = centerId;
		this.distributorId = distributorId;
		this.status = status;
		this.reqCenterId = reqCenterId;
		this.reqType = reqType;
		this.partId = partId;
	}

	public Car_OrderDao(int oId) throws Exception {
		this.oId = oId;
		populate();
	}
	
	public void insert() {
		String qry = "INSERT INTO CAR_ORDER VALUES(" + oId + ", TO_DATE ('" + oDate + "','YYYY-MM-DD')," + qty + "," + centerId + "," + distributorId + ",'" + status + "'," + reqCenterId + ",'" + reqType + "'," + partId + ")";
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		db.runQuery(qry);
		db.closeConnection();
	}
	
	public void update() {
		String qry = "UPDATE CAR_ORDER set ODATE = TO_DATE ('" + oDate + "','YYYY-MM-DD') , QTY = " + qty + " , CENTERID = " + centerId + " , DISTRIBUTORID = " + distributorId + " , STATUS = '" + status + "' , REQCENTERID = " + reqCenterId + " , REQTYPE = '" + reqType + "' , PARTID = " + partId + " WHERE OID = " + oId;
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		db.runQuery(qry);
		db.closeConnection();
	}
	
	public void delete() {
		String qry = "DELETE FROM CAR_ORDER WHERE OID = " + oId;
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		db.runQuery(qry);
		db.closeConnection();
	}
}
