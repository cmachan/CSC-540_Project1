package views;

import java.util.InputMismatchException;
import java.util.Scanner;

import constants.CONSTANTS;
import models.Car;
import models.Customer;

public class CustomerView {
	private static Scanner console = new Scanner(System.in);

	public String displayMainMenu() {
		
		int choice=0;
		System.out.println("--------MENU-------");
		System.out.println("1. Profile");
		System.out.println("2. Register Car");
		System.out.println("3. Service");
		System.out.println("4.  Invoices");
		System.out.println("5. Logout");
		
		while(true) {
			try {
				
				
				System.out.print(">");
				
				choice = Integer.parseInt(console.nextLine());
				if( choice >5 || choice <1) {
					
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
		
	
		return CONSTANTS.CUSTOMER_MAIN_MENU+choice;
	}

	public String displayProfileMenu() {
		
		int choice=0;
		System.out.println("--------MENU-------");
		System.out.println("1. View Profile ");
		System.out.println("2. Update Profile ");
		System.out.println("3. Go Back ");
		
		while(true) {
			try {
				
				
				System.out.print(">");
				
				choice = Integer.parseInt(console.nextLine());
				if( choice >3|| choice <1) {
					
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
		
		
		return CONSTANTS.CUSTOMER_PROFILE+choice;
		
	}

	public String viewProfile(Customer customer) {
		
		int choice=0;
		
		System.out.println("--------Profile-------- ");
		System.out.println("Customer ID:  "+customer.getcId());
		System.out.println("Name:  "+customer.getcName());
		System.out.println("Address:  "+customer.getAddress());
		System.out.println("Email Address:  "+customer.getEmail());
		System.out.println("Phone Number:  "+customer.getPhone());
		System.out.println("Cars Owned by Customer:  ");
		if (customer.getCarsOwned().size()==0) {
			System.out.println("Customer doesn't own any car");
		}
		for (int i=0;i<customer.getCarsOwned().size();i++) {
			Car car=customer.getCarsOwned().get(i);
			
			System.out.println("Car: "+car.getLicensePlate()+" Make: "+car.getMake()+" Model: "+car.getModel()+" Year: "+car.getMakeYear()+" Date of last Service: "+car.getDateOfService()+" Last Service Type: "+car.getLastServiceType()+" Date of Purchase: "+car.getDateOfPurchase()+" Last recorded Mileage: "+car.getLastMileage());
			
		}
		System.out.println("\n");
		System.out.println("--------MENU-------");
		System.out.println("1. Go Back");
		while(true) {
			try {
				System.out.print(">");
				
				choice = Integer.parseInt(console.nextLine());
				if( choice!=1) {
					
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
		
		
		return CONSTANTS.CUSTOMER_PROFILE;
	}
}
