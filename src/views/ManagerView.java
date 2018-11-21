package views;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import constants.CONSTANTS;
import controllers.ManagerController;
import dataAccessObjects.CustomerDao;
import dataAccessObjects.PayrollDao;
import databaseUtilities.DatabaseUtil;
import models.Car;
import models.Customer;
import models.Employee;

public class ManagerView {
	private static Scanner console = new Scanner(System.in);
	private ManagerController controller;
	
	public ManagerController getController() {
		return controller;
	}
	public void setController(ManagerController controller) {
		this.controller = controller;
	}
	
	
	public void addEmployee() {
		int choice;
		
		int serviceCenterId = 1001; //HardCoded
		
		Employee emp = new Employee();
		
		emp.setServiceCenter(serviceCenterId);
		console.nextLine();
		System.out.print("Name: ");
		emp.seteName(console.nextLine());
		
		System.out.print("Address: ");
		emp.setAddress(console.nextLine());
		
		System.out.print("Email Address: ");
		emp.setEmail(console.nextLine());
		
		System.out.print("Phone: ");
		emp.setPhone(console.nextLong());
		
		console.nextLine();
		System.out.print("Role: ");
		emp.setRole(console.nextLine());
		
		System.out.print("Start Date(YYYY-MM-DD): ");
		try {
			emp.setsDate(new SimpleDateFormat("yyyy-MM-dd").parse(console.nextLine()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.print("Compensation: ");
		emp.setWage(console.nextDouble());
		
		controller.addEmployeeController(emp);
	}
	
	public void displayOrderMenu() {
		System.out.println("1. Order History"); 
		System.out.println("2. New Order"); 
		System.out.println("3. Go Back");
		System.out.print("Enter Choice(1-3): ");
		int choice = console.nextInt();
		
		if(choice == 3) {
			displayManagerMainMenu();
		}else {
			controller.orderController(choice);
		}
	}
	
	
	
	public void displayManagerMainMenu() {
		int choice;
		System.out.println();
		System.out.println("1. Profile");
		System.out.println("2. View Customer Profile");
		System.out.println("3. Add New Employees");
		System.out.println("4. Payroll");
		System.out.println("5. Inventory");
		System.out.println("6. Orders");
		System.out.println("7. Notifications");
		System.out.println("8. New Car Model");
		System.out.println("9. Car Service Details");
		System.out.println("10. Service History");
		System.out.println("11. Invoices");
		System.out.println("12. Logout");
		System.out.print("Enter Choice(1-12): ");
		choice = console.nextInt();
		
		if(choice == 12) {
			LoginView.displayMainMenu();
		}else {
			controller.mainFlowControl(CONSTANTS.MANAGER_MAIN_MENU + choice);
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
			controller.goBackController();
		}else {
			System.out.println("Customer Not Found");
		}
	}
}
