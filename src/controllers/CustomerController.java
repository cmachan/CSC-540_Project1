package controllers;

import java.util.ArrayList;

import constants.CONSTANTS;
import dataAccessObjects.CarDao;
import dataAccessObjects.CustomerDao;
import dataAccessObjects.RepairDao;
import models.Car;
import models.Customer;
import models.Repair;
import views.CustomerView;

public class CustomerController  {
	private CustomerView view;
	private static Customer customer;
	
	public CustomerController(CustomerView view){
		this.view=view;
	}
	public  void displayMainMenu() {
		controlFlow(CONSTANTS.CUSTOMER_MAIN_MENU);
		
	}
	
	public Customer getCustomerProfile(int id) {
		 CustomerDao cusDao=new CustomerDao();
		 Customer customer=cusDao.getCustomerProfile(id);
		 this.customer=customer;
		 return customer;
		 
	}
	private static void updateCustomerProfile() {
		 CustomerDao cusDao=new CustomerDao();
		 cusDao.updateCustomerProfile(customer);
		 
		 
	}
	public static void registerCar(Car car) {
		 CarDao carDao=new CarDao();
		 carDao.registerCar(car,customer);
		 
		 
	}
	
	
	public static  ArrayList<Repair> getServiceHistory() {
		 RepairDao repair=new RepairDao();
		 return repair.getServiceHistory(customer.getcId());
		
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
			getCustomerProfile(customer.getcId());
			choice=view.viewProfile(customer);
			break;
		case CONSTANTS.CUSTOMER_UPDATE_PROFILE:
				
			choice=view.updateProfile(customer);
			updateCustomerProfile();
			break;
		
		case CONSTANTS.CUSTOMER_REGISTER_CAR:
			choice=view.registerCar(customer);
			break;
		case CONSTANTS.CUSTOMER_SERVICE:
			
			choice=view.viewServiceMenu();
			break;	
		case CONSTANTS.CUSTOMER_SERVICE_HISTORY:
			ArrayList<Repair> repairs=getServiceHistory();
			choice=view.viewServiceHistory(repairs);
			break;	
			
		case CONSTANTS.CUSTOMER_SERVICE_SCHEDULE:
			
			choice=view.viewServiceSchedule(customer);
			break;	
				
		default:
			return;
		}
		
		controlFlow(choice);
		
	}

}
