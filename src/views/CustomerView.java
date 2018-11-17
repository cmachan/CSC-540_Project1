package views;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

import constants.CONSTANTS;
import controllers.CustomerController;
import dataAccessObjects.EmployeeDao;
import models.Car;
import models.Customer;
import models.Employee;
import models.Repair;
import oracle.jdbc.Const;

public class CustomerView {
	private static Scanner console = new Scanner(System.in);
	private static CustomerController controller;
	
	public static CustomerController getController() {
		return controller;
	}

	public void setController(CustomerController controller) {
		CustomerView.controller = controller;
	}

	public String displayMainMenu() {
		
		int choice=0;
		System.out.println("--------MENU-------");
		System.out.println("1. Profile");
		System.out.println("2. Register Car");
		System.out.println("3. Service");
		System.out.println("4. Invoices");
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
		
		if (choice==3) {
			return CONSTANTS.CUSTOMER_MAIN_MENU;
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

	public String updateProfile(Customer customer) {
		int choice=0;
		
		boolean flag=true;
		while(flag) {
			try {
				System.out.println("--------MENU-------");
				System.out.println("1. Name");
				System.out.println("2. Address ");
				System.out.println("3. Phone Number ");
				System.out.println("4. Password ");
				System.out.println("5. Go Back ");
				
				System.out.print("> Choose 1-5 to enter new value: ");
				System.out.println(">");
				try {
				choice = Integer.parseInt(console.nextLine());
				}
				catch(NumberFormatException e) {
					System.out.println("< Error: Enter Number only ");
				}
				if( choice >5|| choice <1) {
					
					System.out.println("< Error: Choice not correct, Try again ");
					
				}
				else {
					switch(choice) {
					case 1: 
						System.out.println("> Enter new Name:  ");
						System.out.println(">");
						
						customer.setcName((console.nextLine().trim()));
						break;
					case 2: 
						System.out.println("> Enter new Address:  ");
						System.out.println(">");
						customer.setAddress((console.nextLine().trim()));
						break;	
					case 3: 
						System.out.println("> Enter new Phone Number:  ");
						System.out.println(">");
						try {
						customer.setPhone(Long.parseLong(console.nextLine().trim()));
						}catch(NumberFormatException e) {
							System.out.println("< Error: Enter Number only ");
						}
						break;	
						
					case 4: 
						System.out.println("> Enter new Password:  ");
						System.out.println(">");
						customer.setPassword((console.nextLine().trim()));
						break;	
					case 5: 
						flag=false;
						break;	
					}
					
				}
		     	
				
		     
			}catch(Exception e) {
				
				 System.out.println("< Error: Choice not correct, Try again ");
				 console.reset();
			}
	            
	        }
		
		
		return CONSTANTS.CUSTOMER_PROFILE;
		
	}

	public String registerCar(Customer customer) {
		int choice=0;
		String input="";
		Car car=new Car();
		car.setcId(customer.getcId());
		System.out.println("--------Register Car-------");
		while(input.equals("")) {
			System.out.println("A. Enter Licence Plate ");
			System.out.print(">");
			input=(console.nextLine()).trim();
			if (input.equals("")){
				System.out.println("< Error: Licence plate is mandatory ");
			}
		}
		car.setLicensePlate(input);
		input="";
		Date date1=null;
		while(input.equals("")) {
			System.out.println("B. Enter Purchase date in format-MM/dd/yyyy ");
			System.out.print(">");
			input=(console.nextLine()).trim();
			if (input.equals("")){
				System.out.println("< Error: Purchase date is mandatory ");
			}
			
			try {
				date1=new SimpleDateFormat("MM/dd/yyyy").parse(input);  
			   
				}
				catch(ParseException e) {
					System.out.println("< Error: Date not in correct format ");
					input="";	
				}
		
		}
		
		car.setDateOfPurchase(date1);
		
		input="";
		
		while(input.equals("")) {
			System.out.println("C. Enter Make of the car ");
			System.out.print(">");
			input=(console.nextLine()).trim();
			if (input.equals("")){
				System.out.println("< Error: Make is mandatory ");
			}
			
		}
		
		car.setMake(input);
		
		input="";
		
		while(input.equals("")) {
			System.out.println("D. Enter Model of the car ");
			System.out.print(">");
			input=(console.nextLine()).trim();
			if (input.equals("")){
				System.out.println("< Error: Model is mandatory ");
			}
			
		}
		
		car.setModel(input);
		
		
		input="";
		int year=0;
		while(input.equals("")) {
			System.out.println("E. Enter Year of the car ");
			System.out.print(">");
			input=(console.nextLine()).trim();
			if (input.equals("")){
				System.out.println("< Error: Year is mandatory ");
			}
			try {
			 year=Integer.parseInt(input);
			
			if (input.length()!=4 || year<1920 || year>Calendar.getInstance().get(Calendar.YEAR) ) {
				System.out.println("< Error: Year is not correct ");
				input="";
				
			}
			}catch(NumberFormatException e) {
				System.out.println("< Error: Year is not correct, should be a number. ");
			}
			
			
		}
		
		car.setMakeYear(year);
		
		
		input="";
		int mileage=0;
		while(input.equals("")) {
			System.out.println("F. Current mileage ");
			System.out.print(">");
			input=(console.nextLine()).trim();
			if (input.equals("")){
				System.out.println("< Error: mileage is mandatory ");
			}
			try {
				mileage=Integer.parseInt(input);
			}catch(NumberFormatException e) {
				System.out.println("< Error: mileage is not correct, should be a number. ");
			}
			
			
		}
		
		car.setLastMileage(mileage);
		
		
		
		input="";
		
			System.out.println("G. Last Service Date ");
			System.out.print(">");
			input=(console.nextLine()).trim();
			if (!input.equals(""))  {

			try {
				date1=new SimpleDateFormat("MM/dd/yyyy").parse(input);  
			   
				}
				catch(ParseException e) {
					System.out.println("< Error: Date not in correct format ");
					input="";	
				}
			}
			
		
		
		car.setDateOfService(input.equals("")?null:date1);
		
		
		
		
		System.out.println("--------MENU-------");
		System.out.println("1. Register ");
		System.out.println("2. Cancel ");
		
		while(true){
			try {
				
				
				System.out.print(">");
				
				choice = Integer.parseInt(console.nextLine());
				if( choice >2|| choice <1) {
					
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
			controller.registerCar(car);
			System.out.println("Car Registered");
			
		}else {
			System.out.println("Registeration canceled");
		}
		
		
		return CONSTANTS.CUSTOMER_MAIN_MENU;
		
	}

	public String viewServiceMenu() {
		int choice=0;
		System.out.println("--------MENU-------");
		System.out.println("1. View Service History ");
		System.out.println("2. Schedule Service ");
		System.out.println("3. Reschedule Service ");
		System.out.println("4. Go Back ");
		while(true) {
			try {
				
				
				System.out.print(">Enter Choice:");
				
				choice = Integer.parseInt(console.nextLine());
				if( choice >4|| choice <1) {
					
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
		
		if (choice==4) {
			return CONSTANTS.CUSTOMER_MAIN_MENU;
		}
		return CONSTANTS.CUSTOMER_SERVICE+choice;
		
	}

	public String viewServiceHistory(ArrayList<Repair> repairs) {
		int choice=0;
		
		System.out.println("--------Service History-------- ");
		
		
		if (repairs.size()==0) {
			System.out.println("No Service found");
		}
		for (int i=0;i<repairs.size();i++) {
			Repair repair=repairs.get(i);
			
			System.out.println("Service ID: "+repair.getInvoiceNumber()+" License Plate: "+repair.getCar().getLicensePlate()+" Service Type: "+repair.getServiceType()+" Mechanic Name: "+
			repair.getMechanicName()+" Service Start Date/Time: "+repair.getRdate()+" "+repair.getStartTime()+" Service End Date/Time: "+repair.getRdate()+" "+ repair.getEndTime	()+
			" Service Status: "+repair.getStatus());
			
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
		
		
		return CONSTANTS.CUSTOMER_SERVICE;
	}

	public String viewServiceSchedule(Customer customer) {
		int choice=0;
		String input="";
		Repair service=new Repair();
		service.setCenterId(1);
		Car car=new Car();
		service.setcId(customer.getcId());
		System.out.println("--------Schedule the service -------");
		while(input.equals("")) {
			System.out.println("A. Enter Licence Plate ");
			System.out.print(">");
			input=(console.nextLine()).trim();
			if (input.equals("")){
				System.out.println("< Error: Licence plate is mandatory ");
			}
		}
		car.setLicensePlate(input);
		car.setcId(customer.getcId());
		input="";
		int mileage=0;
		while(input.equals("")) {
			System.out.println("B. Enter current mileage of the car ");
			System.out.print(">");
			input=(console.nextLine()).trim();
			if (input.equals("")){
				System.out.println("< Error: current mileage is mandatory ");
			}
			try {
				mileage=Integer.parseInt(input);
			}catch(NumberFormatException e) {
				System.out.println("< Error: mileage is not correct, should be a number. ");
				input="";
			}
			
			
		}
		
		car.setNewMileage(mileage);
		service.setCar(car);
		input="";
		EmployeeDao empDao=new EmployeeDao();
		ArrayList<Employee> mechanics=empDao.getAllMechanic();
		System.out.println("G. Select Mechanic (Enter 1-"+mechanics.size()+"): ");
		for (int i =0;i<mechanics.size();i++) {
			
			System.out.println((i+1) +". "+mechanics.get(i).geteName());
		}
		int mechanic=0;
		while(true){
			try {
				
				
				System.out.print(">");
				input=console.nextLine().trim();
				if (input.equals("")){
					break;
				}
				mechanic = Integer.parseInt(input);
				if( mechanic >mechanics.size()|| mechanic <1) {
					
					System.out.println("< Error: Mechanic selected not correct, Try again ");
					
				}
				else {
					break;
				}
		     
			}catch(Exception e) {
				
				 System.out.println("< Error: Mechanic selected not correct, Enter the number. ");
				 console.reset();
			}
			
		}
		if( !input.equals("")){
			service.setMechanicName(mechanics.get(mechanic-1).geteName());
			service.setMechanicId(mechanics.get(mechanic-1).geteId());
		}
		
		
		
		
		System.out.println("--------MENU-------");
		System.out.println("1. Schedule Maintenance ");
		System.out.println("2. Schedule Repair ");
		
		System.out.println("3. Go Back ");
		
		while(true){
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
		
		
		if (choice==1) {
			viewScheduleMaintenance(service,customer);
			
		}else if(choice==2) {
			viewScheduleRepair(service,customer);
			
			
		}
		
		
		return CONSTANTS.CUSTOMER_SERVICE;
	}

	private void viewScheduleRepair(Repair service, Customer customer) {

		
		
	}

	private void viewScheduleMaintenance(Repair service, Customer customer) {
		System.out.println("--------Schedule Maintenance-------");
		System.out.println("1. Find Service Date ");
		System.out.println("2. Go Back");
		int choice=0;
		
		while(true){
			try {
				
				
				System.out.print(">");
				
				choice = Integer.parseInt(console.nextLine());
				if( choice >2|| choice <1) {
					
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
			if (controller.validateCar(service)) {
				ArrayList<Employee> mechanics=controller.findDates(service);
				viewScheduleMaintenance2(service,mechanics,customer);
			}
			
			
		}else if(choice==2) {
			viewScheduleRepair(service,customer);
			
			
		}
		
	}

	private void viewScheduleMaintenance2(Repair service, ArrayList<Employee> mechanics, Customer customer) {
		// TODO Auto-generated method stub
		System.out.println("--------Schedule Maintenance Page2-------");
		for (int i=0;i<mechanics.size();i++) {
			System.out.println((i+1)+" Date:  "+mechanics.get(i).getStartTime()+" - "+mechanics.get(i).getEndTime());
		}
		
		System.out.println("-------Menu------");
		System.out.println("1. Schedule on Date ");
		System.out.println("2. Go Back");
		int choice=0;
		
		while(true){
			try {
				
				
				System.out.print(">");
				
				choice = Integer.parseInt(console.nextLine());
				if( choice >2|| choice <1) {
					
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
			int dateSelected=0;
			
			while(true){
				try {
					
					
					System.out.print(">Choose the date(1/2)");
					
					dateSelected = Integer.parseInt(console.nextLine());
					if( dateSelected >2|| dateSelected <1) {
						
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
			
			controller.saveMaintenance(service,mechanics.get(dateSelected-1));
			viewServiceSchedule(customer);
			
		}else if(choice==2) {
			viewScheduleMaintenance(service,customer);
			
			
		}
	}
}
