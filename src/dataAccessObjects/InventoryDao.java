package dataAccessObjects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import databaseUtilities.DatabaseUtil;
import models.Invoice;
import models.Part;
import models.Repair;

public class InventoryDao {
	public ArrayList<Part> validateParts(Repair service) {
		// TODO Auto-generated method stub
		String bid="(";
		for (int i =0;i<service.getBaseServices().size();i++) {
			bid+=service.getBaseServices().get(i).getbId();
			if (i!=(service.getBaseServices().size()-1)) {
				bid+=",";
		
			}
		}
		bid+=")";
		
		PreparedStatement statement = null;
		ArrayList<Part> parts=new ArrayList<Part>();
		String qry = "select bspart.PARTID,inv.MINQTYORDER,dis.did,dis.DELIVERYTIME,inv.QUANTITY,inv.THRESHOLD ,bspart.QUANTITY as reqQuant,(select nvl(SERVICECENTERID,-1) from INVENTORY where (QUANTITY-THRESHOLD)> bspart.QUANTITY and carmake= inv.carmake and partid=inv.PARTID and  ROWNUM <= 1 )  " + 
				"as inventorynext from BASICSERVICE_PART bspart,INVENTORY inv,INVENTORY_PARTS inpart, "
				+" distributor_parts dis where bspart.PARTID=inv.PARTID and inpart.PARTID=inv.PARTID and inpart.PARTID=dis.PARTID  and inv.carmake =? and bspart.cartypeid=? "
				+" and inv.SERVICECENTERID=? and  (inv.QUANTITY-inv.THRESHOLD)<bspart.QUANTITY and   bspart.BID in "+bid;
		
		DatabaseUtil db = new DatabaseUtil();
		try {
			Connection conn=db.establishConnection();
		
			statement = conn.prepareStatement(qry);
			
			statement.setString(1,service.getCar().getMake());
			statement.setInt(2,service.getCar().getCarTypeID());
			statement.setInt(3,service.getCenterId());
			ResultSet rs = statement.executeQuery();
			
			
			while (rs.next())
				{
				Part ps=new Part();
				ps.setPartID(rs.getInt("PARTID"));
				ps.setQuantity(Math.max(rs.getInt("reqQuant"),rs.getInt("MINQTYORDER")));
				ps.setDistributorId(rs.getString("did"));
				ps.setDeliveryTime(rs.getInt("DELIVERYTIME"));
				ps.setAltcenter(rs.getInt("inventorynext"));
				ps.setServiceCenterId(service.getCenterId());
				parts.add(ps);
				}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			db.closeConnection();
			
		}
		return parts;
	}

	public void updateInventory(int centerId, int mid, int carType) {
		// TODO Auto-generated method stub
		String qry="update  INVENTORY inv set inv.QUANTITY=inv.QUANTITY-(SELECT NVL( ( select (sum(bspart.QUANTITY) ) from BASICSERVICE_PART bspart " + 
				"where bspart.PARTID=inv.PARTID and  inv.SERVICECENTERID=? and bspart.CARTYPEID=? and bspart.BID in (select BID from BASICMAINTENANCEMAP where MID =?) group by  bspart.PARTID) ,0) from dual) ";
	
		PreparedStatement statement = null;
		Connection conn=null;
		DatabaseUtil db = new DatabaseUtil();
		try {
			conn=db.establishConnection();
			
			statement = conn.prepareStatement(qry);
			statement.setInt(1, centerId);
			statement.setInt(2, carType);
			statement.setInt(3, mid);
			
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

	public ArrayList<Part>  getParts(int getbId, int carType, String make) {
		
		// TODO Auto-generated method stub
		
		
				PreparedStatement statement = null;
				ArrayList<Part> parts=new ArrayList<Part>();
				String qry = "select bspart.PARTID, bspart.QUANTITY,inv.NAME,psw.PRICE,psw.WARRANTY from BASICSERVICE_PART bspart,INVENTORY_PARTS inv,PARTS_WARRANTY psw"
						+ " where bspart.BID=? and bspart.CARTYPEID=? and inv.PARTID=bspart.PARTID and psw.PARTID=inv.PARTID and psw.CARMAKE=?" ;
				
				DatabaseUtil db = new DatabaseUtil();
				try {
					Connection conn=db.establishConnection();
				
					statement = conn.prepareStatement(qry);
					statement.setInt(1,getbId);
					statement.setInt(2,carType);
					statement.setString(3,make);
					ResultSet rs = statement.executeQuery();
					
					
					while (rs.next())
						{
						Part ps=new Part();
						ps.setPartID(rs.getInt("PARTID"));
						ps.setQuantity(rs.getInt("QUANTITY"));
						ps.setpName(rs.getString("NAME"));
						ps.setUnitPrice(rs.getFloat("PRICE"));
						ps.setWarranty(rs.getInt("WARRANTY"));
						parts.add(ps);
						}
					
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					db.closeConnection();
					

				}
				
				return parts;
		
		// TODO Auto-generated method stub
		
	}

	public ArrayList<Invoice> getInvoiceParts(String invoiceNumber, int getbId) {

		PreparedStatement statement = null;
		ArrayList<Invoice> invoices=new ArrayList<Invoice>();
		String qry = "select inps.name,ini.PARTID,ini.COST,ini.WARRANTY,ini.ISFIRST from invoice ini,inventory_parts inps where ini.invoicenumber=? and ini.bid=? and inps.PARTID=ini.PARTID ";
		DatabaseUtil db = new DatabaseUtil();
		try {
			Connection conn=db.establishConnection();
		
			statement = conn.prepareStatement(qry);
			statement.setString(1,invoiceNumber);
			statement.setInt(2,getbId);
			ResultSet rs = statement.executeQuery();
			
			
			while (rs.next())
				{
				Invoice ps=new Invoice();
				ps.setPartId(rs.getInt("PARTID"));
				ps.setPartName((rs.getString("name")));
				ps.setCost(rs.getInt("COST"));
				ps.setWarranty(rs.getInt("WARRANTY")==0?false:true);
				ps.setFirst(rs.getInt("ISFIRST")==0?false:true);
				
				invoices.add(ps);
				}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			db.closeConnection();
			

		}
		
		return invoices;

	}
}
