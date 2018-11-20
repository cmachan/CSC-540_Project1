package controllers;

import java.util.ArrayList;
import java.util.HashMap;

import constants.CONSTANTS;
import dataAccessObjects.LoginDao;
import models.Customer;
import models.Employee;
import models.Login;
import models.Repair;
import views.CustomerView;
import views.EmployeeView;
import views.LoginView;

public class LoginController {

	private LoginView view;
	
	public  void displayMainMenu() {
		controlFlow(CONSTANTS.LOGIN_MAIN_MENU);
		
	}
	public LoginController(LoginView view){
		this.view=view;
	}
	
	public void controlFlow(String choice) {
		System.out.println("\n\n");
		switch(choice) {
		case CONSTANTS.LOGIN_MAIN_MENU:
			choice=view.displayMainMenu();
			break;
		case CONSTANTS.LOGIN_PAGE:
			choice=view.viewLoginPage();
			break;
			
		case CONSTANTS.SIGN_UP_PAGE:
			choice=view.viewSignPage();
			break;
		case CONSTANTS.EXIT:
			return;
									
		default:
			return;
		}
		
		controlFlow(choice);
		
	}


	public void verifyLogin(Login login) {
		// TODO Auto-generated method stub
		LoginDao lDao=new LoginDao();
		if (lDao.getUserProfile(login)) {
			if (login.getRole().equals("MANAGER")) {
				
			}else if (login.getRole().equals("RECEPTIONIST")){
				
			}else {
				CustomerView customerView=new CustomerView();
				CustomerController customerController=new CustomerController(customerView);
				customerView.setController(customerController);
				Customer customer=customerController.getCustomerProfile(login.getuId());
				customerController.displayMainMenu();
				
			}
			
		}else {
			System.out.println("Login incorrect , Please try again");
			
			controlFlow(CONSTANTS.LOGIN_PAGE);
			}
		}
	
	


	public void signup(Login login) {
		LoginDao lDao=new LoginDao();
		if (login.getRole().equalsIgnoreCase("Manager") || login.getRole().equalsIgnoreCase("Receptionist")) {
			lDao.insertEmployee(login);
		}else {
			lDao.insertCustomer(login);
		}
		if (login.getuId()!=(null) && !login.getuId().equals("")) {
			lDao.insertLogin(login);
			
		}
		else{
		System.out.println("Entered data was incorrect , Please try again");
		
		controlFlow(CONSTANTS.SIGN_UP_PAGE);
		}
		// TODO Auto-generated method stub
		
	}
	
	
}
