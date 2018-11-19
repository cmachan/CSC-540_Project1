package constants;

public class CONSTANTS {
	/*
	 * CONSTANTS for Database Connections.
	 */
	public static final String dbUserName = "rsethi3";
	public static final String dbPassword = "200241349";
	public static final String dbUrl = "jdbc:oracle:thin:@orca.csc.ncsu.edu:1521:ORCL01";
	public static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
	public static final String CUSTOMER_PROFILE= CONSTANTS.CUSTOMER_MAIN_MENU+"1";
	public static final String CUSTOMER_REGISTER_CAR= CONSTANTS.CUSTOMER_MAIN_MENU+"2";
	public static final String CUSTOMER_MAIN_MENU= "CUSTOMER_MAIN_MENU";
	public static final String  CUSTOMER_VIEW_PROFILE = CONSTANTS.CUSTOMER_PROFILE+"1";
	public static final String  CUSTOMER_SERVICE= CONSTANTS.CUSTOMER_MAIN_MENU+"3";
	public static final String  CUSTOMER_SERVICE_HISTORY= CONSTANTS.CUSTOMER_SERVICE+"1";
	public static final String  CUSTOMER_SERVICE_SCHEDULE= CONSTANTS.CUSTOMER_SERVICE+"2";
	public static final String  CUSTOMER_SERVICE_RESCHEDULE= CONSTANTS.CUSTOMER_SERVICE+"3";
	public static final String  CUSTOMER_SERVICE_INVOICE= CONSTANTS.CUSTOMER_MAIN_MENU+"4";
	public static final String  CUSTOMER_INVOICE= CONSTANTS.CUSTOMER_MAIN_MENU+"4";
	public static final String  CUSTOMER_LOGOUT= CONSTANTS.CUSTOMER_MAIN_MENU+"5";
	public static final String  CUSTOMER_UPDATE_PROFILE = CONSTANTS.CUSTOMER_PROFILE+"2";
	public static final String STATUS_PENDING = "Pending";
	public static final String STATUS_COMPLETE = "Complete";
}
