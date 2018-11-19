package dataAccessObjects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import databaseUtilities.DatabaseUtil;
import models.BaseService;
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
		finally {
			db.closeConnection();
			

		}
		
		return customer;
		
	}

	public void updateCustomerProfile(Customer customer) {
		PreparedStatement statement = null;
		Connection conn=null;
		String qry = "UPDATE CUSTOMER set CNAME = ? , EMAIL = ? , PHONE = ? , ADDRESS = ? WHERE CID = ?";
		
		DatabaseUtil db = new DatabaseUtil();
		try {
			conn=db.establishConnection();
		
			statement = conn.prepareStatement(qry);
			statement.setString(1, customer.getcName());
			statement.setString(2, customer.getEmail());
			statement.setLong(3, customer.getPhone());
			statement.setString(4, customer.getAddress());
			statement.setInt(5, customer.getcId());
			statement.executeUpdate();
			
			if (statement != null) {
				statement.close();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			db.closeConnection();
			

		}
		
	}

	public void updateCustomerService(int getcId, ArrayList<BaseService> baseServices, Date startTime, int repairId, boolean reschedule) {
		
		// TODO Auto-generated method stub
		PreparedStatement statement = null;
		Connection conn=null;
		DatabaseUtil db = new DatabaseUtil();
		
		try {
			conn=db.establishConnection();
			if (reschedule) {
				String qry = "UPDATE customer_service set lastservice = ? where  lastserviceid=?";
				statement = conn.prepareStatement(qry);
				
				statement.setInt(2, repairId);
				statement.setDate(1, new java.sql.Date(startTime.getTime()));
				statement.executeUpdate();
				
			}
			else {	
			for (int i=0;i<baseServices.size();i++) {
					int bid=baseServices.get(i).getbId();
		
					String qry = "UPDATE customer_service set lastservice = ?,lastserviceid=? where BID =? and CID =?  ";
					String qry2 = "INSERT INTO customer_service(BID,CID,LASTSERVICE) VALUES (?,?,?)  ";
				
						statement = conn.prepareStatement(qry);
					statement.setInt(3, bid);
					statement.setInt(4, getcId);
					statement.setInt(2, repairId);
					statement.setDate(1, new java.sql.Date(startTime.getTime()));
					int affectedRows =statement.executeUpdate();
					if (affectedRows==0) {
						statement = conn.prepareStatement(qry2);
						statement.setInt(1, bid);
						statement.setInt(2, getcId);
						statement.setDate(3, new java.sql.Date(startTime.getTime()));
						statement.executeUpdate();
					}
			}
			
				if (statement != null) {
					statement.close();
				}

			
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			db.closeConnection();
			

		}
	
		
	}
	
	
}
