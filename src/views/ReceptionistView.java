package views;

import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import constants.CONSTANTS;
import controllers.CustomerController;
import controllers.ReceptionistController;
import dataAccessObjects.CarDao;
import dataAccessObjects.CustomerDao;
import dataAccessObjects.Emp_ServiceDao;
import dataAccessObjects.PayrollDao;
import models.Car;
import models.Customer;
import models.Employee;


public class ReceptionistView {
	private static Scanner console = new Scanner(System.in);
	private ReceptionistController controller;
	
	public ReceptionistController getController() {
		return controller;
	}
	public void setController(ReceptionistController controller) {
		this.controller = controller;
	}
	
	public void displayReceptionistMainMenu() {
		int choice;
		System.out.println();
		System.out.println("------------Menu-------------");
		System.out.println("1. Profile");
		System.out.println("2. View Customer Profile");
		System.out.println("3. Register Car");
		System.out.println("4. Service History");
		System.out.println("5. Schedule Service");
		System.out.println("6. Reschedule Service");
		System.out.println("7. Invoices");
		System.out.println("8. Daily Task-Update Inventory");
		System.out.println("9. Daily Task-Record Deliveries");
		System.out.println("10. Logout");
		System.out.print("Enter Choice(1-10): ");
		choice = console.nextInt();
		
		if(choice == 10) {
				
			LoginView.displayMainMenu();
			
		}else {
			controller.mainFlowControl(CONSTANTS.RECPTIONIST_MAIN_MENU + choice);
		}
		
	}
	
	public void viewCusomerProfile(String email) {
		CustomerDao custDao = new CustomerDao();
		CustomerView vi=new CustomerView();
		Customer customer = CustomerController.getCustomerProfile(email);
		
		String choice=vi.viewProfile(customer);
		displayReceptionistMainMenu();
	}
	
	/* 	A. Service ID
		B. License Plate
		C. Service Type
		D. Mechanic Name
		E. Service Start Date/Time
		F. Service End Date/Time (expected or actual)
		G. Service Status(Pending,Ongoing, or Complete)
	 */
	
	public void  viewServiceHistory(String email) {
		CustomerDao custDao = new CustomerDao();
		Customer customer = custDao.getCustomerProfileByEmail(email);
		
		int cid = customer.getcId();
		
		CarDao carDao = new CarDao();
		ArrayList<Car> cars = carDao.getCarsOwnedByCustomer(cid);
		
		for(Car car : cars) {
			
		}
	}
	
	public void registerCar() {
		Car car = new Car();
		String email;
		System.out.println("---Car Registration---");
		System.out.println("Enter Cancel anytime to go back.");
		
		System.out.print("A. Customer email address: ");
		console.nextLine();
		email = console.nextLine();
		
		CustomerDao custDao = new CustomerDao();
		Customer customer = custDao.getCustomerProfileByEmail(email);
		CustomerView view=new CustomerView();
		CustomerController con=new CustomerController(view);
		view.setController(con);
		con.setCustomer(customer);
		
	
		view.registerCar(customer);
		/*car.setcId(customer.getcId());
		
		System.out.print("B. License Plate: ");
		car.setLicensePlate(console.nextLine());
		
		System.out.print("C. Purchase Date(YYYY-MM-DD): ");
		String dt[] = console.nextLine().split("-");
		Date date = new Date(Integer.parseInt(dt[0]),Integer.parseInt(dt[1]),Integer.parseInt(dt[2]));
		car.setDateOfPurchase(date);
		
		System.out.print("D. Make: ");
		car.setMake(console.nextLine());
		
		System.out.print("E. Model: ");
		car.setModel(console.nextLine());
		
		System.out.print("F. Year: ");
		car.setMakeYear(console.nextInt());
		
		System.out.print("G. Current Mileage: ");
		car.setLastMileage(console.nextInt());
		
		System.out.print("H. Last Service Date: ");
		console.nextLine();
		String dt1[] = console.nextLine().split("-");
		Date date1 = new Date(Integer.parseInt(dt1[0]),Integer.parseInt(dt1[1]),Integer.parseInt(dt1[2]));
		car.setDateOfService(date1);
		
		
		controller.registerCarControlFlow(car);*/
		displayReceptionistMainMenu();
	}
	
	public void recordDelivery() {
		System.out.print("Order Id(CSV): ");
		Scanner scan = new Scanner(System.in);
		String orderId = scan.nextLine();
		
		String[] orders=orderId.split(",");
		controller.recordDeliveryController(orders);
		controller.goBackController();
	}
	
	
	public void viewProfile(Employee emp) {
		PayrollDao payRollDao;
		Emp_ServiceDao empServDao;
		try {System.out.println("------------View Profile-------------");
			payRollDao = new PayrollDao(emp.geteId());
			empServDao = new Emp_ServiceDao(emp.geteId());
			System.out.println("A. Employee ID: " + emp.geteId());
			System.out.println("B. Name: " + emp.geteName());
			System.out.println("C. Email Address: " + emp.getEmail());
			System.out.println("D. Phone Number: " + emp.getPhone());
			System.out.println("E. Service Center: " + emp.getServiceCenter());
			System.out.println("F. Role: " + empServDao.getRole());
			System.out.println("G. Start Date: " + payRollDao.getStartDate());
			System.out.println("H. Compensation: " + payRollDao.getWages());
			int freq = payRollDao.getFrequency();
			if(freq == 1)
				System.out.println("I. Compensation Frequency: Hourly");
			else if(freq == 30)
				System.out.println("I. Compensation Frequency: Monthly");
			
		} catch (Exception e) {
			if(e.getMessage().equals("Object Doesn't exists in the database"))
				System.out.println("Employee Profile Not Found");
			else
				e.printStackTrace();
		}
		int choice=0;
		System.out.println("--------MENU-------");
		System.out.println("1. Go Back");
		while(true) {
			try {
				
				choice = console.nextInt();
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
		controller.mainFlowControl(CONSTANTS.RECPTIONIST_PROFILE);

	}
	
	public void displayProfileMenu() {
			
			int choice;
			System.out.println();
			System.out.println("------------Profile-------------");
			System.out.println("1. View Profile");
			System.out.println("2. Update Profile");
			System.out.println("3. Go Back");
			System.out.print("Enter Choice(1-3): ");
			choice = console.nextInt();
			
			if(choice != 3) 
				controller.mainFlowControl(CONSTANTS.EMPLOYEE_PROFILE + choice);
			else {
				
				controller.getView().displayReceptionistMainMenu();
				
			}
			
	}
	
	public void displayUpdateProfileMenu(Employee emp) {
		int choice;
		System.out.println("Select from below");
		System.out.println("1. Name");
		System.out.println("2. Address");
		System.out.println("3. Email Address");
		System.out.println("4. Phone Number");
		System.out.println("5. Password");
		System.out.println("6. Go Back");
		System.out.print("Enter Choice(1-6): ");
		choice = console.nextInt();
		
		if(choice == 6) {
			displayProfileMenu();
		}else {
			controller.updateProfileControlFlow(choice);
		}
	}
	
}
