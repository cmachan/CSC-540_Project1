package databaseUtilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import constants.CONSTANTS;

public class DatabaseUtil {
	
	Connection conn = null;
	
	public Connection establishConnection() {
	   try{
	      Class.forName(CONSTANTS.JDBC_DRIVER);
	      conn = DriverManager.getConnection(CONSTANTS.dbUrl,CONSTANTS.dbUserName,CONSTANTS.dbPassword);
	      
	   }catch(SQLException se){
	      se.printStackTrace();
	   }catch(Exception e){
	      e.printStackTrace();
	   }
	   return conn;
	}
	
	public void closeConnection() {
		try {
			conn.commit();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> getAll(String tableName , String colName , String val , boolean isNumber) {
		ResultSet rs = null;
		ArrayList<String> res = new ArrayList<String>();
		ResultSetMetaData metaData = null;
		Statement st = null;
		String query;
		try {
			st = conn.createStatement();
			if(isNumber)
				query = "SELECT * FROM " + tableName + " WHERE " + colName + " = " + val;
			else
				query = "SELECT * FROM " + tableName + " WHERE " + colName + " = '" + val + "'";
			
			rs = st.executeQuery(query);
//			System.out.println("Query Executed: " + query);
			metaData = rs.getMetaData(); 
			int colCount = metaData.getColumnCount();
			while (rs.next()) {
				for(int i=1;i<=colCount;i++) {
					res.add(rs.getString(i));
				}
				System.out.println();
			}
			rs.close();
			st.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
				try {
					if(rs != null)
						rs.close();
					
					if(st != null)
						st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return res;
	}
	
	public void runQuery(String query) {
		Statement st = null;
		try {
			st = conn.createStatement();
			st.executeUpdate(query);
//			System.out.println("Query Executed: " + query);
			st.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(st != null)
					st.close();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
}
