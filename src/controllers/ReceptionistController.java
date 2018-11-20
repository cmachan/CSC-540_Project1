package controllers;

/*
 * Author: Rahul Sethi
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import constants.CONSTANTS;
import dataAccessObjects.CustomerDao;
import dataAccessObjects.RepairDao;
import databaseUtilities.DatabaseUtil;
import models.Car;
import models.Customer;
import models.Employee;
import models.Repair;
import views.CustomerView;
import views.EmployeeView;
import views.ReceptionistView;

public class ReceptionistController {
	Scanner scan = new Scanner(System.in);
	ReceptionistView view;
	
	public ReceptionistController(ReceptionistView view) {
		this.view = view;
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

	public void mainFlowControl(String choice){
		switch(choice) {
			case CONSTANTS.RECPTIONIST_PROFILE:
				EmployeeView empView = new EmployeeView();
				EmployeeController empController = new EmployeeController(empView);
				empView.setController(empController);
				empController.setRole(2);
				empController.setRecController(this);
				Employee emp = empController.getEmployeeProfile(950932130);
				if (emp != null) {
					empView.displayProfileMenu();
				}
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
	
	
	public void scheduleController() {
		System.out.print("Customer Email Address: ");
		String email = scan.nextLine();
		
		CustomerDao custDao = new CustomerDao();
		Customer cust = custDao.getCustomerProfileByEmail(email);
		if(cust != null) {
			cust.setCenterId(1001); //HardCoded Needs to be changed
			CustomerView custView = new CustomerView();
			custView.setController(new CustomerController(custView));
			String choice = custView.viewServiceSchedule(cust);
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
			cust.setCenterId(1001); //HardCoded Needs to be changed
			CustomerView custView = new CustomerView();
			custView.setController(new CustomerController(custView));
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
			cust.setCenterId(1001); //HardCoded Needs to be changed
			CustomerView custView = new CustomerView();
			custView.setController(new CustomerController(custView));
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
		System.out.println("Task Finished Running");
		goBackController();
	}
	
	public void recordDeliveryController(int orderId) {
		DatabaseUtil dbUtil = new DatabaseUtil();
		Connection conn = dbUtil.establishConnection();
		Statement st = null;
		String qry = "SELECT STATUS FROM CAR_ORDER WHERE OID = " + orderId;
		try {
			st = conn.createStatement();
			
			ResultSet set = st.executeQuery(qry);
			set.next();
			if(set.getString("STATUS").toUpperCase().equals("COMPLETE")) {
				System.out.println("Order is already delivered");
			}else {
				qry = "UPDATE CAR_ORDER SET STATUS = 'COMPLETE' WHERE OID = " + orderId;
				st.executeUpdate(qry);
				
				int serviceCenterId;
				int partId;
				int sourceServiceCenterId;
				int qty;
				String reqType;
				
				qry = "SELECT QTY , CENTERID , REQCENTERID , PARTID , REQTYPE FROM CAR_ORDER WHERE OID = " + orderId;
				
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
}
