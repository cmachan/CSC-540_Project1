package controllers;

import constants.CONSTANTS;
import dataAccessObjects.CustomerDao;
import models.Customer;
import views.CustomerView;

public class CustomerController  {
	private CustomerView view;
	private Customer customer;
	
	public CustomerController(CustomerView view){
		this.view=view;
	}
	public void displayMainMenu() {
		controlFlow(CONSTANTS.CUSTOMER_MAIN_MENU);
		
	}
	
	public Customer getCustomerProfile(int id) {
		 CustomerDao cusDao=new CustomerDao();
		 Customer customer=cusDao.getCustomerProfile(id);
		 this.customer=customer;
		 return customer;
		 
	}
	public void controlFlow(String choice) {
		System.out.println("\n\n");
		switch(choice) {
		case CONSTANTS.CUSTOMER_MAIN_MENU:
			choice=view.displayMainMenu();
			break;
		case CONSTANTS.CUSTOMER_PROFILE:
			choice=view.displayProfileMenu();
			break;
		case CONSTANTS.CUSTOMER_VIEW_PROFILE:
			
			choice=view.viewProfile(this.customer);
			break;
			
		default:
			return;
		}
		
		controlFlow(choice);
		
	}

}
