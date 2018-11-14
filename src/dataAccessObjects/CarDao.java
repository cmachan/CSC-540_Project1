package dataAccessObjects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

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
				car.setLicensePlate(rs.getString("LICENSEPLATE"));
				car.setMake(rs.getString("MAKE"));
				car.setModel(rs.getString("MODEL"));
				car.setMakeYear(rs.getInt("YEAR"));
				car.setDateOfService(rs.getDate("DATEOFSERVICE"));
				car.setDateOfPurchase(rs.getDate("DATEOFPURCHASE"));
				car.setLastServiceType(rs.getString("LASTSERVICETYPE"));
				car.setLastMileage(rs.getInt("LASTMILEAGE"));
				car.setcId(id);
				
				carsowned.add(car);
		
				}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return carsowned;
	}

	public void registerCar(Car car, Customer customer) {
		
		PreparedStatement statement = null;
		Connection conn=null;
		String qry = "INSERT INTO CAR VALUES(?,?,?,?,?,?,?)";
		
		DatabaseUtil db = new DatabaseUtil();
		try {
			conn=db.establishConnection();
			CarTypeDao carType=new CarTypeDao();
			statement = conn.prepareStatement(qry);
			statement.setString(1, car.getLicensePlate());
			int ctid= carType.getCarType(car.getMake(), car.getModel(), car.getMakeYear());
			System.out.println(ctid);
			statement.setInt(2,ctid );
			statement.setInt(3, car.getcId());
			java.sql.Date date=car.getDateOfService()==null? null:new java.sql.Date(car.getDateOfService().getTime());
			statement.setDate(4,  date);
			statement.setString(5, car.getLastServiceType());
			statement.setDate(6, new java.sql.Date(car.getDateOfPurchase().getTime()));
			statement.setInt(7, car.getLastMileage());
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

	public Car getCar(String licensePlate, int consumerId) {
		PreparedStatement statement = null;
		Car  car=null;
		String qry = "SELECT * FROM CAR c"
				+ "  WHERE c.LICENSEPLATE = ? and c.CID=? " ;
		DatabaseUtil db = new DatabaseUtil();
		try {
			Connection conn=db.establishConnection();
		
			statement = conn.prepareStatement(qry);
			statement.setString(1, licensePlate);
			statement.setInt(2, consumerId);
			ResultSet rs = statement.executeQuery();
			
			
			if (rs.next())
				{
				car=new Car();
				car.setLicensePlate(rs.getString("LICENSEPLATE"));
				car.setDateOfService(rs.getDate("DATEOFSERVICE"));
				car.setDateOfPurchase(rs.getDate("DATEOFPURCHASE"));
				car.setLastServiceType(rs.getString("LASTSERVICETYPE"));
				car.setLastMileage(rs.getInt("LASTMILEAGE"));
				car.setcId(consumerId);
				
		
				}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return car;
	}

	public void updateCar(Car car, String serviceType, Date date) {
				PreparedStatement statement = null;
				Connection conn=null;
				String qry = "UPDATE CAR set LASTMILEAGE = ?, DATEOFSERVICE=?, LASTSERVICETYPE=?  WHERE LICENSEPLATE = ?";
				DatabaseUtil db = new DatabaseUtil();
				try {
					conn=db.establishConnection();
					
					statement = conn.prepareStatement(qry);
					statement.setInt(1, car.getNewMileage());
					statement.setDate(2, new java.sql.Date( date.getTime() ));
					statement.setString(3,serviceType);
					statement.setString(4,car.getLicensePlate());
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
