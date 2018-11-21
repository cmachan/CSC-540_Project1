package main;

import controllers.CustomerController;
import controllers.EmployeeController;
import controllers.LoginController;
import controllers.ReceptionistController;
import dataAccessObjects.CustomerDao;
import models.Customer;
import models.Employee;
import views.CustomerView;
import views.EmployeeView;
import views.LoginView;
import views.ReceptionistView;

public class CustomerLogin {
	public static void main(String args[]) throws Exception {
		
		LoginView lview = new LoginView();

		LoginController login=new LoginController(lview);
		lview.setController(login);
		login.displayMainMenu();
		
		
//		EmployeeView empView = new EmployeeView();
//		EmployeeController empController = new EmployeeController(empView);
//		empView.setController(empController);
//		empController.setRole(1);
//		Employee emp = empController.getEmployeeProfile(950932130);
//		if (emp != null) {
//			empView.displayProfileMenu();
//		}
		
		
	//	ReceptionistView recView = new ReceptionistView();
	//	ReceptionistController recController = new ReceptionistController(recView);
	//	recView.setController(recController);
	//	recView.displayReceptionistMainMenu();
		
	}
}
