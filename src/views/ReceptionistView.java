package views;

import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import constants.CONSTANTS;
import controllers.ReceptionistController;
import dataAccessObjects.CarDao;
import dataAccessObjects.CustomerDao;
import models.Car;
import models.Customer;


public class ReceptionistView {
	private static Scanner console = new Scanner(System.in);
	private ReceptionistController controller;
	
	public ReceptionistController getController() {
		return controller;
	}
	public void setController(ReceptionistController controller) {
		this.controller = controller;
	}
	
	public void displayReceptionistMainMenu() {
		int choice;
		System.out.println();
		System.out.println("1. Profile");
		System.out.println("2. View Customer Profile");
		System.out.println("3. Register Car");
		System.out.println("4. Service History");
		System.out.println("5. Schedule Service");
		System.out.println("6. Reschedule Service");
		System.out.println("7. Invoices");
		System.out.println("8. Daily Task-Update Inventory");
		System.out.println("9. Daily Task-Record Deliveries");
		System.out.println("10. Logout");
		System.out.print("Enter Choice(1-10): ");
		choice = console.nextInt();
		
		if(choice == 10) {
			
		}else {
			controller.mainFlowControl(CONSTANTS.RECPTIONIST_MAIN_MENU + choice);
		}
		
	}
	
	public void viewCusomerProfile(String email) {
		CustomerDao custDao = new CustomerDao();
		Customer customer = custDao.getCustomerProfileByEmail(email);
		if(customer != null) {
			System.out.println();
			System.out.println("Customer ID:  "+customer.getcId());
			System.out.println("Name:  "+customer.getcName());
			System.out.println("Address:  "+customer.getAddress());
			System.out.println("Email Address:  "+customer.getEmail());
			System.out.println("Phone Number:  "+customer.getPhone());
		}else {
			System.out.println("Customer Not Found");
		}
	}
	
	/* 	A. Service ID
		B. License Plate
		C. Service Type
		D. Mechanic Name
		E. Service Start Date/Time
		F. Service End Date/Time (expected or actual)
		G. Service Status(Pending,Ongoing, or Complete)
	 */
	
	public void  viewServiceHistory(String email) {
		CustomerDao custDao = new CustomerDao();
		Customer customer = custDao.getCustomerProfileByEmail(email);
		
		int cid = customer.getcId();
		
		CarDao carDao = new CarDao();
		ArrayList<Car> cars = carDao.getCarsOwnedByCustomer(cid);
		
		for(Car car : cars) {
			
		}
	}
	
	public void registerCar() {
		Car car = new Car();
		String email;
		System.out.println("---Car Registration---");
		System.out.println("Enter Cancel anytime to go back.");
		
		System.out.print("A. Customer email address: ");
		console.nextLine();
		email = console.nextLine();
		
		CustomerDao custDao = new CustomerDao();
		Customer customer = custDao.getCustomerProfileByEmail(email);
		car.setcId(customer.getcId());
		
		System.out.print("B. License Plate: ");
		car.setLicensePlate(console.nextLine());
		
		System.out.print("C. Purchase Date(YYYY-MM-DD): ");
		String dt[] = console.nextLine().split("-");
		Date date = new Date(Integer.parseInt(dt[0]),Integer.parseInt(dt[1]),Integer.parseInt(dt[2]));
		car.setDateOfPurchase(date);
		
		System.out.print("D. Make: ");
		car.setMake(console.nextLine());
		
		System.out.print("E. Model: ");
		car.setModel(console.nextLine());
		
		System.out.print("F. Year: ");
		car.setMakeYear(console.nextInt());
		
		System.out.print("G. Current Mileage: ");
		car.setLastMileage(console.nextInt());
		
		System.out.print("H. Last Service Date: ");
		console.nextLine();
		String dt1[] = console.nextLine().split("-");
		Date date1 = new Date(Integer.parseInt(dt1[0]),Integer.parseInt(dt1[1]),Integer.parseInt(dt1[2]));
		car.setDateOfService(date1);
		
		
		controller.registerCarControlFlow(car);
	}
	
}
