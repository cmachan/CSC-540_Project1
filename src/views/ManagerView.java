package views;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import constants.CONSTANTS;
import constants.Utility;
import controllers.CustomerController;
import controllers.ManagerController;
import dataAccessObjects.CustomerDao;
import dataAccessObjects.PayrollDao;
import dataAccessObjects.RepairDao;
import databaseUtilities.DatabaseUtil;
import models.BaseService;
import models.Car;
import models.CarType;
import models.Customer;
import models.Employee;
import models.Invoice;
import models.Repair;

public class ManagerView {
	private static Scanner console = new Scanner(System.in);
	private ManagerController controller;
	
	public ManagerController getController() {
		return controller;
	}
	public void setController(ManagerController controller) {
		this.controller = controller;
	}
	
	
	public void addEmployee() {
		int choice;
		
		int serviceCenterId = 1001; //HardCoded
		
		Employee emp = new Employee();
		
		emp.setServiceCenter(serviceCenterId);
		console.nextLine();
		System.out.print("Name: ");
		emp.seteName(console.nextLine());
		
		
		System.out.print("Address: ");
		emp.setAddress(console.nextLine());
		
		System.out.print("Email Address: ");
		
		String input="";
		while(input.equals("")) {
			input=(console.nextLine()).trim();
			
			if (input.equals("")){
				System.out.println("Error: Email Address empty. ");
			}else if(!Utility.isValidEmailAddress(input)) {
				System.out.println("Error: Email Address not correct ");
				input="";
			}
		}
		emp.setEmail(input);
		
		System.out.print("Phone: ");
		input="";
		while(input.equals("")) {
			input=(console.nextLine()).trim();
			
			if (input.equals("")){
				System.out.println("Error: Phone empty. ");
			}else if(!Utility.isValidPhoneNumber(input)) {
				System.out.println("Error: Phone not correct ");
				input="";
			}
		}
		emp.setPhone(Long.parseLong(input));
		System.out.print("Role: ");
		emp.setRole(console.nextLine());
		
		System.out.print("Start Date(YYYY-MM-DD): ");
		
		String dt = console.nextLine();
		
		LocalDate ld = LocalDate.of(Integer.parseInt(dt.split("-")[0]), Integer.parseInt(dt.split("-")[1]) , Integer.parseInt(dt.split("-")[2]));
		Date dt1 = java.sql.Date.valueOf(ld);
		emp.setsDate(dt1);
		
		
		System.out.print("Compensation: ");
		emp.setWage(console.nextDouble());
		
		controller.addEmployeeController(emp);
	}
	
	public void displayOrderMenu() {
		System.out.println("1. Order History"); 
		System.out.println("2. New Order"); 
		System.out.println("3. Go Back");
		System.out.print("Enter Choice(1-3): ");
		int choice = console.nextInt();
		
		if(choice == 3) {
			displayManagerMainMenu();
		}else {
			controller.orderController(choice);
		}
	}
	
	
	
	public void displayManagerMainMenu() {
		int choice;
		System.out.println();
		System.out.println("1. Profile");
		System.out.println("2. View Customer Profile");
		System.out.println("3. Add New Employees");
		System.out.println("4. Payroll");
		System.out.println("5. Inventory");
		System.out.println("6. Orders");
		System.out.println("7. Notifications");
		System.out.println("8. New Car Model");
		System.out.println("9. Car Service Details");
		System.out.println("10. Service History");
		System.out.println("11. Invoices");
		System.out.println("12. Logout");
		System.out.print("Enter Choice(1-12): ");
		choice = console.nextInt();
		
		if(choice == 12) {
			LoginView.displayMainMenu();
		}else {
			controller.mainFlowControl(CONSTANTS.MANAGER_MAIN_MENU + choice);
		}
		
	}
	
