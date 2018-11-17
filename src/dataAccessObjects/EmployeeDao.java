package dataAccessObjects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import databaseUtilities.DatabaseUtil;
import models.Customer;
import models.Employee;
import models.Part;

public class EmployeeDao {
	
	 public static Date addDays(Date date, int days)
	    {
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(date);
	        cal.add(Calendar.DATE, days); //minus number would decrement the days
	        return cal.getTime();
	    }
	 public static Date addHours(Date date, int hours)
	    {
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(date);
	        cal.add(Calendar.HOUR, hours); //minus number would decrement the days
	        return cal.getTime();
	    }
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

	public ArrayList<Employee> getFreeMechanic(int centerId, int hours) {
		String qry = " select sch.SDATE,serv.EID,  extract(hour from numtodsinterval( (sum  ( SUBSTR(ENDTIME-STARTTIME  , 1, 2)*60 + SUBSTR(ENDTIME-STARTTIME  , 4, 2)*60 + SUBSTR(ENDTIME-STARTTIME  , 7, 2))), 'SECOND'))  as intv " + 
				" FROM EMP_SERVICE serv LEFT JOIN SCHEDULE sch  on   serv.EID = sch.MECHID and serv.ROLE='Mechanic' and serv.SERVICECENTERID=? and ( (sch.SDATE is null  or sch.sdate>=CURRENT_DATE   )  ) GROUP BY sch.SDATE,serv.EID order by intv,SDATE " + 
				"";
		PreparedStatement statement = null;
		String qry2 = " select ENDTIME from SCHEDULE where MECHID=? and SDATE=? order by ENDTIME Desc ";
		ArrayList<Employee> employees=new ArrayList<>();
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		 DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Employee  employee=null;
		DatabaseUtil db = new DatabaseUtil();
		try {
			Connection conn=db.establishConnection();
		
			statement = conn.prepareStatement(qry);
			statement.setInt(1,centerId);
			ResultSet rs = statement.executeQuery();
			
			Date dt=dateFormat.parse(date);
			while  ( employees.size()<2)
				{
				employee=new Employee();
				if (rs.next()) {
					if ( rs.getDate("SDATE")==null  || dateFormat.parse(rs.getDate("SDATE").toString()) !=dt) {
						
						employee.seteId(rs.getInt("EID"));
						employee.setsDate(dt);
						employees.add(employee);
						dt= addDays(dt, 1);
					}else if (11-rs.getInt("intv")>hours) {
						
	
						employee.seteId(rs.getInt("EID"));
						employee.setsDate(rs.getDate("SDATE"));
						employees.add(employee);
						
					}
					}
				 else{
					if (employees.size()==1) {
				
						employee.seteId(employees.get(0).geteId());
						employee.setsDate(dt);
						employees.add(employee);
						dt= addDays(dt, 1);
					
			
				}
				 }}
			for ( int i=0;i<employees.size();i++) {
				
			
				statement = conn.prepareStatement(qry2);
				statement.setInt(1,employees.get(i).geteId());
				statement.setDate(2,new java.sql.Date( employees.get(i).getsDate().getTime()));
				rs = statement.executeQuery();
				
				if (rs.next()) {
					employees.get(i).setStartTime( new Date(rs.getTimestamp("ENDTIME").getTime()));
					
				}
				
				else {
					 String time = " 08:00:00.0";
					 	
				    Date date2 = dateFormat2.parse(dateFormat.format(employees.get(i).getsDate()) + time);     
					employees.get(i).setStartTime(date2);
					employees.get(i).setsDate(employees.get(i).getsDate() );
					
				}
			}
					
				
			
			
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return employees;
	}

	public ArrayList<Date> getStartDate(int mechanicId,int hours) {
		String qry = " select sch.SDATE, extract(hour from numtodsinterval( (sum  ( SUBSTR(ENDTIME-STARTTIME  , 1, 2)*60 + SUBSTR(ENDTIME-STARTTIME  , 4, 2)*60 + SUBSTR(ENDTIME-STARTTIME  , 7, 2))), 'SECOND'))  as intv " + 
				" FROM EMP_SERVICE serv LEFT JOIN SCHEDULE sch  on   serv.EID = sch.MECHID where serv.ROLE='Mechanic' and serv.EID=?  and ( (sch.SDATE is null  or sch.sdate>=CURRENT_DATE   )  ) GROUP BY sch.SDATE order by intv,SDATE " + 
				"";
		PreparedStatement statement = null;
		String qry2 = " select ENDTIME from SCHEDULE where MECHID=? and SDATE=? order by ENDTIME Desc ";
		
		ArrayList<Date> dates=new ArrayList<>();
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		 DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
		DatabaseUtil db = new DatabaseUtil();
		try {
			Connection conn=db.establishConnection();
		
			statement = conn.prepareStatement(qry);
			statement.setInt(1,mechanicId);
			ResultSet rs = statement.executeQuery();
			
			Date dt=dateFormat.parse(date);
			while  (dates.size()<2 )
				{ 
				if (rs.next()){
				if (rs.getDate("SDATE")==null || dateFormat.parse(rs.getDate("SDATE").toString()) !=dt) {
					 dates.add(dt);
				    dt= addDays(dt, 1);
					
				}else if (11-rs.getInt("intv")>hours) {
					

					 dates.add(rs.getDate("SDATE"));
					
				}
				}
				else {
					if (dates.size()==1) {
						
						
						 dates.add(dt);
						 dt= addDays(dt, 1);
					
				}
				}}
			
			for ( int i=0;i<dates.size();i++) {
				
				
				statement = conn.prepareStatement(qry2);
				statement.setInt(1,mechanicId);
				statement.setDate(1,new java.sql.Date( dates.get(i).getTime()) );
				rs = statement.executeQuery();
				
				if (rs.next()) {
					dates.set(i,  new Date(rs.getTimestamp("ENDTIME").getTime()));
					
				}
				
				else {
					 String time = " 08:00:00";
					 	
				    Date date2 = dateFormat2.parse(dateFormat.format(dates.get(i)) + time);     
				    dates.set(i, date2);
					
				}
			}
			
				
				
			
			
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return dates;
	}
	public void updateMechHours(int employee, int hour) {
		// TODO Auto-generated method stub
		PreparedStatement statement = null;
		Connection conn=null;
		String qry = "UPDATE MECH_HOURS set HOURS = HOURS+? WHERE EID = ?";
		DatabaseUtil db = new DatabaseUtil();
		try {
			conn=db.establishConnection();
			
			statement = conn.prepareStatement(qry);
			statement.setInt(1, hour);
			statement.setInt(2, employee);
			
			statement.executeUpdate();
			
			if (statement != null) {
				statement.close();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			db.closeConnection();
			

		}
		
	}
	
	public void updateEmployee(Employee emp) {
		DatabaseUtil db = new DatabaseUtil();
		Connection conn;
		conn = db.establishConnection();
		Statement st = null;
		String qry = "UPDATE EMPLOYEE set ADDRESS = '" + emp.getAddress() + "' , ENAME = '" + emp.geteName() + "' , EMAIL = '" + emp.getEmail() + "' , PHONE = " + emp.getPhone() + " WHERE EID = " + emp.geteId();
		try {
			st = conn.createStatement();
			st.executeUpdate(qry);
			st.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
				try {
					if(st != null)
						st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}
	
	public Employee getEmployeeProfile(int id) {
		Employee emp = new Employee();
		DatabaseUtil db = new DatabaseUtil();
		Connection conn;
		conn = db.establishConnection();
		ResultSet rs = null;
		Statement st = null;
		String query = "select E.eid , E.ename , E.address ,  E.email , E.phone , ES.servicecenterid , P.startdate from EMPLOYEE E , EMP_SERVICE ES , PAYROLL P where E.eid = ES.eid and ES.eid = P.eid and E.eid = " + id;
		try {
			st = conn.createStatement();
			rs = st.executeQuery(query);
			if(rs.next()) {
				emp.seteId(rs.getInt(1));
				emp.seteName(rs.getString(2));
				emp.setAddress(rs.getString(3));
				emp.setEmail(rs.getString(4));
				emp.setPhone(rs.getLong(5));
				emp.setServiceCenter(rs.getInt(6));
				emp.setsDate(rs.getDate(7));
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
		
		return emp;
	}
}
