package dataAccessObjects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import constants.CONSTANTS;
import models.Part;
import databaseUtilities.DatabaseUtil;
import models.BaseService;
import models.Car;
import models.Repair;

public class RepairDao {

	public ArrayList<Repair> getServiceHistory(int cId) {
		
		PreparedStatement statement = null;
		ArrayList<Repair> repairs=new ArrayList<Repair>();
		String qry = "SELECT rep.INVOICENUMBER,rep.CARID,rep.SERVICETYPE,emp.ENAME,sc.STARTTIME,sc.ENDTIME,rep.STATUS,rep.RDATE FROM REPAIR rep,EMPLOYEE emp,SCHEDULE sc WHERE rep.CID = ? and rep.MECHANICID = emp.EID and rep.RID=sc.RID " ;
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
				repair.setRdate((rs.getDate("RDATE")));
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

	public ArrayList<BaseService> getBaseServices(Repair service) {
		// TODO Auto-generated method stub
		int diffMileage=service.getCar().getNewMileage()-service.getCar().getLastMileage();

		PreparedStatement statement = null;
		ArrayList<BaseService> baseServices=new ArrayList<BaseService>();
		String qry = "SELECT bs.BID,bs.LABOURCHARGE,bs.HRS,ms.MID from BASICSERVICE bs,MAINTENANCESERVICE ms,BASICMAINTENANCEMAP bsms where ms.MILES=? and ms.MID=bsms.MID and bsms.BID=bs.BID " ;
		DatabaseUtil db = new DatabaseUtil();
		try {
			Connection conn=db.establishConnection();
		
			statement = conn.prepareStatement(qry);
			statement.setInt(1,diffMileage);
			ResultSet rs = statement.executeQuery();
			
			
			while (rs.next())
				{
				BaseService bs=new BaseService();
				bs.setbId(rs.getInt("BID"));
				bs.setLabourCharge(rs.getString("LABOURCHARGE"));
				
				bs.setHour(rs.getInt("HRS"));
				service.setMid(rs.getInt("MID"));
				baseServices.add(bs);
				}
			service.setServiceType("Maintainance");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return baseServices;
	}

	

	public void placeOrder(ArrayList<Part> parts) {
		
		
	}

	public void checkUpdateDates(int hours, int centerId) {
		// TODO Auto-generated method stub
	// TODO Auto-generated method stub
		
		
		PreparedStatement statement = null;
		ArrayList<Part> parts=new ArrayList<Part>();
		String qry = "select bspart.PARTID,inv.MINQTYORDER,dis.DELIVERYTIME from BASICSERVICE_PART bspart,INVENTORY inv,INVENTORY_PARTS inpart,"
				+ " DISTRIBUTOR dis where bspart.PARTID=inv.PARTID and inpart.PARTID=inv.PARTID and inpart.DISTRIBUTORID=dis.DID and inv.SERVICECENTERID=?  and  (inv.QUANTITY-inv.THRESHOLD)<bspart.QUANTITY and bspart.BID in (select BID from BASICMAINTENANCEMAP where MID =?)" ;
		
		DatabaseUtil db = new DatabaseUtil();
		try {
			Connection conn=db.establishConnection();
		
			statement = conn.prepareStatement(qry);
			statement.setInt(1,centerId);
			ResultSet rs = statement.executeQuery();
			
			
			while (rs.next())
				{
				Part ps=new Part();
				ps.setPartID(rs.getInt("PARTID"));
				ps.setMinQuantity(rs.getInt("MINQTYORDER"));
				ps.setDeliveryTime(rs.getInt("DELIVERYTIME"));
				parts.add(ps);
				}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int insertRepair(Repair service) {
		// TODO Auto-generated method stub
		PreparedStatement statement = null;
		Connection conn=null;
		DateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");
		String qry = "Insert into REPAIR(CID,FEES,CARID,MECHANICID,FAULTS,SERVICETYPE,TIMESLOT,MID,RDATE,CENTERID,STATUS) values(?,?,?,?,?,?,?,?,?,?,?)";
		DatabaseUtil db = new DatabaseUtil();
		try {
			conn=db.establishConnection();
			
			statement = conn.prepareStatement(qry);
			statement.setInt(1, service.getcId());
			statement.setFloat(2, service.getFees());
			statement.setString(3, service.getCar().getLicensePlate());
			statement.setInt(4, service.getMechanicId());
			statement.setString(5, service.getFaults());
			statement.setString(6, service.getServiceType());
			statement.setString(7, dateFormat2.format(service.getStartTime())+"-"+dateFormat2.format(service.getEndTime()));
			statement.setInt(8, service.getMid());
			statement.setDate(9,new java.sql.Date( service.getStartTime().getTime()));
			statement.setInt(10,service.getCenterId());
			statement.setString(11,CONSTANTS.STATUS_PENDING);
			statement.executeUpdate();
			PreparedStatement ps = conn
		                .prepareStatement("SELECT REPAIR_SEQ1.currval FROM SYS.DUAL");
		         
		        ResultSet rs = ps.executeQuery();
		        if (rs.next()) {
		            return (int) rs.getLong(1);
		        }

			if (statement != null) {
				statement.close();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			db.closeConnection();
			

		}
		return -1;
	}

	public void insertSchedule(Repair service) {
		
		PreparedStatement statement = null;
		Connection conn=null;
		String qry = "Insert into SCHEDULE(RID,STARTTIME,ENDTIME,SDATE,CENTERID,MECHID) values(?,?,?,?,?,?)";
		DatabaseUtil db = new DatabaseUtil();
		try {
			conn=db.establishConnection();
			
			statement = conn.prepareStatement(qry);
			statement.setInt(1, service.getrId());
			statement.setTimestamp(2, new java.sql.Timestamp(service.getStartTime().getTime()));
			statement.setTimestamp(3,  new java.sql.Timestamp(service.getEndTime().getTime()));
			statement.setDate(4, new java.sql.Date(service.getStartTime().getTime()));
			statement.setInt(5, service.getCenterId());
			statement.setInt(6, service.getMechanicId());
			
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

	
	
	
}
