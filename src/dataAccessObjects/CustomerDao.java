package dataAccessObjects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import databaseUtilities.DatabaseUtil;
import models.Customer;

public class CustomerDao {
	
	
	public Customer getCustomerProfile(int id) {
		PreparedStatement statement = null;
		Customer  customer=null;
		String qry = "SELECT * FROM CUSTOMER  WHERE CID = ?" ;
		DatabaseUtil db = new DatabaseUtil();
		try {
			Connection conn=db.establishConnection();
		
			statement = conn.prepareStatement(qry);
			statement.setInt(1, id);
			ResultSet rs = statement.executeQuery();
			
			
			if (rs.next())
				{
				customer=new Customer(id);
				customer.setAddress(rs.getString("ADDRESS"));
				customer.setcName(rs.getString("CNAME"));
				customer.setEmail(rs.getString("EMAIL"));
				customer.setPhone(rs.getLong("PHONE"));
				CarDao carDao=new CarDao();
				customer.setCarsOwned(carDao.getCarsOwnedByCustomer(id));
		
				}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return customer;
		
	}
	
	
}
