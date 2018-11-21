package views;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

import constants.CONSTANTS;
import constants.Utility;
import controllers.CustomerController;
import controllers.LoginController;
import dataAccessObjects.EmployeeDao;
import dataAccessObjects.ServiceCenterDao;
import databaseUtilities.DatabaseUtil;
import models.BaseService;
import models.Car;
import models.Center;
import models.Customer;
import models.Employee;
import models.Fault;
import models.Invoice;
import models.Login;
import models.Repair;
import oracle.jdbc.Const;

public class LoginView {
	private static Scanner console = new Scanner(System.in);
	private static LoginController controller;
	
	public static LoginController getController() {
		return controller;
	}

	public void setController(LoginController controller) {
		LoginView.controller = controller;
	}

	public static String displayMainMenu() {
		new DatabaseUtil().closeConnection2();
		int choice=0;
		System.out.println("--------WELCOME-------");
		System.out.println("1. Login");
		System.out.println("2. Sign Up");
		System.out.println("3. Exit");
		
		while(true) {
			try {
				
				
				System.out.print(">");
				
				choice = Integer.parseInt(console.nextLine());
				if( choice >3 || choice <1) {
					
					System.out.println("< Error: Choice not correct, Try again ");
					
				}
				else {
					break;
				}
		     
			}catch(Exception e) {
				
				 System.out.println("< Error: Choice not correct, Try again ");
				 console.reset();
			}
	            
	        }
		
	
		return CONSTANTS.LOGIN_MAIN_MENU+choice;
	}

	public String viewLoginPage() {
		int choice=0;
		System.out.println("--------Login-------");
		Login login=new Login();
		String input="";
		while(input.equals("")) {
			
			System.out.println("Enter Cancel to go back");
			System.out.println("A. User ID ");
			
			input=(console.nextLine()).trim();
			if (input.equalsIgnoreCase("cancel")) {
				return CONSTANTS.LOGIN_MAIN_MENU;
			}
			if (input.equals("")){
				System.out.println("< Error: User ID is mandatory ");
			}
		}
		login.setuId(input);
		input="";
		while(input.equals("")) {
			 
			System.out.println("B. Password ");
			
			input=(console.nextLine()).trim();
			if (input.equalsIgnoreCase("cancel")) {
				return CONSTANTS.LOGIN_MAIN_MENU;
			}
			if (input.equals("")){
				System.out.println("< Error: Password is mandatory ");
			}
		}
		login.setPass(input);
		
	
		System.out.println("1. Sign-In");
		System.out.println("2. Go Back");
		
		
		while(true) {
			try {
				
				
				System.out.print(">");
				
				choice = Integer.parseInt(console.nextLine());
				if( choice >2 || choice <1) {
					
					System.out.println("< Error: Choice not correct, Try again ");
					
				}
				else {
					break;
				}
		     
			}catch(Exception e) {
				
				 System.out.println("< Error: Choice not correct, Try again ");
				 console.reset();
			}
	            
	        }
		if(choice==1) {
			controller.verifyLogin(login);
			
		}
		
	
		return CONSTANTS.LOGIN_MAIN_MENU;	
	}

	public String viewSignPage() {

		int choice=0;
		System.out.println("--------Sign UP-------");
		Login login=new Login();
		String input="";
		System.out.println("Enter Cancel to go back");
		System.out.println("A. Email Address");
		
		while(input.equals("")) {
			
			
			input=(console.nextLine()).trim();
			if (input.equalsIgnoreCase("cancel")) {
				return CONSTANTS.LOGIN_MAIN_MENU;
			}
			if (input.equals("")){
				System.out.println("Error: Email is mandatory ");
			}
			if (!Utility.isValidEmailAddress(input)){
				System.out.println("Error: Email is not correct");
				input="";
			}
		}
		login.setEmail(input);
		input="";
		 
		System.out.println("B. Password ");
		
		while(input.equals("")) {
		
			input=(console.nextLine()).trim();
			if (input.equalsIgnoreCase("cancel")) {
				return CONSTANTS.LOGIN_MAIN_MENU;
			}
			if (input.equals("")){
				System.out.println("Error: Password is mandatory ");
			}
		}
		login.setPass(input);
		
		 
		System.out.println("C. Name ");
		
		input="";
		while(input.equals("")) {
		
			input=(console.nextLine()).trim();
			if (input.equalsIgnoreCase("cancel")) {
				return CONSTANTS.LOGIN_MAIN_MENU;
			}
			if (input.equals("")){
				System.out.println("Error: Name is mandatory ");
			}
		}
		login.setName(input);
		 
		System.out.println("D. Address ");
		
		input="";
		while(input.equals("")) {
		
			input=(console.nextLine()).trim();
			if (input.equalsIgnoreCase("cancel")) {
				return CONSTANTS.LOGIN_MAIN_MENU;
			}
			if (input.equals("")){
				System.out.println("Error: Address is mandatory ");
			}
		}
		login.setAddress(input);
		
		 
		System.out.println("E. Phone Number ");
		
		input="";
		while(input.equals("")) {
		
			input=(console.nextLine()).trim();
			if (input.equalsIgnoreCase("cancel")) {
				return CONSTANTS.LOGIN_MAIN_MENU;
			}
			if (input.equals("")){
				System.out.println("Error: Phone Number is mandatory ");
			}else if(!Utility.isValidPhoneNumber(input)) {
				System.out.println("Error: Phone Number not correct ");
				input="";
			}
		}
		login.setPhoneNumber(input);
		
		input="";
		
		
		ServiceCenterDao centerDao=new ServiceCenterDao();
		ArrayList<Center> centers=centerDao.getAllCenters();
		System.out.println("F. Service Center to register  (Enter 1-"+centers.size()+"): ");
				
		for (int i =0;i<centers.size();i++) {
			
			System.out.println((i+1) +". "+centers.get(i).getName());
		}
		
		while(true){
			try {
				input=console.nextLine().trim();
				if (input.equalsIgnoreCase("cancel")) {
					return CONSTANTS.LOGIN_MAIN_MENU;
				}
				if( Integer.parseInt(input) >centers.size()|| Integer.parseInt(input) <1) {
					
					System.out.println("< Error: Center selected not correct, Try again ");
					
				}
				else {
					break;
				}
		     
			}catch(Exception e) {
				
				 System.out.println("< Error: Center selected not correct, Enter the number. ");
				 console.reset();
			}
			
		}
		
		login.setCenterID(centers.get(Integer.parseInt(input)-1).getId());
		
		login.setRole("CUSTOMER");
		
		
		System.out.println("\n");
		System.out.println("1. Sign Up");
		System.out.println("2. Go Back");
		while(true) {
			try {
				
				
				choice = Integer.parseInt(console.nextLine());
				if( choice >2 || choice <1) {
					
					System.out.println("< Error: Choice not correct, Try again ");
					
				}
				else {
					break;
				}
		     
			}catch(Exception e) {
				
				 System.out.println("< Error: Choice not correct, Try again ");
				 console.reset();
			}
	            
	        }
		if (choice==1) {
			controller.signup(login);
			System.out.println("Sign up successful , Please login ");
			return CONSTANTS.LOGIN_PAGE;
		}
		
	
		return CONSTANTS.LOGIN_MAIN_MENU;
	}

	
	
}