	public void viewServiceInvoice(HashMap<String, Repair> completedservices) {
		int choice=0;
		
		System.out.println("--------Invoice-------- ");
		
		
		if (completedservices.keySet().size()==0) {
			System.out.println("No Service found");
		}
		  for (Map.Entry<String, Repair> me : completedservices.entrySet()) {
			  
			Repair service=(Repair) me.getValue();
			
			RepairDao rDao=new RepairDao();
			rDao.getServiceDetail(service);
			
			System.out.println(" Service ID: "+service.getInvoiceNumber());
			System.out.println(" Customer Name:"+service.getCname());
			
			System.out.println(" Service Start Date/Time: "+service.getRdate()+ " "+ service.getStartTime());
			System.out.println(" Service End Date/Time: "+service.getRdate()+" "+ service.getEndTime	());
			System.out.println(" License Plate: "+service.getCar().getLicensePlate());
			System.out.println(" Service Type: "+service.getServiceType());
			System.out.println(" Mechanic Name: "+ service.getMechanicName());
			System.out.println(" Services: ");
			int labourwages=0;
			int count=0;
			for (int i=0;i<service.getBaseServices().size();i++) {
				BaseService bs=service.getBaseServices().get(i);
				System.out.println("	"+bs.getName()+"");
				boolean lb=false;
				boolean war=false;
				for (int j=0;j<bs.getInvoice().size();j++) {
					Invoice inv=bs.getInvoice().get(j);
					String warr="";
					if (inv.isWarranty()) {
						war=true;
						warr=" Part is with the Warranty"; 		
					}
				System.out.println("		Part Name:"+inv.getPartName()+" Cost:"+inv.getCost()+warr);
				

				if (inv.isFirst()) {
					lb=true;
				}
				
				
				}
			
				
				if (lb==true) {
					
					System.out.println("		Service was first time provided so No labour charge"); 		
				}
				else {
					if (!war) {
						count+=1;
						labourwages+=bs.getLabourCharge();
						System.out.println("		Labour Charge per hour:"+bs.getLabourCharge()); 
						System.out.println("		Labour hours:"+bs.getHour()); 
					}
				}
				
				
			}
			labourwages=service.getBaseServices().size()==0?0: labourwages/count;
			float hours=(float)(service.getEndTime().getTime()-service.getStartTime().getTime())/3600000;
			System.out.println(" Total labour hours : "+hours );
			System.out.println(" Labour Wages per hour : "+labourwages );
			
			System.out.println(" Total Service Cost: "+ service.getFees());
			
			
			System.out.println("----------------------------------------");
			
			
		}
		System.out.println("\n");
		System.out.println("--------MENU-------");
		System.out.println("1. Go Back");
		while(true) {
			try {
				
				console.nextLine();
				choice = Integer.parseInt(console.nextLine());
				if( choice<1 || choice>1) {
					
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
	
		
		
		
		displayManagerMainMenu();
		  }
	
	
	public void viewCusomerProfile(String email) {
		CustomerDao custDao = new CustomerDao();
		Customer customer = custDao.getCustomerProfileByEmail(email);
		if(customer != null) {
			System.out.println();
			System.out.println("Customer ID:  "+customer.getcId());
			System.out.println("Name:  "+customer.getcName());
			System.out.println("Address:  "+customer.getAddress());
			System.out.println("Email Address:  "+customer.getEmail());
			System.out.println("Phone Number:  "+customer.getPhone());
			controller.goBackController();
		}else {
			System.out.println("Customer Not Found");
		}
	}
	public void viewServiceHistory(ArrayList<Repair> repairs) {
		int choice=0;
		
		System.out.println("--------Service History-------- ");
		
		
		if (repairs.size()==0) {
			System.out.println("No Service found");
		}
		for (int i=0;i<repairs.size();i++) {
			Repair repair=repairs.get(i);
			
			System.out.println("Service ID: "+repair.getInvoiceNumber()+", Customer Name"+ repair.getCname()+", License Plate: "+repair.getCar().getLicensePlate()+", Service Type: "+repair.getServiceType()+", Mechanic Name: "+
			repair.getMechanicName()+", Service Start Date/Time: "+repair.getRdate()+" "+repair.getStartTime()+", Service End Date/Time: "+repair.getRdate()+" "+ repair.getEndTime	()+
			", Service Status: "+repair.getStatus());
			
		}
		System.out.println("\n");
		System.out.println("--------MENU-------");
		System.out.println("1. Go Back");
		while(true) {
			try {
				console.nextLine();
				
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
		

		displayManagerMainMenu();
	}
	public void viewCarService(ArrayList<CarType> cars) {
		int choice=0;
		
		System.out.println("--------Car Service Details-------- ");
		
		
		if (cars.size()==0) {
			System.out.println("No detail found");
		}
		int count=0;
		for (int i=0;i<cars.size();i++) {
			CarType car=cars.get(i);
			
			count+=1;
			
			if (count==1) {
				
				System.out.println("A. Make:"+car.getMake());
				System.out.println("B. Model:"+car.getModel());
			}
			
				if (car.getType().equals("SERVICE A")) {
					System.out.println("C. SERVICE A:");
					System.out.println("	a.  Miles:"+car.getMiles());
					System.out.println("	b.  List of Basic Services:");
					for (int j=0;j<car.getBaseService().size();j++) {
						System.out.println("		Service:"+car.getBaseService().get(j).getName()+"Service ID:"+ car.getBaseService().get(j).getbId());
					}
					
				}
				if (car.getType().equals("SERVICE B")) {
					System.out.println("D. SERVICE B:");
					System.out.println("	a.  Miles:"+car.getMiles());
					System.out.println("	b.  List of Basic Services:");
					for (int j=0;j<car.getBaseService().size();j++) {
						System.out.println("		Service:"+car.getBaseService().get(j).getName()+"Service ID:"+ car.getBaseService().get(j).getbId());
					}
					
				}
				if (car.getType().equals("SERVICE C") ){
					System.out.println("E. SERVICE C:");
					System.out.println("	a.  Miles:"+car.getMiles());
					System.out.println("	b.  List of Basic Services:");
					for (int j=0;j<car.getBaseService().size();j++) {
						System.out.println("		Service:"+car.getBaseService().get(j).getName()+"Service ID:"+ car.getBaseService().get(j).getbId());
					}
					
				}
				System.out.println("---------------------------------------------");
				if (count==3) {
					count=0;
				}
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
		

		displayManagerMainMenu();
	}
}
