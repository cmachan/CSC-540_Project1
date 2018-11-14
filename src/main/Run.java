package main;

import dataAccessObjects.Car_OrderDao;

public class Run {
	public static void main(String args[]) throws Exception {
		Car_OrderDao obj = new Car_OrderDao(1, "2018-01-01", 4 , 1, 1 , "Transit" , 1 , "R" , 1);
		obj.insert();
//		Car_OrderDao obj = new Car_OrderDao(1);
//		obj.setQty(7);
//		obj.update();
//		obj.delete();
	}
}
