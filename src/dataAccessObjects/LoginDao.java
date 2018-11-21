package dataAccessObjects;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import databaseUtilities.DatabaseUtil;
import models.BaseService;
import models.Login;

public class LoginDao {
	
	
	public boolean getUserProfile(Login login) {
		PreparedStatement statement = null;
		
		String qry = "SELECT CUSTOMER.ADDRESS as addr,CUSTOMER.CNAME as name ,CUSTOMER.EMAIL as email,CUSTOMER.PHONE as phone ,CUSTOMER.CID as id, 'CUSTOMER' as role FROM CUSTOMER,LOGIN  WHERE LOGIN.ID=? and LOGIN.PASSWORD=? and LOGIN.ID=CUSTOMER.EMAIL "
				+ "UNION"
				+ " SELECT EMPLOYEE.ADDRESS as addr,EMPLOYEE.ENAME as name ,EMPLOYEE.EMAIL as email,EMPLOYEE.PHONE as phone ,EMPLOYEE.EID as id,"
				+ " EMP_SERVICE.ROLE as role FROM EMPLOYEE,LOGIN,EMP_SERVICE  WHERE"
				+ " LOGIN.ID=? and LOGIN.PASSWORD=? and LOGIN.ID=CAST( EMPLOYEE.EID AS varchar2(30))  and EMPLOYEE.EID=EMP_SERVICE.EID " ;
		DatabaseUtil db = new DatabaseUtil();
		try {
			Connection conn=db.establishConnection();
		
			statement = conn.prepareStatement(qry);
			statement.setString(1, login.getuId());
			statement.setString(2, login.getPass());
			statement.setString(3, login.getuId());
			statement.setString(4, login.getPass());
			ResultSet rs = statement.executeQuery();
			
			
			if (rs.next())
				{
				
				login.setAddress(rs.getString("addr"));
				login.setName(rs.getString("name"));
				login.setEmail(rs.getString("email"));
				login.setPhoneNumber(rs.getString("phone"));
				login.setRole(rs.getString("role"));
				return true;
				}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}finally {
			db.closeConnection();
			

		}
		
		return false;
	}
	
	

	public boolean insertCustomer(Login login) {
		
		PreparedStatement statement = null;
		Connection conn=null;
		DatabaseUtil db = new DatabaseUtil();
		
		try {
			conn=db.establishConnection();
			String qry2 = "INSERT INTO  CUSTOMER (CNAME,EMAIL,PHONE,ADDRESS,CENTERID) VALUES (?,?,?,?,?)  ";
				
					statement = conn.prepareStatement(qry2);
						
					statement.setString(1, login.getName());
					statement.setString(2, login.getEmail());
				
					String ph=(login.getPhoneNumber().replaceAll("-", ""));
					statement.setString(3, ph.replaceAll("\\.", ""));

					statement.setString(4, login.getAddress());
					statement.setInt(5, login.getCenterID());
					statement.executeUpdate();
				        PreparedStatement ps = conn
				                .prepareStatement("SELECT CUSTOMER_SEQ.currval FROM SYS.DUAL");
				         
				        ResultSet rs = ps.executeQuery();
				        if (rs.next()) {
				            login.setuId(login.getEmail());
				        }

			
				if (statement != null) {
					statement.close();
				}
				return true;
			
		
		} catch (SQLException e) {
			if (e.getSQLState().equals("23000")) {
				System.out.println("Customer with same email present");
			}
			else {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			}
		}finally {
			db.closeConnection();
			

		}
		return false;
	}

	public boolean insertEmployee(Login login) {
		PreparedStatement statement = null;
		Connection conn=null;
		DatabaseUtil db = new DatabaseUtil();
		
		try {
			conn=db.establishConnection();
			String qry = "INSERT INTO EMPLOYEE (ENAME,EMAIL,PHONE,ADDRESS) values (?,?,?,?)  ";
				
					statement = conn.prepareStatement(qry);
						
					statement.setString(1, login.getName());
					statement.setString(2, login.getEmail());

					String ph=(login.getPhoneNumber().replaceAll("-", ""));
					statement.setString(3, ph.replaceAll("\\.", ""));
					statement.setString(4, login.getAddress());
					statement.executeUpdate();
				        PreparedStatement ps = conn
				                .prepareStatement("SELECT EMPLOYEE_SEQ.currval FROM SYS.DUAL");
				         
				        ResultSet rs = ps.executeQuery();
				        if (rs.next()) {
				        	
				            login.setuId(rs.getString(1));
				            String qry2 = "INSERT INTO EMP_SERVICE VALUES values (?,?,?)  ";
				            statement = conn.prepareStatement(qry2);
							
							statement.setString(1, login.getuId());
							statement.setString(2, login.getRole());
							statement.setInt(2, login.getCenterID());
							statement.executeUpdate();
				        }

			
				if (statement != null) {
					statement.close();
				}

			return true;
		
		} catch (SQLException e) {
			if (e.getSQLState()!=null && e.getSQLState().equals("23000")) {
				System.out.println("Employee with same id present");
			}
			else {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			}
		}finally {
			db.closeConnection();
			

		}
		return false;
	}
		
	

	public void insertLogin(Login login) {
		PreparedStatement statement = null;
		Connection conn=null;
		DatabaseUtil db = new DatabaseUtil();
		
		try {
			conn=db.establishConnection();
			
					String qry2 = "INSERT INTO login VALUES (?,?)  ";
				
						statement = conn.prepareStatement(qry2);
						
					statement.setString(1, login.getuId());
					statement.setString(2, login.getPass());
					statement.executeUpdate();
			
				if (statement != null) {
					statement.close();
				}

			
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}finally {
			db.closeConnection();
			

		}
	}



	public void updatepassword(String id, String password) {
		PreparedStatement statement = null;
		Connection conn=null;
		DatabaseUtil db = new DatabaseUtil();
		
		try {
			conn=db.establishConnection();
			
					String qry2 = "UPDATE  login set password=? where id=?  ";
				
						statement = conn.prepareStatement(qry2);
						
					statement.setString(2, id);
					statement.setString(1, password);
					statement.executeUpdate();
			
				if (statement != null) {
					statement.close();
				}

			
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}finally {
			db.closeConnection();
			

		}
	}
	
	
}
