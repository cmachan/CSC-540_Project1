package dataAccessObjects;

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

	void populate() {
		
	}
	
	public CarTypeDao(int carTypeId) {
		this.carTypeId = carTypeId;
		populate();
	}
	
	public void insert() {
		
	}
	
	public void update() {
		
	}
	
	public void delete() {
		
	}
}
