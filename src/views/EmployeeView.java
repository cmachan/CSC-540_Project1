package views;

import java.util.Scanner;

import dataAccessObjects.*;

public class EmployeeView {
	Scanner sc = null;
	int employeeId;
	
	public EmployeeView(int employeeId) {
		this.employeeId = employeeId;
		sc = new Scanner(System.in);
	}
	
	public void profile() {
		int choice;
		System.out.print("1. View Profile");
		System.out.print("2. View Profile");
		System.out.print("3. Go Back");
		System.out.println("Enter choice (1-3): ");
		
		choice = sc.nextInt();
		EmployeeView emp = new EmployeeView(employeeId);
		switch (choice) {
			case 1:  emp.viewProfile();	
			         break;
			case 2:  emp.updateProfile();
			         break;
			case 3:  
			         break;
			default: System.out.println("Invalid Entry");
					 profile();
			         break;
		}
	}

	public void viewProfile() {
		try {
			EmployeeDao empDao = new EmployeeDao(employeeId);
			Emp_ServiceDao empServDao = new Emp_ServiceDao(employeeId);
			PayrollDao payrollDao = new PayrollDao(employeeId);
			
			System.out.println("A. Employee ID: " + employeeId);
			System.out.println("B. Name: " + empDao.geteName());
			System.out.println("C. Email Address: " + empDao.getEmail());
			System.out.println("D. Phone Number: " + empDao.getPhone());
			System.out.println("E. Service Center: " + empServDao.getServiceCenterId());
			System.out.println("F. Role: " + empServDao.getRole());
			System.out.println("G. Start Date: " + payrollDao.getStartDate());
			System.out.println("H. Compensation: " + payrollDao.getWages());
			if(payrollDao.getFrequency() == 30)
				System.out.println("I. Compensation Frequency: Monthly");
			else if(payrollDao.getFrequency() == 1)
				System.out.println("I. Compensation Frequency: Hourly");
			
			System.out.println("1. Go Back");
			System.out.println("Enter Choice(1): ");
			int choice;
			
			choice = sc.nextInt();
			
			switch (choice) {
		        case 1:  	
		                 break;
		        default: System.out.println("Invalid Entry");
		        		 viewProfile();
		                 break;
			}
			
			
		}catch(Exception e) {
			
			System.out.println(e.getMessage());
		}

	}
	
	public void updateProfile() {
		
	}
}
