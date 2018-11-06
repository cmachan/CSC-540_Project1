package main;

import dataAccessObjects.CustomerDao;
import dataAccessObjects.Mech_HoursDao;

public class Run {
	public static void main(String args[]) throws Exception {
		CustomerDao obj = new CustomerDao(1, "Utkarsh", "utk@ncsu.edu", Long.parseLong("9193211234"), "Avent Ferry");
//		Mech_HoursDao obj = new Mech_HoursDao(1);
		obj.insert();
//		obj.setHours(15);
//		obj.update();
//		obj.delete();
	}
}
