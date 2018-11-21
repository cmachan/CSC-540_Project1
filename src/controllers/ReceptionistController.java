package controllers;

/*
 * Author: Rahul Sethi
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import constants.CONSTANTS;
import constants.Utility;
import dataAccessObjects.CustomerDao;
import dataAccessObjects.EmployeeDao;
import dataAccessObjects.RepairDao;
import databaseUtilities.DatabaseUtil;
import models.Car;
import models.Customer;
import models.Employee;
import models.Login;
import models.Repair;
import views.CustomerView;
import views.EmployeeView;
import views.ReceptionistView;

public class ReceptionistController {
	Scanner scan = new Scanner(System.in);
	ReceptionistView view;
	static Employee receptionist;
	
	public ReceptionistController(ReceptionistView view) {
		this.view = view;
	}
	
	public Scanner getScan() {
		return scan;
	}

	public void setScan(Scanner scan) {
		this.scan = scan;
	}

	public static Employee getReceptionist() {
		return receptionist;
	}

	public static void setReceptionist(Employee receptionist) {
		ReceptionistController.receptionist = receptionist;
	}

	public ReceptionistView getView() {
		return view;
	}
	
	public void setView(ReceptionistView view) {
		this.view = view;
	}

	public void registerCarControlFlow(Car car) {
		System.out.println("1. Register");
		System.out.println("2. Cancel");
		System.out.print("Enter Choice(1-2): ");
		int choice = scan.nextInt();
		
		switch(choice) {
		case 1:
			registerCarIntoDb(car);
			break;
		case 2:
			view.displayReceptionistMainMenu();
			break;
		default:
			System.out.println("Invalid Input");
			registerCarControlFlow(car);
		}
	}
	
	public void serviceHistoryDisplayController() {
		System.out.print("Customer Email Address: ");
		String email;
		email = scan.nextLine();
		view.viewServiceHistory(email);
		goBackController();
	}
	
	public void goBackController() {
		System.out.println("\n1. Go Back");
		System.out.print("Enter Choice(1): ");
		int choice;
		choice = scan.nextInt();
		
		if(choice == 1) {
			view.displayReceptionistMainMenu();
		}else {
			System.out.println("Invalid Input");
			goBackController();
		}
		
	}

	public void registerCarIntoDb(Car car) {
		DatabaseUtil dbUtil = new DatabaseUtil();
		Connection conn = dbUtil.establishConnection();
		int carTypeId = 0;
		Statement st = null;
		String qry = "SELECT * from CARTYPE where MAKE = '" + car.getMake().toUpperCase() +"' and MODEL = '" + car.getModel().toUpperCase() +"'";
		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(qry);
			
			int flag = 0;
			
			if(rs.next()) {
				flag = 1;
				carTypeId = rs.getInt("CARTYPEID");
			}
			
			if(flag == 0) {
				qry = "SELECT MAX(CARTYPEID) from CARTYPE";
				rs = st.executeQuery(qry);
				rs.next();
				carTypeId = rs.getInt(1) + 1;
				qry = "INSERT INTO CARTYPE (MAKE , MODEL) VALUES('" + car.getMake().toUpperCase() + "','" + car.getModel().toUpperCase() +"')";
				st.executeUpdate(qry);
			}
			
			qry = "Insert into CAR values ('" + car.getLicensePlate() + "'," + carTypeId + "," + car.getcId() + ",to_date('" + car.getDateOfService().getYear() + "-" + car.getDateOfService().getMonth() + "-" + car.getDateOfService().getDate() + "','YYYY-MM-DD'),'" + car.getLastServiceType() + "',to_date('" + car.getDateOfPurchase().getYear() + "-" + car.getDateOfPurchase().getMonth() + "-" + car.getDateOfPurchase().getDate() + "','YYYY-MM-DD')," + car.getLastMileage() + "," + car.getMakeYear() + ")";
			st.executeUpdate(qry);
			
			System.out.println("Car Registered Successfully");
			
			st.close();
		}catch(SQLException e) {
			e.printStackTrace();
			System.out.println("Car Registeration Fail");
		}finally {
				try {
					dbUtil.closeConnection();
					if(st != null)
						st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		
	}

	public void updateProfileControlFlow(int choice) {

		String input="";
		switch(choice) {
		case 1: 
			System.out.println("Enter new Name:  ");
			input="";
			while(input.equals("")) {
				input=(scan.nextLine()).trim();
				if (input.equals("")){
					System.out.println("Error: Name is blank. ");
				}
			}
			
			receptionist.seteName(input);
			break;
		case 2: 
			System.out.println("Enter new Address:  ");
			input="";
			while(input.equals("")) {
				input=(scan.nextLine()).trim();
				if (input.equals("")){
					System.out.println("Error: Address is blank. ");
				}
			}
			receptionist.setAddress(input);
			break;
		case 3:
			System.out.println("Enter new email:  ");
			input="";
			while(input.equals("")) {
			
				input=(scan.nextLine()).trim();
				
				if (input.equals("")){
					System.out.println("Error: Email is blank. ");
				}else if(!Utility.isValidEmailAddress((input))) {
					System.out.println("Error: Email not correct ");
					input="";
				}
				
	
			}
			receptionist.setEmail(input);
				
			break;
		case 4: 
			System.out.println("Enter new Phone Number:  ");
			input="";
			while(input.equals("")) {
				input=(scan.nextLine()).trim();
				
				if (input.equals("")){
					System.out.println("Error: Phone Number is blank. ");
				}else if(!Utility.isValidPhoneNumber(input)) {
					System.out.println("Error: Phone Number not correct ");
					input="";
				}
			}
			input=input.replaceAll("-", "");
			receptionist.setPhone(Long.parseLong(input.replaceAll("\\.", "")));
			break;	
			
		case 5: 
			System.out.println("Enter new Password:  ");
			input="";
			while(input.equals("")) {
			
				input=(scan.nextLine()).trim();
				
				if (input.equals("")){
					System.out.println("Error: Password is blank. ");
				}
				
	
			}
			receptionist.setPass(input);
			break;	
		default:
			System.out.println("Invalid Choice");
			view.displayUpdateProfileMenu(receptionist);	
		}
		
		EmployeeDao empDao = new EmployeeDao();
		empDao.updateEmployee(receptionist);
		view.displayProfileMenu();
		}
	public void mainFlowControl(String choice){
		switch(choice) {
			case CONSTANTS.EMPLOYEE_VIEW_PROFILE:
				view.viewProfile(receptionist);
				break;
			case CONSTANTS.EMPLOYEE_UPDATE_PROFILE:
				view.displayUpdateProfileMenu(receptionist);
				break;
			case CONSTANTS.RECPTIONIST_PROFILE:
				view.displayProfileMenu();
				break;
			case CONSTANTS.RECPTIONIST_CUSTOMER_PROFILE:
				System.out.print("Customer email address: ");
				view.viewCusomerProfile(scan.nextLine());
				break;
			case CONSTANTS.RECPTIONIST_REGISTER_CAR:
				view.registerCar();
				break;
			case CONSTANTS.RECPTIONIST_SERVICE_HISTORY:
				viewServiceHistory();
				break;
			case CONSTANTS.RECPTIONIST_SCHED_SERVICE:
				scheduleController();
				break;
			case CONSTANTS.RECPTIONIST_RESCHED_SERVICE:
				rescheduleController();
				break;
			case CONSTANTS.RECPTIONIST_INVOICE:
				invoice();
				break;
			case CONSTANTS.RECPTIONIST_UPDATE_INVENTORY:
				updateInventory();
				break;
			case CONSTANTS.RECPTIONIST_RECORD_DELIVERY:
				view.recordDelivery();
				break;
			default:
				System.out.println("Invalid Choice");
		}
	
		view.displayReceptionistMainMenu();
	}
	
	
	private void invoice() {
		// TODO Auto-generated method stub
		System.out.print("Customer Email Address: ");
		String email = scan.nextLine();
		
		CustomerDao custDao = new CustomerDao();
		Customer cust = custDao.getCustomerProfileByEmail(email);
		if(cust != null) {
			
			
			;
			CustomerView custView=new CustomerView();
			CustomerController con=new CustomerController(custView);
			custView.setController(con);
			CustomerController.setCustomer(cust);
			
			HashMap<String, Repair> completedservices=con.getCompletedServices(cust.getcId());
			
			String choice =custView.viewServiceInvoice(completedservices,cust);
			
			if(choice.equals(CONSTANTS.CUSTOMER_SERVICE)) {
				view.displayReceptionistMainMenu();
			}
		}else {
			System.out.print("Customer Doesn't Exist");
		}
	}

	public void scheduleController() {
		System.out.print("Customer Email Address: ");
		String email = scan.nextLine();
		
		CustomerDao custDao = new CustomerDao();
		Customer cust = custDao.getCustomerProfileByEmail(email);
		if(cust != null) {
			CustomerView custView=new CustomerView();
			CustomerController con=new CustomerController(custView);
			custView.setController(con);
			CustomerController.setCustomer(cust);
			//HardCoded Needs to be changed
			
			String choice = custView.viewServiceSchedule(cust,receptionist.getServiceCenter());
			if (choice.equals(CONSTANTS.CUSTOMER_SERVICE_SCHEDULE2)) {
				choice=custView.viewSchedule(cust);
			}
			if(choice.equals(CONSTANTS.CUSTOMER_SERVICE)) {
				view.displayReceptionistMainMenu();
			}
		}else {
			System.out.print("Customer Doesn't Exist");
		}
	}
	
	public void viewServiceHistory() {
		
		System.out.print("Customer Email Address: ");
		String email = scan.nextLine();
		
		CustomerDao custDao = new CustomerDao();
		Customer cust = custDao.getCustomerProfileByEmail(email);
		if(cust != null) {
			CustomerView custView=new CustomerView();
			CustomerController con=new CustomerController(custView);
			custView.setController(con);
			CustomerController.setCustomer(cust);
			//HardCoded Needs to be changed
			
			//HardCoded Needs to be changed
			RepairDao repairDao = new RepairDao();
			ArrayList<Repair> repairs = repairDao.getServiceHistory(cust.getcId());
			String choice=custView.viewServiceHistory(repairs);
			if(choice.equals(CONSTANTS.CUSTOMER_SERVICE)) {
				view.displayReceptionistMainMenu();
			}
		}else {
			System.out.print("Customer Doesn't Exist");
		}
	}
	
	public void rescheduleController() {
		System.out.print("Customer Email Address: ");
		String email = scan.nextLine();
		
		CustomerDao custDao = new CustomerDao();
		Customer cust = custDao.getCustomerProfileByEmail(email);
		if(cust != null) {
			CustomerView custView=new CustomerView();
			CustomerController con=new CustomerController(custView);
			custView.setController(con);
			CustomerController.setCustomer(cust);
			//HardCoded Needs to be changed
			
			RepairDao repair=new RepairDao();
			ArrayList<Repair> upcomingServices = repair.getUpcomingServices(cust.getcId());
			String choice = custView.viewServiceReSchedule(upcomingServices,cust);
			if(choice.equals(CONSTANTS.CUSTOMER_SERVICE)) {
				view.displayReceptionistMainMenu();
			}
		}else {
			System.out.print("Customer Doesn't Exist");
		}
	}
	
	public void updateInventory() {
		long start = System.currentTimeMillis();
	    try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Task Finished.");
		goBackController();
	}
	
	public void recordDeliveryController(String[] orderss) {
		DatabaseUtil dbUtil = new DatabaseUtil();
		Connection conn ;
		Statement st = null;
		
		int orderID=0;
		for (int i =0;i<orderss.length;i++) {
			try {
				orderID=Integer.parseInt(orderss[i]);
				String qry = "SELECT STATUS FROM CAR_ORDER WHERE OID = " + orderID;
				conn = dbUtil.establishConnection();
				st = conn.createStatement();
				
				ResultSet set = st.executeQuery(qry);
				set.next();
				if(set.getString("STATUS").toUpperCase().equals("COMPLETE")) {
					System.out.println("Order is already delivered");
				}else {
					qry = "UPDATE CAR_ORDER SET STATUS = '"+CONSTANTS.STATUS_COMPLETE+"', ACTUALDELDATE=CURRENT_DATE WHERE OID = " + orderID;
					st.executeUpdate(qry);
					
					int serviceCenterId;
					int partId;
					int sourceServiceCenterId;
					int qty;
					String reqType;
					
					qry = "SELECT QTY , CENTERID , REQCENTERID , PARTID , REQTYPE FROM CAR_ORDER WHERE OID = " + orderID;
					
					ResultSet rs = st.executeQuery(qry);
					rs.next();
					serviceCenterId = rs.getInt("CENTERID");
					partId = rs.getInt("PARTID");
					reqType = rs.getString("REQTYPE");
					qty = rs.getInt("QTY");
					
					if(reqType.equals("S")) {
						sourceServiceCenterId = rs.getInt("REQCENTERID");
						
						qry = "UPDATE INVENTORY SET QUANTITY = QUANTITY - " + qty + " WHERE PARTID = " + partId + " AND SERVICECENTERID = " + sourceServiceCenterId;
						st.executeUpdate(qry);
					}
					
					qry = "UPDATE INVENTORY SET QUANTITY = QUANTITY + " + qty + " WHERE PARTID = " + partId + " AND SERVICECENTERID = " + serviceCenterId;
					st.executeUpdate(qry);
					
					System.out.println("Order Status Updated to Delivered");
					
				}

				st.close();
				
			}catch(SQLException e) {
				e.printStackTrace();
				System.out.println("Order Status Updation Failed");
			}
			catch(NumberFormatException nc) {
				
				System.out.println(" Order is not correct");
			}
			finally {	try {
						dbUtil.closeConnection();
						if(st != null)
							st.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
			}
		}
		
	}

	public void setReceptionist(Login login) {
		// TODO Auto-generated method stub
		
		EmployeeView empView = new EmployeeView();
		EmployeeController empController = new EmployeeController(empView);
		empView.setController(empController);
		
		empController.setRecController(this);
		receptionist = empController.getEmployeeProfile(Integer.parseInt(login.getuId()));
		
		
	}
}
