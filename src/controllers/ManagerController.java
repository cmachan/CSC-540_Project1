package controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import constants.CONSTANTS;
import dataAccessObjects.CarTypeDao;
import dataAccessObjects.Emp_ServiceDao;
import dataAccessObjects.Mech_HoursDao;
import dataAccessObjects.PayrollDao;
import dataAccessObjects.RepairDao;
import databaseUtilities.DatabaseUtil;
import models.Car;
import models.CarType;
import models.Employee;
import models.Repair;
import views.EmployeeView;
import views.ManagerView;


public class ManagerController {
	Scanner console = new Scanner(System.in);
	ManagerView view;
	static Employee manager; 
	public Scanner getConsole() {
		return console;
	}

	public void setConsole(Scanner console) {
		this.console = console;
	}

	public static Employee getManager() {
		return manager;
	}

	public static void setManager(Employee manager) {
		ManagerController.manager = manager;
	}

	public ManagerController(ManagerView view) {
		this.view = view;
	}
	
	public ManagerView getView() {
		return view;
	}
	
	public void setView(ManagerView view) {
		this.view = view;
	}
	
	public void payrollController() {
		System.out.print("A. Employee Id: ");
		int eId = console.nextInt();
		
		displayPayrollDetails(eId);
		
		goBackController();
		
	}
	
	
	public void displayPayrollDetails(int eId) {
		DatabaseUtil db = new DatabaseUtil();
		Connection conn;
		conn = db.establishConnection();
		Statement st = null;
		String payCheckDate = null;
		String payPeriod = null;
		String eName;
		int compensation;
		int frequency;
		int units;
		double currEarnings;
		double earningsTillDate;
		String startDate;
		double wage;
		
		
		String qry = "SELECT ENAME FROM EMPLOYEE WHERE EID = " + eId;
		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(qry);
			rs.next();
			
			eName = rs.getString("ENAME");
			
			qry =  "SELECT * FROM PAYROLL WHERE EID = " + eId;
			
			ResultSet rs2 = st.executeQuery(qry);
			rs2.next();
			
			startDate = rs2.getString("STARTDATE");
			startDate = (startDate.split(" "))[0];
		
			wage = rs2.getDouble("WAGES");
			frequency = rs2.getInt("FREQUENCY");
			
			LocalDate currDate = LocalDate.now();
			LocalDate nextMonday = null;
			LocalDate prevMonday = null;
			if(frequency == 1) {
				nextMonday = currDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
				payCheckDate = nextMonday.toString();
				prevMonday = nextMonday.minusDays(7);
				payPeriod = "(" + nextMonday.minusDays(7).toString() + " - " + payCheckDate + ")";
			}else{
				int date = Integer.parseInt(startDate.split("-")[2]);
				long incre;
				if(date >= currDate.getDayOfMonth()) {
					incre = date - currDate.getDayOfMonth();
				}else {
					incre = 30 - (currDate.getDayOfMonth() - date);
				}
				LocalDate nextDate = currDate.plusDays(incre);
				payCheckDate = nextDate.toString();
				payPeriod = "(" + nextDate.minusDays(30).toString() + " - " + payCheckDate + ")";
			}
			
			System.out.println("A. Paycheck date: " + payCheckDate);
			System.out.println("B. Pay period: " + payPeriod);
			System.out.println("C. Employee ID: " + eId);
			System.out.println("D. Employee Name: " + eName);
			System.out.println("E. Compensation: " + wage);
			System.out.println("F. Frequency: " + (frequency == 1 ? "Hourly" : "Monthly"));
			if(frequency == 30) {
				LocalDate stld = LocalDate.of(Integer.parseInt(startDate.split("-")[0]),Integer.parseInt(startDate.split("-")[1]),Integer.parseInt(startDate.split("-")[2]));
				long monthsBetween = ChronoUnit.DAYS.between(stld, currDate);
				System.out.println("G. Earnings(Current): " + wage);
				System.out.println("H. Earnings(Till Date): " + monthsBetween*wage);
			}else {
				Mech_HoursDao mDao = null;
				try {
					mDao = new Mech_HoursDao(eId);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				long thisWeekHours = 0;
				
				qry = "select TIMESLOT , RDATE from repair WHERE RDATE > to_date('" + prevMonday.toString() + "','yyyy-MM-dd') AND RDATE < to_date('" + nextMonday.toString() + "','yyyy-MM-dd') AND MECHANICID = " + eId;
				
				ResultSet rs3 = st.executeQuery(qry);
				
				while(rs3.next()) {
					String s = rs3.getString(1);
					String dateStart = s.split("-")[0];
					String dateStop = s.split("-")[1];

					SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

					Date d1 = null;
					Date d2 = null;

					try {
						d1 = format.parse(dateStart);
						d2 = format.parse(dateStop);
						long diff = d2.getTime() - d1.getTime();
						long diffHours = diff / (60 * 60 * 1000) % 24;
						thisWeekHours += diffHours;
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				System.out.println("G. Earnings(Current): " + thisWeekHours * wage);
				System.out.println("H. Earnings(Till Date): " + mDao.getHours() * wage);
			}
			
			
			st.close();
		}catch(SQLException e) {
			System.out.println("Payroll Details Failed");
		}finally {
				db.closeConnection();
		}
	}
	
	public void addEmployee(Employee emp) {
		DatabaseUtil db = new DatabaseUtil();
		Connection conn;
		conn = db.establishConnection();
		Statement st = null;
		String qry = "INSERT INTO EMPLOYEE (ENAME , EMAIL , ADDRESS , PHONE) values ('" + emp.geteName() + "' , '" + emp.getEmail()+ "' , '" + emp.getAddress() + "' , " + emp.getPhone() + ")";
		try {
			st = conn.createStatement();
			st.executeUpdate(qry);
			
			qry = "SELECT EID FROM EMPLOYEE WHERE EMAIL = '" + emp.getEmail() + "'";
			
			ResultSet rs = st.executeQuery(qry);
			rs.next();
			emp.seteId(rs.getInt("EID"));
			int freq;
			if(emp.getRole().equals("RECEPTIONIST")) {
				freq = 30;
			}else {
				freq = 1;
			}
			PayrollDao payDao = new PayrollDao(emp.geteId(), emp.getsDate().getYear() + "-" + emp.getsDate().getMonth() + "-" + emp.getsDate().getDate(), emp.getWage(), freq);
			payDao.insert();
			
			Emp_ServiceDao empServDao = new Emp_ServiceDao(emp.geteId(), emp.getRole(), emp.getServiceCenter());
			empServDao.insert();
			System.out.println("Employee Added Successfully");
			st.close();
		}catch(SQLException e) {
			System.out.println("Employee Addition Failed");
//			e.printStackTrace();
		}finally {
				db.closeConnection();
				view.displayManagerMainMenu();
		}
	}
	
	public void orderController(int choice) {
		if(choice == 1) {
			viewOrderHistory();
		}else if(choice == 2) {
			newOrder();
		}
		orderGoBackController();
	}
	
	public void newOrder() {
		int partId , qty;
		String carMake;
		int centerId = manager.getServiceCenter(); 
		String did;
		int reqCenterId;
		String reqType; //S for Service Center D for Distributor 
		System.out.print("A. Partid: ");
		partId = console.nextInt();
		
		System.out.print("B. Quantity: ");
		qty = console.nextInt();
		console.nextLine();
		
		System.out.print("C. Car Make: ");
		carMake = console.nextLine();

		DatabaseUtil db = new DatabaseUtil();
		Connection conn;
		conn = db.establishConnection();
		Statement st = null;
//		String qry = "Insert into CAR_ORDER (ODATE,QTY,CENTERID,DISTRIBUTORID,STATUS,REQCENTERID,REQTYPE,PARTID,ACTUALDELDATE,EXPECTDELDATE,CARMAKE) values (to_date('19-NOV-18','DD-MON-RR'),5,1001,null,'Pending',1002,null,10,null,to_date('21-NOV-18','DD-MON-RR'),null)";
		String qry = "SELECT QUANTITY , THRESHOLD , SERVICECENTERID FROM INVENTORY WHERE PARTID = " + partId + " AND SERVICECENTERID = " + centerId + " AND CARMAKE = '" + carMake + "'";
		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(qry);
			rs.next();
			
			if((rs.getInt(1) - rs.getInt(2)) > qty) {
				reqType = "S";
				reqCenterId = rs.getInt(3);
				qry = "Insert into CAR_ORDER (ODATE,QTY,CENTERID,STATUS,REQCENTERID,REQTYPE,PARTID,EXPECTDELDATE,CARMAKE) "
						+ "values (to_date('" + LocalDate.now().toString() + "','yyyy-MM-dd')," + qty + "," + centerId + ",'Pending'," + reqCenterId + ",'" + reqType + "'," + partId + ",to_date('" + LocalDate.now().plusDays(7).toString() + "','yyyy-MM-dd'),'" + carMake + "')";
			}else {
				reqType = "D";
				qry = "SELECT DID , DELIVERYTIME FROM DISTRIBUTOR_PARTS WHERE PARTID = " + partId;
				ResultSet rs2 = st.executeQuery(qry);
				rs2.next();
				did = rs2.getString(1);
				qry = "Insert into CAR_ORDER (ODATE,QTY,CENTERID,STATUS,DISTRIBUTORID,REQTYPE,PARTID,EXPECTDELDATE,CARMAKE) "
						+ "values (to_date('" + LocalDate.now().toString() + "','yyyy-MM-dd')," + qty + "," + centerId + ",'Pending','" + did + "','" + reqType + "'," + partId + ",to_date('" + LocalDate.now().plusDays(rs2.getInt(2)).toString() + "','yyyy-MM-dd'),'" + carMake + "')";
			}
			
//			System.out.print(qry);
			st.executeUpdate(qry);
			System.out.println("Order Placed Succesfully");
			st.close();
		}catch(SQLException e) {
			System.out.println("Order Creation Failed");
//			e.printStackTrace();
		}finally {
				db.closeConnection();
		}
	}
	
	public void viewOrderHistory(){
		DatabaseUtil db = new DatabaseUtil();
		Connection conn;
		conn = db.establishConnection();
		Statement st = null;
		String qry = 	" SELECT o.OID , o.ODATE , ip.NAME , sc1.NAME , sc2.NAME , o.QTY , pw.PRICE , pw.PRICE*o.QTY , o.STATUS " + 
						" from CAR_ORDER o , INVENTORY_PARTS ip , SERVICECENTER sc1 , SERVICECENTER sc2 , PARTS_WARRANTY pw " + 
						" where ip.PARTID = o.PARTID AND sc1.ID = o.REQCENTERID AND sc2.ID = o.CENTERID AND pw.PARTID = o.PARTID AND UPPER(pw.CARMAKE) = UPPER(o.CARMAKE) ";
		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(qry);
			System.out.println("OID\tODATE\tNAME\tSupplier\tPurchaser\tQty\tPrice\tAmount\tStatus");
			while(rs.next()) {
				System.out.println(rs.getString(1) + "\t" + rs.getString(2).split(" ")[0] + "\t" + rs.getString(3) + "\t" +rs.getString(4) + "\t" + rs.getString(5) + "\t" + rs.getString(6) + "\t" + rs.getString(7) + "\t" + rs.getString(8) + "\t" + rs.getString(9));
			}
			st.close();
		}catch(SQLException e) {
			System.out.println("Order History Failed");
//			e.printStackTrace();
		}finally {
				db.closeConnection();
		}
	}
	
