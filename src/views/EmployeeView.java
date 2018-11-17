package views;

import java.util.Scanner;

import constants.CONSTANTS;
import controllers.EmployeeController;
import dataAccessObjects.Emp_ServiceDao;
import dataAccessObjects.PayrollDao;
import models.Car;
import models.Customer;
import models.Employee;

public class EmployeeView {
	private static Scanner console = new Scanner(System.in);
	private EmployeeController controller;
	
	public EmployeeController getController() {
		return controller;
	}
	public void setController(EmployeeController controller) {
		this.controller = controller;
	}
	
	public void viewProfile(Employee emp) {
		PayrollDao payRollDao;
		Emp_ServiceDao empServDao;
		try {
			payRollDao = new PayrollDao(emp.geteId());
			empServDao = new Emp_ServiceDao(emp.geteId());
			System.out.println("A. Employee ID: " + emp.geteId());
			System.out.println("B. Name: " + emp.geteName());
			System.out.println("C. Email Address: " + emp.getEmail());
			System.out.println("D. Phone Number: " + emp.getPhone());
			System.out.println("E. Service Center: " + emp.getServiceCenter());
			System.out.println("F. Role: " + empServDao.getRole());
			System.out.println("G. Start Date: " + payRollDao.getStartDate());
			System.out.println("H. Compensation: " + payRollDao.getWages());
			int freq = payRollDao.getFrequency();
			if(freq == 1)
				System.out.println("I. Compensation Frequency: Hourly");
			else if(freq == 30)
				System.out.println("I. Compensation Frequency: Monthly");
			
		} catch (Exception e) {
			if(e.getMessage().equals("Object Doesn't exists in the database"))
				System.out.println("Employee Profile Not Found");
			else
				e.printStackTrace();
		}

	}
	
	public void displayProfileMenu() {
			
			int choice;
			System.out.println();
			System.out.println("1. View Profile");
			System.out.println("2. Update Profile");
			System.out.println("3. Go Back");
			System.out.print("Enter Choice(1-3): ");
			choice = console.nextInt();
			
			if(choice != 3) 
				controller.mainMenuControlFlow(CONSTANTS.EMPLOYEE_PROFILE + choice);
			else {
				
				if(controller.getRole() == 1) {
					//Call Manager Main Menu
				}else if(controller.getRole() == 2) {
					controller.getRecController().getView().displayReceptionistMainMenu();
				}
			}
			
	}
	
	public void displayUpdateProfileMenu(Employee emp) {
		int choice;
		System.out.println();
		System.out.println("1. Name");
		System.out.println("2. Address");
		System.out.println("3. Email Address");
		System.out.println("4. Phone Number");
		System.out.println("5. Password");
		System.out.println("6. Go Back");
		System.out.print("Enter Choice(1-6): ");
		choice = console.nextInt();
		
		if(choice == 6) {
			displayProfileMenu();
		}else {
			controller.updateProfileControlFlow(choice);
		}
	}
	
}
