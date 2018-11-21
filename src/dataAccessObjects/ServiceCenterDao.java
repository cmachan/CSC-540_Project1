package dataAccessObjects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import databaseUtilities.DatabaseUtil;
import models.Center;

public class ServiceCenterDao {

	public ArrayList<Center> getAllCenters() {
		PreparedStatement statement = null;
		
		String qry1="select * from SERVICECENTER";
		ArrayList<Center> centers=new ArrayList<>();
		DatabaseUtil db = new DatabaseUtil();
		try {
			Connection conn=db.establishConnection();
		
			statement = conn.prepareStatement(qry1);
			
			ResultSet rs = statement.executeQuery();
			while (rs.next())
				{
				Center center=new Center(); 
				center.setId(rs.getInt("ID"));
				center.setName(rs.getString("NAME"));
				centers.add(center);
				
				}
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}finally {
			db.closeConnection();
			

		}
		
		return centers;
	}
	
	
	
	
	
	
}
