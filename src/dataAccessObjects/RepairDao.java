package dataAccessObjects;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import constants.CONSTANTS;
import models.Part;
import databaseUtilities.DatabaseUtil;
import models.BaseService;
import models.Car;
import models.CarType;
import models.Fault;
import models.Invoice;
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
//			e.printStackTrace();
		} finally {
			db.closeConnection();
		}
		return repairs;
		
		// TODO Auto-generated method stub
		
	}

	public ArrayList<BaseService> getBaseServices(Repair service) {
		// TODO Auto-generated method stub
		int diffMileage=service.getCar().getNewMileage()-service.getCar().getLastMileage();
		diffMileage=diffMileage/1000;
		PreparedStatement statement = null;
		ArrayList<BaseService> baseServices=new ArrayList<BaseService>();
		String qry = "SELECT cs.LASTSERVICE,bs.BID,bs.LABOURCHARGE,bs.HRS,bsms.MID from BASICMAINTENANCEMAP bsms,BASICSERVICE bs left join customer_service cs on bs.BID =cs.BID and cs.CID=? where bsms.BID=bs.BID and  bsms.MID="
				+ "(select MID from ("
				+ " select max(MID) as MID   from maintenanceservice  where  MILES<? and CARTYPEID=?"
				+ " union " + 
				" select min(MID) as MID   from maintenanceservice  where  MILES>? and CARTYPEID=? " + 
				") where rownum =1)" ;
		DatabaseUtil db = new DatabaseUtil();
		try {
			Connection conn=db.establishConnection();
		
			statement = conn.prepareStatement(qry);
			statement.setInt(1,service.getcId());
			statement.setInt(2,diffMileage);
			statement.setInt(3,service.getCar().getCarTypeID());
			statement.setInt(4,diffMileage);
			statement.setInt(5,service.getCar().getCarTypeID());
			ResultSet rs = statement.executeQuery();
			InventoryDao inv=new InventoryDao();
			
			while (rs.next())
				{
				BaseService bs=new BaseService();
				bs.setbId(rs.getInt("BID"));
				bs.setLabourCharge(rs.getInt("LABOURCHARGE"));
				bs.setLastService(rs.getDate("LASTSERVICE"));
				bs.setHour(rs.getFloat("HRS"));
				bs.setParts(inv.getParts(bs.getbId(),service.getCar().getCarTypeID(),service.getCar().getMake()));
				service.setMid(rs.getInt("MID"));
				baseServices.add(bs);
				}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		} finally {
			db.closeConnection();
		}
		return baseServices;
	}

	public ArrayList<BaseService> getBaseServices(int mid) {
		// TODO Auto-generated method stub
		
		PreparedStatement statement = null;
		ArrayList<BaseService> baseServices=new ArrayList<BaseService>();
		String qry = "select * from BASICMAINTENANCEMAP bcms ,BASICSERVICE bs where bs.bid=bcms.bid and bcms.mid=?" ;
		DatabaseUtil db = new DatabaseUtil();
		try {
			Connection conn=db.establishConnection();
		
			statement = conn.prepareStatement(qry);
			statement.setInt(1,mid);
			
			ResultSet rs = statement.executeQuery();
			InventoryDao inv=new InventoryDao();
			
			while (rs.next())
				{
				BaseService bs=new BaseService();
				bs.setbId(rs.getInt("BID"));
				bs.setLabourCharge(rs.getInt("LABOURCHARGE"));
				bs.setHour(rs.getFloat("HRS"));
				bs.setName(rs.getString("BNAME"));
				baseServices.add(bs);
				}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		} finally {
			db.closeConnection();
		}
		return baseServices;
	}

	public void placeOrder(ArrayList<Part> parts) {
		String qry = "Insert into CAR_ORDER(ODATE,QTY,CENTERID,DISTRIBUTORID,STATUS,REQCENTERID,PARTID,EXPECTDELDATE,REQTYPE) values(CURRENT_DATE,?,?,?,?,?,?,?,?)";
		PreparedStatement statement = null;
		Connection conn=null;
		DatabaseUtil db = new DatabaseUtil();
		DateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");
		for (int i=0;i<parts.size();i++) {
			Part part=parts.get(i);
			try {
				conn=db.establishConnection();

				statement = conn.prepareStatement(qry);
				statement.setInt(1, part.getQuantity());
				statement.setInt(2, part.getServiceCenterId());
				statement.setString(4, CONSTANTS.STATUS_PENDING);
				statement.setInt(6,part.getPartID());
				statement.setDate(7,new Date(EmployeeDao.addDays(new java.util.Date(),part.getDeliveryTime()).getTime()));
			if (part.getAltcenter()!=-1) {
				statement.setString(3, null);
				statement.setString(8, "S");
				statement.setInt(5, part.getAltcenter());
			}else {
				statement.setString(5, null);
				statement.setString(8, "D");
				statement.setString(3, part.getDistributorId());
			}
			
				
				
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
		}finally {
			db.closeConnection();
			

		}
		
	}

	public int insertRepair(Repair service) {
		// TODO Auto-generated method stub
		PreparedStatement statement = null;
		Connection conn=null;
		DateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");
		String qry = "Insert into REPAIR(CID,FEES,CARID,MECHANICID,FAULT,SERVICETYPE,TIMESLOT,MID,RDATE,CENTERID,STATUS) values(?,?,?,?,?,?,?,?,?,?,?)";
		DatabaseUtil db = new DatabaseUtil();
		try {
			conn=db.establishConnection();
			
			
			statement = conn.prepareStatement(qry);
			statement.setInt(1, service.getcId());
			statement.setFloat(2, service.getFees());
			statement.setString(3, service.getCar().getLicensePlate());
			statement.setInt(4, service.getMechanicId());
			if (service.getFault()!=null) {
				statement.setInt(5, service.getFault().getfId());
			}
			else {
				statement.setString(5, null);
			}
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
		
		public ArrayList<Fault> getAllFaults() {
			
			

			PreparedStatement statement = null;
			ArrayList<Fault> faults=new ArrayList<Fault>();
			String qry = "SELECT * from fault " ;
			DatabaseUtil db = new DatabaseUtil();
			try {
				Connection conn=db.establishConnection();
			
				statement = conn.prepareStatement(qry);
				
				ResultSet rs = statement.executeQuery();
				
				
				while (rs.next())
					{
					Fault fault=new Fault();
					fault.setfId(rs.getInt("FID"));
					fault.setfName(rs.getString("FNAME"));
					fault.setDiagnostic(rs.getString("diagnostic"));
					fault.setFee(rs.getInt("FEE"));
					
					faults.add(fault);
					}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				db.closeConnection();
				

			}
			return faults;
	}

		public void getFaultDetails(Fault fault, int carType,int customerId,String make) {
			// TODO Auto-generated method stub

			PreparedStatement statement = null;
			ArrayList<BaseService> baseServices=new ArrayList<BaseService>();
			String qry = "SELECT  cs.LASTSERVICE, BS.BID,BS.BNAME,BS.LABOURCHARGE,BS.HRS from FAULT_SERVICE f,BASICSERVICE BS  left join customer_service cs on BS.BID =cs.BID and cs.CID=?  where f.FID=? and f.BID=BS.BID " ;
			DatabaseUtil db = new DatabaseUtil();
			try {
				Connection conn=db.establishConnection();
			
				statement = conn.prepareStatement(qry);
				statement.setInt(1,customerId);
				statement.setInt(2,fault.getfId());
				ResultSet rs = statement.executeQuery();
				InventoryDao inv=new InventoryDao();
				
				while (rs.next())
					{
					BaseService bs=new BaseService();
					bs.setbId(rs.getInt("BID"));
					bs.setName(rs.getString("BNAME"));
					bs.setLabourCharge(rs.getInt("LABOURCHARGE"));
					bs.setHour(rs.getFloat("HRS"));
					bs.setLastService(rs.getDate("LASTSERVICE"));
					bs.setParts(inv.getParts(bs.getbId(),carType,make));
					baseServices.add(bs);
					}
				fault.setBs(baseServices);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				db.closeConnection();
				

			}
			
		}

		public ArrayList<Repair> getUpcomingServices(int getcId) {
			PreparedStatement statement = null;
			ArrayList<Repair> repairs=new ArrayList<Repair>();
			String qry = "SELECT rep.RID,rep.CENTERID,rep.INVOICENUMBER,rep.CARID,rep.SERVICETYPE,emp.ENAME,sc.STARTTIME,sc.ENDTIME,rep.STATUS,rep.RDATE ,m.type as type " + 
					"FROM REPAIR rep, " + 
					"EMPLOYEE emp,SCHEDULE sc,maintenanceservice m WHERE rep.CID = ? and rep.MECHANICID = emp.EID and rep.RID=sc.RID and rep.RDATE>CURRENT_DATE and rep.mid =m.mid " + 
					"UNION " + 
					"SELECT rep.RID,rep.CENTERID,rep.INVOICENUMBER,rep.CARID,rep.SERVICETYPE,emp.ENAME,sc.STARTTIME,sc.ENDTIME,rep.STATUS,rep.RDATE ,f.fname as type " + 
					"FROM REPAIR rep, " + 
					"EMPLOYEE emp,SCHEDULE sc,fault f WHERE rep.CID = ? and rep.MECHANICID = emp.EID and rep.RID=sc.RID and rep.RDATE>CURRENT_DATE and rep.fault =f.fid";
				DatabaseUtil db = new DatabaseUtil();
			try {
				Connection conn=db.establishConnection();
			
				statement = conn.prepareStatement(qry);
				statement.setInt(1,getcId);
				statement.setInt(2,getcId);
				ResultSet rs = statement.executeQuery();
				
				
				while (rs.next())
					{
					Repair repair=new Repair();
					repair.setInvoiceNumber(rs.getString("INVOICENUMBER"));
					Car car=new Car();
					car.setLicensePlate(rs.getString("CARID"));
					repair.setCar(car);
					repair.setrId(rs.getInt("RID"));
					repair.setServiceType(rs.getString("SERVICETYPE"));
					repair.setMechanicName(rs.getString("ENAME"));
					repair.setStartTime(rs.getTime("STARTTIME"));
					repair.setEndTime(rs.getTime("ENDTIME"));
					repair.setRdate((rs.getDate("RDATE")));
					repair.setStatus(rs.getString("STATUS"));
					repair.setCenterId(rs.getInt("CENTERID"));
					repair.setServiceTypeDetail(rs.getString("TYPE"));
					
					repairs.add(repair);
					}
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				db.closeConnection();
				

			}
			
			return repairs;
			
		}

		public void updateSchedule(Repair service) {
			// TODO Auto-generated method stub

			PreparedStatement statement = null;
			Connection conn=null;
			String qry = "UPDATE  SCHEDULE set STARTTIME=?,ENDTIME=?,SDATE=?,MECHID=? WHERE RID = ? ";
			DatabaseUtil db = new DatabaseUtil();
			try {
				conn=db.establishConnection();
				
				statement = conn.prepareStatement(qry);
				statement.setTimestamp(1, new java.sql.Timestamp(service.getStartTime().getTime()));
				statement.setTimestamp(2,  new java.sql.Timestamp(service.getEndTime().getTime()));
				statement.setDate(3, new java.sql.Date(service.getStartTime().getTime()));
				statement.setInt(4, service.getCenterId());
				statement.setInt(5, service.getrId());
				
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

		public void updateRepair(Repair selectedRepair) {
			// TODO Auto-generated method stub
			PreparedStatement statement = null;
			Connection conn=null;

			DateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");
			String qry = "UPDATE REPAIR set MECHANICID =?,TIMESLOT=?,RDATE=?  WHERE RID = ? ";
			DatabaseUtil db = new DatabaseUtil();
			try {
				conn=db.establishConnection();
				
				statement = conn.prepareStatement(qry);
				statement.setInt(1, selectedRepair.getMechanicId());
			
				
				statement.setString(2, dateFormat2.format(selectedRepair.getStartTime())+"-"+dateFormat2.format(selectedRepair.getEndTime()));
			
				statement.setDate(3,new java.sql.Date( selectedRepair.getStartTime().getTime()));
				statement.setInt(4,selectedRepair.getrId());
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

		public HashMap<String, Repair> getCompletedServices(int getcId) {
			PreparedStatement statement = null;
			HashMap<String, Repair> repairs=new HashMap<String, Repair>();
			String qry = "SELECT rep.FEES, rep.RID,rep.CENTERID,rep.INVOICENUMBER,rep.CARID,rep.SERVICETYPE,emp.ENAME,sc.STARTTIME,sc.ENDTIME,rep.STATUS,rep.RDATE ,m.type as type " + 
					"FROM REPAIR rep, " + 
					"EMPLOYEE emp,SCHEDULE sc,maintenanceservice m WHERE rep.status=? and rep.CID = ? and rep.MECHANICID = emp.EID and rep.RID=sc.RID  and rep.mid =m.mid " + 
					"UNION " + 
					"SELECT rep.FEES, rep.RID,rep.CENTERID,rep.INVOICENUMBER,rep.CARID,rep.SERVICETYPE,emp.ENAME,sc.STARTTIME,sc.ENDTIME,rep.STATUS,rep.RDATE ,f.fname as type " + 
					"FROM REPAIR rep, " + 
					"EMPLOYEE emp,SCHEDULE sc,fault f WHERE rep.status=? and rep.CID = ? and rep.MECHANICID = emp.EID and rep.RID=sc.RID  and rep.fault =f.fid";
				DatabaseUtil db = new DatabaseUtil();
			try {
				Connection conn=db.establishConnection();
			
				statement = conn.prepareStatement(qry);
				statement.setString(1,CONSTANTS.STATUS_COMPLETE);
				statement.setInt(2,getcId);
				statement.setString(3,CONSTANTS.STATUS_COMPLETE);
				statement.setInt(4,getcId);
				
				ResultSet rs = statement.executeQuery();
				
				
				while (rs.next())
					{
					Repair repair=new Repair();
					repair.setInvoiceNumber(rs.getString("INVOICENUMBER"));
					Car car=new Car();
					car.setLicensePlate(rs.getString("CARID"));
					repair.setCar(car);
					repair.setrId(rs.getInt("RID"));
					repair.setServiceType(rs.getString("SERVICETYPE"));
					repair.setMechanicName(rs.getString("ENAME"));
					repair.setStartTime(rs.getTime("STARTTIME"));
					repair.setEndTime(rs.getTime("ENDTIME"));
					repair.setRdate((rs.getDate("RDATE")));
					repair.setStatus(rs.getString("STATUS"));
					repair.setCenterId(rs.getInt("CENTERID"));
					repair.setFees(rs.getFloat("FEES"));
					repair.setServiceTypeDetail(rs.getString("TYPE"));
					
					repairs.put(repair.getInvoiceNumber(), repair);
					}
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				db.closeConnection();
				

			}
			
			return repairs;
		}

		public void getServiceDetail(Repair service) {
			String sql="";
			PreparedStatement statement = null;
			ArrayList<BaseService> baseServices=new ArrayList<>();
			if (service.getServiceType().equals("Maintainance")){
				
				sql="select cartype.make,cartype.cartypeid,bs.bid,bs.LABOURCHARGE,bs.HRS,bs.bname " + 
						"from REPAIR rep , basicmaintenancemap bsmap,basicservice bs,car, cartype " + 
						"where rep.rid=? and rep.mid=bsmap.mid and bsmap.bid =bs.bid and rep.carid =car.licenseplate and car.cartypeid=cartype.cartypeid";
				
			}else {
				sql="select cartype.make,cartype.cartypeid, bs.LABOURCHARGE,bs.HRS,bs.bname, " + 
						"bs.bid from repair rep ," + 
						"fault_service fs, " + 
						"basicservice bs,car, cartype " + 
						"where rep.rid=?  and rep.fault=fs.fid and fs.bid =bs.bid and rep.carid =car.licenseplate and car.cartypeid=cartype.cartypeid " ;
			}
			
			DatabaseUtil db = new DatabaseUtil();
			try {
				Connection conn=db.establishConnection();
			
				statement = conn.prepareStatement(sql);
				statement.setInt(1,service.getrId());
				
				ResultSet rs = statement.executeQuery();
				InventoryDao inv=new InventoryDao();
				
				while (rs.next())
					{
					BaseService bs=new BaseService();
					bs.setbId(rs.getInt("BID"));
					bs.setLabourCharge(rs.getInt("LABOURCHARGE"));
					bs.setHour(rs.getFloat("HRS"));
					bs.setName((rs.getString("bname")));
					bs.setInvoice(inv.getInvoiceParts(service.getInvoiceNumber(),bs.getbId()));
					
					baseServices.add(bs);
					}
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				db.closeConnection();
				

			}
			
			service.setBaseServices(baseServices);
			
		}

		public void saveInvoice(int getrId, ArrayList<Invoice> invoice) {

			String qry = "Insert into INVOICE values(?,?,?,?,?,?)";
			PreparedStatement statement = null;
			Connection conn=null;
			DatabaseUtil db = new DatabaseUtil();
			for (int i=0;i<invoice.size();i++) {
				Invoice inv=invoice.get(i);
				try {
					conn=db.establishConnection();

				statement = conn.prepareStatement(qry);
					statement.setString(1, "INVOICE_"+getrId);
					statement.setInt(2,inv.getPartId() );
					statement.setInt(3,inv.isWarranty()?1:0 );
					statement.setFloat(4,inv.getCost() );
					statement.setInt(5,inv.isFirst()?1:0 );
					statement.setInt(6,inv.getBid() );
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

		public HashMap<String, Repair> getCompletedServicesbycenter(int serviceCenter) {
			PreparedStatement statement = null;
			HashMap<String, Repair> repairs=new HashMap<String, Repair>();
			String qry = "SELECT  CUSTOMER.cname ,rep.FEES, rep.RID,rep.CENTERID,rep.INVOICENUMBER,rep.CARID,rep.SERVICETYPE,emp.ENAME,sc.STARTTIME,sc.ENDTIME,rep.STATUS,rep.RDATE ,m.type as type " + 
					"FROM REPAIR rep, " + 
					"EMPLOYEE emp,SCHEDULE sc,maintenanceservice m,CUSTOMER   WHERE CUSTOMER.cid=rep.cid  and rep.status=? and rep.CENTERID = ? and rep.MECHANICID = emp.EID and rep.RID=sc.RID  and rep.mid =m.mid " + 
					"UNION " + 
					"SELECT  CUSTOMER.cname ,rep.FEES, rep.RID,rep.CENTERID,rep.INVOICENUMBER,rep.CARID,rep.SERVICETYPE,emp.ENAME,sc.STARTTIME,sc.ENDTIME,rep.STATUS,rep.RDATE ,f.fname as type " + 
					"FROM REPAIR rep, " + 
					"EMPLOYEE emp,SCHEDULE sc,fault f,CUSTOMER  WHERE CUSTOMER.cid=rep.cid  and rep.status=? and rep.CENTERID = ? and rep.MECHANICID = emp.EID and rep.RID=sc.RID  and rep.fault =f.fid";
				DatabaseUtil db = new DatabaseUtil();
			try {
				Connection conn=db.establishConnection();
			
				statement = conn.prepareStatement(qry);
				statement.setString(1,CONSTANTS.STATUS_COMPLETE);
				statement.setInt(2,serviceCenter);
				statement.setString(3,CONSTANTS.STATUS_COMPLETE);
				statement.setInt(4,serviceCenter);
				
				ResultSet rs = statement.executeQuery();
				
				
				while (rs.next())
					{
					Repair repair=new Repair();
					repair.setInvoiceNumber(rs.getString("INVOICENUMBER"));
					Car car=new Car();
					car.setLicensePlate(rs.getString("CARID"));
					repair.setCar(car);
					repair.setCname(rs.getString("cname"));
					repair.setrId(rs.getInt("RID"));
					repair.setServiceType(rs.getString("SERVICETYPE"));
					repair.setMechanicName(rs.getString("ENAME"));
					repair.setStartTime(rs.getTime("STARTTIME"));
					repair.setEndTime(rs.getTime("ENDTIME"));
					repair.setRdate((rs.getDate("RDATE")));
					repair.setStatus(rs.getString("STATUS"));
					repair.setCenterId(rs.getInt("CENTERID"));
					repair.setFees(rs.getFloat("FEES"));
					repair.setServiceTypeDetail(rs.getString("TYPE"));
					
					repairs.put(repair.getInvoiceNumber(), repair);
					}
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}finally {
				db.closeConnection();
				

			}
			
			return repairs;
		}

		public ArrayList<Repair> getServiceHistorybyCenter(int serviceCenter) {

			PreparedStatement statement = null;
			ArrayList<Repair> repairs=new ArrayList<Repair>();
			String qry = "SELECT CUSTOMER.cname , rep.INVOICENUMBER,rep.CARID,rep.SERVICETYPE,emp.ENAME,sc.STARTTIME,sc.ENDTIME,rep.STATUS,rep.RDATE FROM REPAIR rep,EMPLOYEE emp,SCHEDULE sc,CUSTOMER  WHERE  CUSTOMER.cid=rep.cid and rep.CENTERID = ? and rep.MECHANICID = emp.EID and rep.RID=sc.RID " ;
			DatabaseUtil db = new DatabaseUtil();
			try {
				Connection conn=db.establishConnection();
			
				statement = conn.prepareStatement(qry);
				statement.setInt(1,serviceCenter);
				ResultSet rs = statement.executeQuery();
				
				
				while (rs.next())
					{
					Repair repair=new Repair();
					repair.setInvoiceNumber(rs.getString("INVOICENUMBER"));
					Car car=new Car();
					car.setLicensePlate(rs.getString("CARID"));
					repair.setCar(car);
					repair.setCname(rs.getString("cname"));
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
			//	e.printStackTrace();
			} finally {
				db.closeConnection();
			}
			return repairs;
			
			// TODO Auto-generated method stub
		}

		public ArrayList<CarType> getCarServicebyCenter(int serviceCenter) {
			
			
			
			PreparedStatement statement = null;
			ArrayList<CarType> cart=new ArrayList<CarType>();
			String qry = " select * from cartype ct,maintenanceservice m where ct.cartypeid=m.cartypeid order by ct.cartypeid, m.TYPE" ;
			DatabaseUtil db = new DatabaseUtil();
			try {
				Connection conn=db.establishConnection();
			
				statement = conn.prepareStatement(qry);
				
				ResultSet rs = statement.executeQuery();
				
				
				while (rs.next())
					{
					CarType car=new CarType();
					car.setMake(rs.getString("make"));
					car.setModel(rs.getString("model"));
					car.setType(rs.getString("type"));
					car.setMiles(rs.getInt("miles"));
					car.setBaseService(getBaseServices(rs.getInt("mid")));
					cart.add(car);
					}
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				
			} finally {
				db.closeConnection();
			}
			return cart;
			
		}
		



}

	

	
	
	

