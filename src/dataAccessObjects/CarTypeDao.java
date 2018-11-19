package dataAccessObjects;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import databaseUtilities.*;
import models.Customer;

public class CarTypeDao {

	
	public int insertCarType(String make,String model) {
		PreparedStatement statement = null;
		String qry2 = "INSERT  INTO CARTYPE (MAKE,MODEL)  VALUES (?,?)";

		DatabaseUtil db = new DatabaseUtil();
		   try {
					Connection conn=db.establishConnection();
					
			         statement = conn.prepareStatement(qry2,
			                                      Statement.RETURN_GENERATED_KEYS);
			     
			        statement.setString(1, make);
			        statement.setString(2, model);
			      
			        // ...

			        statement.executeUpdate();
			        PreparedStatement ps = conn
			                .prepareStatement("SELECT CARTYPE_SEQ.currval FROM SYS.DUAL");
			         
			        ResultSet rs = ps.executeQuery();
			        if (rs.next()) {
			            return (int) rs.getLong(1);
			        }

			       
			        			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   return -1;
	}
	
	
	
	public int getCarType(String make,String model) {
		PreparedStatement statement = null;
		
		String qry1="select CARTYPEID from CARTYPE where MAKE=? and MODEL=?";
		
		DatabaseUtil db = new DatabaseUtil();
		try {
			Connection conn=db.establishConnection();
		
			statement = conn.prepareStatement(qry1);
			statement.setString(1, make);
			statement.setString(2, model);
			ResultSet rs = statement.executeQuery();
			if (rs.next())
				{
				return rs.getInt("CARTYPEID");
				
				}
			else {
				return insertCarType( make, model); 
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
		 
	}
}