	public void mainFlowControl(String choice) {
			switch(choice) {
				case CONSTANTS.MANAGER_PROFILE:
					EmployeeView empView = new EmployeeView();
					EmployeeController empController = new EmployeeController(empView);
					empView.setController(empController);
					empController.setRole(1);
					empController.setManController(this);
					Employee emp = empController.getEmployeeProfile(manager.geteId());
					if (emp != null) {
						empView.displayProfileMenu();
					}
					break;
				case CONSTANTS.MANAGER_CUSTOMER_PROFILE:
					System.out.print("Customer email address: ");
					view.viewCusomerProfile(console.nextLine());
					break;
				case CONSTANTS.MANAGER_ADD_EMPLOYEE:
					view.addEmployee();
					break;
				case CONSTANTS.MANAGER_PAYROLL:
					payrollController();
					break;
				case CONSTANTS.MANAGER_INVENTORY:
					inventoryController();
					break;
				case CONSTANTS.MANAGER_ORDERS:
					view.displayOrderMenu();
					break;
				case CONSTANTS.MANAGER_NOTIFICATION:
					viewNotifications();
					break;
				case CONSTANTS.MANAGER_NEW_CAR_MODEL:
					addCarModel();
					break;
				case CONSTANTS.MANAGER_CAR_SERVICE_DETAILS:
						carService();
					break;
				case CONSTANTS.MANAGER_SERVICE_HISTORY:
					history();
					break;
				case CONSTANTS.MANAGER_INVOICES:
						invoice();
					break;
				default:
					System.out.println("Invalid Choice");
			}
			view.displayManagerMainMenu();
	}
	
