package controllers;

/*
 * Author: Rahul Sethi
 */

import java.util.Scanner;

import constants.CONSTANTS;
import dataAccessObjects.EmployeeDao;
import models.Employee;
import views.EmployeeView;

public class EmployeeController {
	private EmployeeView view;
	private Employee emp;
	Scanner scan = new Scanner(System.in);
	ReceptionistController recController;
		
	int role; //1 for manager 2 for receptionist
	
	public EmployeeController(EmployeeView view) {
		this.view = view;
	}
	
	
	public ReceptionistController getRecController() {
		return recController;
	}


	public void setRecController(ReceptionistController recController) {
		this.recController = recController;
	}


	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	
	public Employee getEmployeeProfile(int id) {
		EmployeeDao empDao = new EmployeeDao();
		this.emp = empDao.getEmployeeProfile(id);
		return this.emp;
	}
	
	public void mainMenuControlFlow(String choice) {
		switch(choice) {
			case CONSTANTS.EMPLOYEE_VIEW_PROFILE:
				view.viewProfile(emp);
				break;
			case CONSTANTS.EMPLOYEE_UPDATE_PROFILE:
				view.displayUpdateProfileMenu(emp);
				break;
			default:
				System.out.println("Invalid Choice");
		}
		
		view.displayProfileMenu();
	}
	
	public void updateProfileControlFlow(int choice) {
		
		switch(choice) {
			case 1:
				System.out.print("Enter New Name: ");
				emp.seteName(scan.nextLine());
				break;
			case 2:
				System.out.print("Enter New Address: ");
				emp.setAddress(scan.nextLine());
				break;
			case 3:
				System.out.print("Enter New Email Address: ");
				emp.setEmail(scan.nextLine());
				break;
			case 4:
				System.out.print("Enter New Phone Number: ");
				emp.setPhone(Long.parseLong(scan.nextLine()));
				break;
			case 5:
				System.out.print("Enter New Password: ");
				
				break;
			default:
				System.out.println("Invalid Choice");
				view.displayUpdateProfileMenu(emp);
		}
		EmployeeDao empDao = new EmployeeDao();
		empDao.updateEmployee(emp);
		view.displayProfileMenu();
	}
	
}
