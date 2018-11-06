package dataAccessObjects;
import java.util.ArrayList;

import databaseUtilities.*;

public class CarTypeDao {
	int carTypeId;
	String make;
	String model;
	int year;
	
	public int getCarTypeId() {
		return carTypeId;
	}
	
	public void setCarTypeId(int carTypeId) {
		this.carTypeId = carTypeId;
	}
	
	public String getMake() {
		return make;
	}
	
	public void setMake(String make) {
		this.make = make;
	}
	
	public String getModel() {
		return model;
	}
	
	public void setModel(String model) {
		this.model = model;
	}
	
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	public CarTypeDao(int carTypeId, String make, String model, int year) {
		this.carTypeId = carTypeId;
		this.make = make;
		this.model = model;
		this.year = year;
	}

	void populate() throws Exception {
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		ArrayList<String> res = db.getAll("CARTYPE", "CARTYPEID", Integer.toString(this.carTypeId), true);
		
		if(res.size() == 0)
			throw new Exception("Object Doesn't exists in the database");
		
		int idx = 0;
		for(String s : res) {
			if(idx == 0)
				carTypeId = Integer.parseInt(s);
			else if(idx == 1)
				make = s;
			else if(idx == 2)
				model = s;
			else
				year = Integer.parseInt(s);
			
			idx++;
		}
		db.closeConnection();
	}
	
	public CarTypeDao(int carTypeId) throws Exception {
		this.carTypeId = carTypeId;
		populate();
	}
	
	public void insert() {
		String qry = "INSERT INTO CARTYPE VALUES(" + carTypeId + ",'" + make + "','" + model + "'," + year + ")";
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		db.runQuery(qry);
		db.closeConnection();
	}
	
	public void update() {
		String qry = "UPDATE CARTYPE set MAKE = '" + make + "' , MODEL = '" + model + "' , year = " + year + " WHERE CARTYPEID = " + carTypeId;
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		db.runQuery(qry);
		db.closeConnection();
	}
	
	public void delete() {
		String qry = "DELETE FROM CARTYPE WHERE CARTYPEID = " + carTypeId;
		DatabaseUtil db = new DatabaseUtil();
		db.establishConnection();
		db.runQuery(qry);
		db.closeConnection();
	}
}
