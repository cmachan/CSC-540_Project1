package main;

import controllers.CustomerController;
import dataAccessObjects.CustomerDao;
import models.Customer;
import views.CustomerView;

public class CustomerLogin {
	public static void main(String args[]) throws Exception {
		CustomerView customerView=new CustomerView();
		CustomerController customerController=new CustomerController(customerView);
		customerView.setController(customerController);
		Customer customer=customerController.getCustomerProfile(1);
		customer.setCenterId(1001);
		if (customer!=null) {
			customerController.displayMainMenu();
		}
		
		
		
		
	}
}
