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
import views.ManagerView;
import views.ReceptionistView;

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
				
				ManagerView manView = new ManagerView();
				ManagerController manController = new ManagerController(manView);
				manView.setController(manController);
				EmployeeView empView = new EmployeeView();
				EmployeeController empController = new EmployeeController(empView);
				empView.setController(empController);
				
				
				ManagerController.setManager(empController.getEmployeeProfile(Integer.parseInt(login.getuId())));
				manView.displayManagerMainMenu();
				
				
				
			}else if (login.getRole().equals("RECEPTIONIST")){
				ReceptionistView recView = new ReceptionistView();
				ReceptionistController recController = new ReceptionistController(recView);
				recView.setController(recController);
				recController.setReceptionist(login);
				recView.displayReceptionistMainMenu();
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
		boolean flag=false;
		
		flag=lDao.insertCustomer(login);
		
		if (flag==true && login.getuId()!=(null) && !login.getuId().equals("")) {
			lDao.insertLogin(login);
			
		}
		else{
		System.out.println("Entered data was incorrect , Please try again");
		
		controlFlow(CONSTANTS.SIGN_UP_PAGE);
		}
		// TODO Auto-generated method stub
		
	}
	
	
}
