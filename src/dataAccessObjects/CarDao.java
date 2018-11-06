package dataAccessObjects;

import java.util.ArrayList;

import databaseUtilities.DatabaseUtil;

public class CarDao {
	int licensePlate;
	int carTypeId;
	int cId;
	String dateOfService;
	String lastServiceType;
	String dateOfPurchase;
	int lastMileage;
	public int getLicensePlate() {
		return licensePlate;
	}
	
	public void setLicensePlate(int licensePlate) {
		this.licensePlate = licensePlate;
	}
	
	public int getCarTypeId() {
		return carTypeId;
	}
	
	public void setCarTypeId(int carTypeId) {
		this.carTypeId = carTypeId;
	}
	
	public int getcId() {
		return cId;
	}
	
	public void setcId(int cId) {
		this.cId = cId;
	}
	
	public String getDateOfService() {
		return dateOfService;
	}
	
	public void setDateOfService(String dateOfService) {
		this.dateOfService = dateOfService;
	}
	
	public String getLastServiceType() {
		return lastServiceType;
	}
	
	public void setLastServiceType(String lastServiceType) {
		this.lastServiceType = lastServiceType;
	}
	
	public String getDateOfPurchase() {
		return dateOfPurchase;
	}
	
	public void setDateOfPurchase(String dateOfPurchase) {
		this.dateOfPurchase = dateOfPurchase;
	}
	
	public int getLastMileage() {
		return lastMileage;
	}
	
	public void setLastMileage(int lastMileage) {
		this.lastMileage = lastMileage;
	}
	
	void populate() throws Exception {
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		ArrayList<String> res = db.getAll("CAR", "LICENSEPLATE", Integer.toString(this.licensePlate), true);
		
		if(res.size() == 0)
			throw new Exception("Object Doesn't exists in the database");
		
		int idx = 0;
		for(String s : res) {
			if(idx == 0)
				licensePlate = Integer.parseInt(s);
			else if(idx == 1)
				carTypeId = Integer.parseInt(s);
			else if(idx == 2)
				cId = Integer.parseInt(s);
			else if(idx == 3)
				dateOfService = s;
			else if(idx == 4)
				lastServiceType = s;
			else if(idx == 5)
				dateOfPurchase = s;
			else
				lastMileage = Integer.parseInt(s);
			
			idx++;
		}
		db.closeConnection();
	}
	
	public CarDao(int licensePlate) throws Exception {
		this.licensePlate = licensePlate;
		populate();
	}
	
	public CarDao(int licensePlate, int carTypeId, int cId, String dateOfService, String lastServiceType,
			String dateOfPurchase, int lastMileage) {
		this.licensePlate = licensePlate;
		this.carTypeId = carTypeId;
		this.cId = cId;
		this.dateOfService = dateOfService;
		this.lastServiceType = lastServiceType;
		this.dateOfPurchase = dateOfPurchase;
		this.lastMileage = lastMileage;
	}
	
	
	public void insert() {
		String qry = "INSERT INTO CAR VALUES(" + licensePlate + "," + carTypeId + "," + cId + ",'" + dateOfService + "','" + lastServiceType + "','" + dateOfPurchase + "'," + lastMileage + ")";
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		db.runQuery(qry);
		db.closeConnection();
	}
	
	public void update() {
		String qry = "UPDATE CAR SET CARTYPEID = " +  carTypeId + " , CID = " +  cId + " , DATEOFSERVICE = '" +  dateOfService + "' , LASTSERVICETYPE = '" +  lastServiceType + "' , DATEOFPURCHASE = '" +  dateOfPurchase + "' , LASTMILEAGE = " +  lastMileage + " WHERE LICENSEPLATE = " + licensePlate;
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		db.runQuery(qry);
		db.closeConnection();
	}
	
	public void delete() {
		String qry = "DELETE FROM CAR WHERE LICENSEPLATE = " + licensePlate;
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		db.runQuery(qry);
		db.closeConnection();
	}
	
}
