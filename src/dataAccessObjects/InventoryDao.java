package dataAccessObjects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import databaseUtilities.DatabaseUtil;
import models.Part;
import models.Repair;

public class InventoryDao {
	public ArrayList<Part> validateParts(Repair service) {
		// TODO Auto-generated method stub
		
		
		PreparedStatement statement = null;
		ArrayList<Part> parts=new ArrayList<Part>();
		String qry = "select bspart.PARTID,inv.MINQTYORDER,dis.DELIVERYTIME from BASICSERVICE_PART bspart,INVENTORY inv,INVENTORY_PARTS inpart,"
				+ " distributor_parts dis where bspart.PARTID=inv.PARTID and inpart.PARTID=inv.PARTID and inpart.PARTID=dis.PARTID and dis.deliverytime=(select min(deliverytime) from distributor_parts where partid=dis.PARTID )    and inv.SERVICECENTERID=?  and  (inv.QUANTITY-inv.THRESHOLD)<bspart.QUANTITY and bspart.BID in (select BID from BASICMAINTENANCEMAP where MID =?)" ;
		
		DatabaseUtil db = new DatabaseUtil();
		try {
			Connection conn=db.establishConnection();
		
			statement = conn.prepareStatement(qry);
			statement.setInt(1,service.getCenterId());
			statement.setInt(2,service.getMid());
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
		return parts;
	}

	public void updateInventory(int centerId, int mid) {
		// TODO Auto-generated method stub
		String qry="update  INVENTORY inv set inv.QUANTITY=inv.QUANTITY-(SELECT NVL( ( select (sum(bspart.QUANTITY) ) from BASICSERVICE_PART bspart " + 
				"where bspart.PARTID=inv.PARTID and  inv.SERVICECENTERID=? and bspart.BID in (select BID from BASICMAINTENANCEMAP where MID =?) group by  bspart.PARTID) ,0) from dual) ";
	
		PreparedStatement statement = null;
		Connection conn=null;
		DatabaseUtil db = new DatabaseUtil();
		try {
			conn=db.establishConnection();
			
			statement = conn.prepareStatement(qry);
			statement.setInt(1, centerId);
			statement.setInt(2, mid);
			
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