	private void carService() {
		// TODO Auto-generated method stub
		 RepairDao repair=new RepairDao();
		 ArrayList<CarType> cars= repair.getCarServicebyCenter(manager.getServiceCenter());
		 view.viewCarService(cars);
	}

	private void history() {
		// TODO Auto-generated method stub
		 RepairDao repair=new RepairDao();
		 ArrayList<Repair> repairs= repair.getServiceHistorybyCenter(manager.getServiceCenter());
		 view.viewServiceHistory(repairs);
	}

	private void invoice() {
		// TODO Auto-generated method stub
		HashMap<String, Repair> completedservices=getCompletedServices(manager.getServiceCenter());
		view.viewServiceInvoice(completedservices);
		
	}

	private HashMap<String, Repair> getCompletedServices(int serviceCenter) {
		 RepairDao repair=new RepairDao();
		 return repair.getCompletedServicesbycenter(serviceCenter);
	}

	public void addCarModel() {
		DatabaseUtil db = new DatabaseUtil();
		Connection conn;
		conn = db.establishConnection();
		Statement st = null;
		String qry = "";
		
		try {
			String make;
			String model;
			int year;
			
			int aMiles , aBid;
			int bMiles , bBid;
			int cMiles , cBid;
			
			System.out.print("A. Make: ");
			make = console.nextLine();
			
			System.out.print("B. Model: ");
			model = console.nextLine();
			
			System.out.print("C. Year: ");
			year = console.nextInt();
			
			st = conn.createStatement();
			qry = "SELECT * from CARTYPE WHERE MAKE = '" + make + "' AND MODEL = '" + model + "'";
			ResultSet rs = st.executeQuery(qry);
			
			
			if(rs.next()) {
				System.out.println("Car Make already available");
				view.displayManagerMainMenu();
			}
			
			//-------------
			
			System.out.println("D. Service A: ");
			System.out.print("a. Miles: ");
			aMiles = console.nextInt();
			
			st = conn.createStatement();
			qry = "select BID , BNAME from basicservice";
			rs = st.executeQuery(qry);
			
			String[] bidsA;
			
			while(rs.next()) {
				System.out.println(rs.getInt(1) + " -> " + rs.getString(2));
			}
			console.nextLine();
			System.out.print("b. Choose Basic Services(1,4,5,..): ");
			String line = console.nextLine();
			bidsA = line.split(",");
			
			//----------------------
			
			System.out.println("E. Service B: ");
			System.out.print("a. Miles: ");
			bMiles = console.nextInt();
			
			
			qry = "select BID , BNAME from basicservice";
			rs = st.executeQuery(qry);
			
			String[] bidsB;
			
			while(rs.next()) {
				System.out.println(rs.getInt(1) + " -> " + rs.getString(2));
			}
			console.nextLine();
			System.out.print("b. Choose Basic Services(1,4,5,..): ");
			line = console.nextLine();
			bidsB = line.split(",");
			
			
			//----------------------
			
			System.out.println("E. Service B: ");
			System.out.print("a. Miles: ");
			cMiles = console.nextInt();
			
			st = conn.createStatement();
			qry = "select BID , BNAME from basicservice";
			rs = st.executeQuery(qry);
			
			String[] bidsC;
			
			while(rs.next()) {
				System.out.println(rs.getInt(1) + " -> " + rs.getString(2));
			}
			console.nextLine();
			System.out.print("b. Choose Basic Services(1,4,5,..): ");
			line = console.nextLine();
			bidsC = line.split(",");
			
			//---------------------------
			
			System.out.println("1. Add car");
			System.out.println("2. Go Back");
			System.out.print("Enter Choice(1-2): ");
			int choice = console.nextInt();
			
			if(choice == 2) {
				view.displayManagerMainMenu();
			}
			
			CarTypeDao carTypeDao = new CarTypeDao();
			int carTypeId = carTypeDao.insertCarType(make, model);
			
			qry = "SELECT MAX(MID) FROM MAINTENANCESERVICE";
			
			rs = st.executeQuery(qry);
			rs.next();
			
			int aMid = rs.getInt(1)+1;
			int bMid = aMid+1;
			int cMid = aMid+2;
			
			qry = "INSERT INTO MAINTENANCESERVICE VALUES(" + aMid + " , 'SERVICE A' , " + aMiles + " , " + carTypeId + ")";
			st.executeUpdate(qry);
			
			qry = "INSERT INTO MAINTENANCESERVICE VALUES(" + bMid + " , 'SERVICE B' , " + bMiles + " , " + carTypeId + ")";
			st.executeUpdate(qry);
			
			qry = "INSERT INTO MAINTENANCESERVICE VALUES(" + cMid + " , 'SERVICE C' , " + cMiles + " , " + carTypeId + ")";
			st.executeUpdate(qry);
			
			int partId , qty;
			
			for(String bid : bidsA) {
				qry = "INSERT INTO BASICMAINTENANCEMAP VALUES(" + aMid + " , " + Integer.parseInt(bid) + ")";
				st.executeUpdate(qry);
				
				qry = "SELECT PARTID , QUANTITY FROM BASICSERVICE_PART WHERE BID = " + Integer.parseInt(bid);
				rs = st.executeQuery(qry);
				rs.next();
				partId = rs.getInt(1);
				qty = rs.getInt(2);
				
				qry = "INSERT INTO BASICSERVICE_PART VALUES(" + Integer.parseInt(bid) + " , " + partId + " , " + qty + " , " + carTypeId + ")";
				st.executeUpdate(qry);
			}
			
			for(String bid : bidsB) {
				qry = "INSERT INTO BASICMAINTENANCEMAP VALUES(" + bMid + " , " + Integer.parseInt(bid) + ")";
				st.executeUpdate(qry);
				
				qry = "SELECT PARTID , QUANTITY FROM BASICSERVICE_PART WHERE BID = " + Integer.parseInt(bid);
				rs = st.executeQuery(qry);
				rs.next();
				partId = rs.getInt(1);
				qty = rs.getInt(2);
				
				qry = "INSERT INTO BASICSERVICE_PART VALUES(" + Integer.parseInt(bid) + " , " + partId + " , " + qty + " , " + carTypeId + ")";
				st.executeUpdate(qry);
			}
			
			for(String bid : bidsC) {
				qry = "INSERT INTO BASICMAINTENANCEMAP VALUES(" + cMid + " , " + Integer.parseInt(bid) + ")";
				st.executeUpdate(qry);
				
				qry = "SELECT PARTID , QUANTITY FROM BASICSERVICE_PART WHERE BID = " + Integer.parseInt(bid);
				rs = st.executeQuery(qry);
				rs.next();
				partId = rs.getInt(1);
				qty = rs.getInt(2);
				
				qry = "INSERT INTO BASICSERVICE_PART VALUES(" + Integer.parseInt(bid) + " , " + partId + " , " + qty + " , " + carTypeId + ")";
				st.executeUpdate(qry);
			}
			
			
			st.close();
			System.out.println("Car Added Successfully");
		}catch(SQLException e) {
			System.out.println("Car Addition Failed");
//			e.printStackTrace();
		}finally {
				db.closeConnection();
				view.displayManagerMainMenu();
		}
	}
	
	
	public void viewNotifications() {
		DatabaseUtil db = new DatabaseUtil();
		Connection conn;
		conn = db.establishConnection();
		Statement st = null;
		String qry = "SELECT n.NOTIFICATIONID , o.REQTYPE FROM CAR_ORDER o , NOTIFICATION n where o.OID = n.ORDERID";
		
		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(qry);
			List<Integer> nids = new ArrayList<Integer>();
			List<String> reqTypes = new ArrayList<String>();
			int idx = 0;
			
			while(rs.next()) {
				nids.add(rs.getInt(1));
				reqTypes.add(rs.getString(2));
			}
			
			for(int nid : nids) {
				String reqType = reqTypes.get(idx);
				idx++;
//				System.out.println(reqType);
				if(reqType.equals("S")) {
					qry = 	" SELECT n.NOTIFICATIONID , n.NDATE , o.OID , sc.NAME , o.EXPECTDELDATE " + 
							" from NOTIFICATION n , CAR_ORDER o , SERVICECENTER sc " + 
							" where n.ORDERID = o.OID AND sc.ID = o.REQCENTERID AND n.NOTIFICATIONID = " + nid;
				}else if(reqType.equals("D")) {
					qry = 	" SELECT n.NOTIFICATIONID , n.NDATE , o.OID , d.NAME , o.EXPECTDELDATE " + 
							" from NOTIFICATION n , CAR_ORDER o , DISTRIBUTOR d " + 
							" where n.ORDERID = o.OID AND d.DID = o.DISTRIBUTORID AND n.NOTIFICATIONID = " + nid;
				}
				ResultSet rs2 = st.executeQuery(qry);
				rs2.next();
				System.out.println(rs2.getString(1) + "\t" + rs2.getString(2) + "\t" + rs2.getString(3) + "\t" + rs2.getString(4) + "\t" + rs2.getString(5));
			}
			
			st.close();
		}catch(SQLException e) {
			System.out.println("View Notifications Failed");
		}finally {
				db.closeConnection();
		}
		goBackController();
	}
	
	public void inventoryController() {
		viewInventory();
		goBackController();
	}
	
	public void viewInventory() {
		
		DatabaseUtil db = new DatabaseUtil();
		Connection conn;
		conn = db.establishConnection();
		Statement st = null;
		String qry = 	" SELECT IP.PARTID , CONCAT(CONCAT(IP.NAME , '-') ,PW.CARMAKE) , I.QUANTITY , PW.PRICE , I.MINQTYORDER , I.THRESHOLD " + 
						" FROM PARTS_WARRANTY PW , INVENTORY_PARTS IP , INVENTORY I " + 
						" WHERE IP.PARTID = PW.PARTID AND PW.PARTID = I.PARTID ";	
		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(qry);
			System.out.println("PartId\tQty\tPrice\tMinQty\tThreshold\tName");
			while(rs.next()) {
				System.out.println(rs.getString(1) + "\t" + rs.getString(3) + "\t" + rs.getString(4) + "\t" +rs.getString(5) + "\t" + rs.getString(6) + "\t\t" + rs.getString(2));
			}
			st.close();
		}catch(SQLException e) {
			System.out.println("View Inventory Failed");
//			e.printStackTrace();
		}finally {
				db.closeConnection();
		}
		
	}
	
	public void addEmployeeController(Employee emp) {
		System.out.println("1. Add");
		System.out.println("2. Go Back");
		System.out.print("Enter Choice(1-2): ");
		int choice = console.nextInt();
		
		if(choice == 1) {
			addEmployee(emp);
		}else if(choice == 2) {
			view.displayManagerMainMenu();
		}else {
			System.out.print("Invalid Input");
			addEmployeeController(emp);
		}
	}
	
	public void goBackController() {
		System.out.println("\n1. Go Back");
		System.out.print("Enter Choice(1): ");
		int choice;
		choice = console.nextInt();
		
		if(choice == 1) {
			view.displayManagerMainMenu();
		}else {
			System.out.println("Invalid Input");
			goBackController();
		}
		
	}
	
	public void orderGoBackController() {
		System.out.println("\n1. Go Back");
		System.out.print("Enter Choice(1): ");
		int choice;
		choice = console.nextInt();
		
		if(choice == 1) {
			view.displayOrderMenu();
		}else {
			System.out.println("Invalid Input");
			goBackController();
		}
		
	}
}
