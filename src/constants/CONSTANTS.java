package constants;

public class CONSTANTS {
	/*
	 * CONSTANTS for Database Connections.
	 */
	public static final String dbUserName = "rsethi3";
	public static final String dbPassword = "200241349";
	public static final String dbUrl = "jdbc:oracle:thin:@orca.csc.ncsu.edu:1521:ORCL01";
	public static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
	
	/*
	 * CONSTANTS for CUSTOMER Module
	 * Added by Cheru
	 */
	
	public static final String CUSTOMER_PROFILE= CONSTANTS.CUSTOMER_MAIN_MENU+"1";
	public static final String CUSTOMER_REGISTER_CAR= CONSTANTS.CUSTOMER_MAIN_MENU+"2";
	public static final String CUSTOMER_MAIN_MENU= "CUSTOMER_MAIN_MENU";
	public static final String CUSTOMER_VIEW_PROFILE = CONSTANTS.CUSTOMER_PROFILE+"1";
	public static final String CUSTOMER_SERVICE= CONSTANTS.CUSTOMER_MAIN_MENU+"3";
	public static final String CUSTOMER_SERVICE_HISTORY= CONSTANTS.CUSTOMER_SERVICE+"1";
	public static final String CUSTOMER_SERVICE_SCHEDULE= CONSTANTS.CUSTOMER_SERVICE+"2";
	public static final String  CUSTOMER_SERVICE_RESCHEDULE= CONSTANTS.CUSTOMER_SERVICE+"3";
	public static final String  CUSTOMER_SERVICE_INVOICE= CONSTANTS.CUSTOMER_MAIN_MENU+"4";
	public static final String CUSTOMER_INVOICE= CONSTANTS.CUSTOMER_MAIN_MENU+"4";
	public static final String LOGOUT= "LOGOUT";
	public static final String CUSTOMER_UPDATE_PROFILE = CONSTANTS.CUSTOMER_PROFILE+"2";
	public static final String CUSTOMER_SERVICE_SCHEDULE2 = CONSTANTS.CUSTOMER_SERVICE_SCHEDULE+"2";
	public static final String STATUS_PENDING = "Pending";
	
	/*
	 * CONSTANTS for Employee Module
	 * Added by Rahul
	 */
	public static final String EMPLOYEE_PROFILE= "EMPLOYEE_PROFILE";
	public static final String EMPLOYEE_VIEW_PROFILE= EMPLOYEE_PROFILE + "1";
	public static final String EMPLOYEE_UPDATE_PROFILE= EMPLOYEE_PROFILE + "2";
	
		public static final String STATUS_COMPLETE = "Complete";
	
	/*
	 * CONSTANTS for RECEPTIONIST Module
	 * Added by Rahul
	 */
	public static final String RECPTIONIST_MAIN_MENU = "RECEPTION_MAIN_MENU";
	
	public static final String RECPTIONIST_PROFILE = "RECEPTION_MAIN_MENU1";
	public static final String RECPTIONIST_CUSTOMER_PROFILE = "RECEPTION_MAIN_MENU2";
	public static final String RECPTIONIST_REGISTER_CAR = "RECEPTION_MAIN_MENU3";
	public static final String RECPTIONIST_SERVICE_HISTORY = "RECEPTION_MAIN_MENU4";
	public static final String RECPTIONIST_SCHED_SERVICE = "RECEPTION_MAIN_MENU5";
	public static final String RECPTIONIST_RESCHED_SERVICE = "RECEPTION_MAIN_MENU6";
	public static final String RECPTIONIST_INVOICE = "RECEPTION_MAIN_MENU7";
	public static final String RECPTIONIST_UPDATE_INVENTORY = "RECEPTION_MAIN_MENU8";
	public static final String RECPTIONIST_RECORD_DELIVERY = "RECEPTION_MAIN_MENU9";
	public static final String LOGIN_MAIN_MENU = "LOGIN_MAIN_MENU";
	
	public static final String MANAGER_MAIN_MENU = "MANAGER_MAIN_MENU";
	public static final String MANAGER_PROFILE = "MANAGER_MAIN_MENU1";
	public static final String MANAGER_CUSTOMER_PROFILE = "MANAGER_MAIN_MENU2";
	public static final String MANAGER_ADD_EMPLOYEE = "MANAGER_MAIN_MENU3";
	public static final String MANAGER_PAYROLL = "MANAGER_MAIN_MENU4";
	public static final String MANAGER_INVENTORY = "MANAGER_MAIN_MENU5";
	public static final String MANAGER_ORDERS = "MANAGER_MAIN_MENU6";
	public static final String MANAGER_NOTIFICATION = "MANAGER_MAIN_MENU7";
	public static final String MANAGER_NEW_CAR_MODEL = "MANAGER_MAIN_MENU8";
	public static final String MANAGER_CAR_SERVICE_DETAILS = "MANAGER_MAIN_MENU9";
	public static final String MANAGER_SERVICE_HISTORY = "MANAGER_MAIN_MENU10";
	public static final String MANAGER_INVOICES = "MANAGER_MAIN_MENU11";
	public static final String LOGIN_PAGE = LOGIN_MAIN_MENU+1;
	public static final String SIGN_UP_PAGE = LOGIN_MAIN_MENU+2;
	public static final String LOGIN_VERIFICATION = LOGIN_PAGE+1;
	public static final String EXIT = "LOGIN_MAIN_MENU"+3;
}
