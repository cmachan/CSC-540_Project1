package dataAccessObjects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import databaseUtilities.DatabaseUtil;
import models.Customer;
import models.Employee;

public class EmployeeDao {
	
	
	
	public ArrayList<Employee> getAllMechanic() {
		String qry = "select emp.ENAME,emp.EID,serv.SERVICECENTERID FROM EMPLOYEE emp,EMP_SERVICE serv WHERE emp.EID = serv.EID and serv.ROLE='Mechanic' ";
		PreparedStatement statement = null;
		ArrayList<Employee> mechanics=new ArrayList<Employee>();
		Employee  employee=null;
		DatabaseUtil db = new DatabaseUtil();
		try {
			Connection conn=db.establishConnection();
		
			statement = conn.prepareStatement(qry);
		
			ResultSet rs = statement.executeQuery();
			
			
			while (rs.next())
				{
				employee=new Employee();;
				
				employee.seteId(rs.getInt("EID"));
				employee.seteName(rs.getString("ENAME"));
				employee.setServiceCenter(rs.getInt("SERVICECENTERID"));
				mechanics.add(employee);
		
				}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mechanics;
	}
	
	
}
