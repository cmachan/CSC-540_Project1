package dataAccessObjects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import databaseUtilities.DatabaseUtil;
import models.Car;
import models.Repair;

public class RepairDao {

	public ArrayList<Repair> getServiceHistory(int cId) {
		
		PreparedStatement statement = null;
		ArrayList<Repair> repairs=new ArrayList<Repair>();
		String qry = "SELECT rep.INVOICENUMBER,rep.CARID,rep.SERVICETYPE,emp.ENAME,sc.STARTTIME,sc.ENDTIME,rep.STATUS FROM REPAIR rep,EMPLOYEE emp,SCHEDULE sc WHERE rep.CID = ? and rep.MECHANICID = emp.EID and rep.RID=sc.RID " ;
		DatabaseUtil db = new DatabaseUtil();
		try {
			Connection conn=db.establishConnection();
		
			statement = conn.prepareStatement(qry);
			statement.setInt(1,cId);
			ResultSet rs = statement.executeQuery();
			
			
			while (rs.next())
				{
				Repair repair=new Repair();
				repair.setInvoiceNumber(rs.getString("INVOICENUMBER"));
				Car car=new Car();
				car.setLicensePlate(rs.getString("CARID"));
				repair.setCar(car);
				repair.setServiceType(rs.getString("SERVICETYPE"));
				repair.setMechanicName(rs.getString("ENAME"));
				repair.setStartTime(rs.getTime("STARTTIME"));
				repair.setEndTime(rs.getTime("ENDTIME"));
				repair.setStatus(rs.getString("STATUS"));
				repairs.add(repair);
				}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return repairs;
		
		// TODO Auto-generated method stub
		
	}

	
	
	
}
