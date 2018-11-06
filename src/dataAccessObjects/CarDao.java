package dataAccessObjects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import databaseUtilities.DatabaseUtil;
import models.Car;
import models.Customer;

public class CarDao {

	public ArrayList<Car> getCarsOwnedByCustomer(int id) {
		PreparedStatement statement = null;
		Car  car=null;
		ArrayList<Car> carsowned=new ArrayList<Car>();
		String qry = "SELECT c.LICENSEPLATE,ct.MAKE,ct.MODEL,ct.YEAR, c.DATEOFSERVICE,c.LASTSERVICETYPE,c.DATEOFPURCHASE,c.LASTMILEAGE FROM CAR c,CARTYPE ct  WHERE c.CID = ? AND c.CARTYPEID=ct.CARTYPEID " ;
		DatabaseUtil db = new DatabaseUtil();
		try {
			Connection conn=db.establishConnection();
		
			statement = conn.prepareStatement(qry);
			statement.setInt(1, id);
			ResultSet rs = statement.executeQuery();
			
			
			while (rs.next())
				{
				car=new Car();
				car.setLicensePlate(rs.getInt("LICENSEPLATE"));
				car.setMake(rs.getString("MAKE"));
				car.setModel(rs.getString("MODEL"));
				car.setMakeYear(rs.getInt("YEAR"));
				car.setDateOfService(rs.getString("DATEOFSERVICE"));
				car.setDateOfPurchase(rs.getString("DATEOFPURCHASE"));
				car.setLastServiceType(rs.getString("LASTSERVICETYPE"));
				car.setLastMileage(rs.getInt("LASTMILEAGE"));
				
				carsowned.add(car);
		
				}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return carsowned;
	}

	
}
