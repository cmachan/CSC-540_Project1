package databaseUtilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import constants.*;

public class DatabaseUtil {
	
	Connection conn = null;
	
	public void getConnection() {
	   try{
	      Class.forName(CONSTANTS.JDBC_DRIVER);
	      conn = DriverManager.getConnection(CONSTANTS.dbUrl,CONSTANTS.dbUserName,CONSTANTS.dbPassword);
	   }catch(SQLException se){
	      se.printStackTrace();
	   }catch(Exception e){
	      e.printStackTrace();
	   }
	}
	
	public void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
