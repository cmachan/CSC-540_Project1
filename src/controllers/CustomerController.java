package controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import constants.CONSTANTS;
import dataAccessObjects.CarDao;
import dataAccessObjects.CustomerDao;
import dataAccessObjects.EmployeeDao;
import dataAccessObjects.InventoryDao;
import dataAccessObjects.RepairDao;
import models.BaseService;
import models.Car;
import models.Customer;
import models.Employee;
import models.Part;
import models.Repair;
import views.CustomerView;

public class CustomerController  {
	private CustomerView view;
	private static Customer customer;
	
	public CustomerController(CustomerView view){
		this.view=view;
	}
	public  void displayMainMenu() {
		controlFlow(CONSTANTS.CUSTOMER_MAIN_MENU);
		
	}
	
	public Customer getCustomerProfile(int id) {
		 CustomerDao cusDao=new CustomerDao();
		 Customer customer=cusDao.getCustomerProfile(id);
		 this.customer=customer;
		 return customer;
		 
	}
	private  void updateCustomerProfile() {
		 CustomerDao cusDao=new CustomerDao();
		 cusDao.updateCustomerProfile(customer);
		 
		 
	}
	public  void registerCar(Car car) {
		 CarDao carDao=new CarDao();
		 carDao.registerCar(car,customer);
		 
		 
	}
	
	
	public   ArrayList<Repair> getServiceHistory() {
		 RepairDao repair=new RepairDao();
		 return repair.getServiceHistory(customer.getcId());
		
	}
	
	
	public void controlFlow(String choice) {
		System.out.println("\n\n");
		switch(choice) {
		case CONSTANTS.CUSTOMER_MAIN_MENU:
			choice=view.displayMainMenu();
			break;
		case CONSTANTS.CUSTOMER_PROFILE:
			choice=view.displayProfileMenu();
			break;
		case CONSTANTS.CUSTOMER_VIEW_PROFILE:
			getCustomerProfile(customer.getcId());
			choice=view.viewProfile(customer);
			break;
		case CONSTANTS.CUSTOMER_UPDATE_PROFILE:
				
			choice=view.updateProfile(customer);
			updateCustomerProfile();
			break;
		
		case CONSTANTS.CUSTOMER_REGISTER_CAR:
			choice=view.registerCar(customer);
			break;
		case CONSTANTS.CUSTOMER_SERVICE:
			
			choice=view.viewServiceMenu();
			break;	
		case CONSTANTS.CUSTOMER_SERVICE_HISTORY:
			ArrayList<Repair> repairs=getServiceHistory();
			choice=view.viewServiceHistory(repairs);
			break;	
			
		case CONSTANTS.CUSTOMER_SERVICE_SCHEDULE:
			
			choice=view.viewServiceSchedule(customer);
			break;	
				
		default:
			return;
		}
		
		controlFlow(choice);
		
	}
	public boolean validateCar(Repair service) {
		CarDao carDao=new CarDao();
		Car carTemp=carDao.getCar(service.getCar().getLicensePlate(),service.getCar().getcId());
		if (carTemp==null)
			return false;
		
		carTemp.setNewMileage(service.getCar().getNewMileage());
		service.setCar(carTemp);
		
		
		return true;
	}
	
	
	
	
	public ArrayList<Employee> findDates(Repair service) {
		
		RepairDao rDao=new RepairDao();
		InventoryDao in=new InventoryDao();
		ArrayList<BaseService>baseServices= rDao.getBaseServices(service);
		ArrayList<Part> parts= in.validateParts(service);
		ArrayList<Employee> emps=null;
		if (parts.size()!=0) {
			int max=0;
			for (int i =0;i<parts.size();i++) {
				max=Math.max(max, parts.get(i).getDeliveryTime());
				
			}
			rDao.placeOrder(parts);
			
		}
		else {
			int hours=0;
			for (int i =0;i<baseServices.size();i++) {
				hours+=baseServices.get(i).getHour();	
			}
			emps=new ArrayList<>();
			EmployeeDao empDao=new EmployeeDao();
			if (service.getMechanicId()==0) {
				
				emps=empDao.getFreeMechanic(service.getCenterId(),hours);
				
				
			}else {
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				try {	
				Employee emp1= new Employee();
				emp1.seteId(service.getMechanicId());
				Employee emp2= new Employee();
				emp2.seteId(service.getMechanicId());
				ArrayList< Date> date=(empDao.getStartDate(service.getMechanicId(),hours));
				emp1.setStartTime(date.get(0));
				
					emp1.setsDate(dateFormat.parse(dateFormat.format(date.get(0))));
				
				emp2.setStartTime(date.get(1));
				emp2.setsDate(dateFormat.parse(dateFormat.format(date.get(1))));
				emps.add(emp1);
				emps.add(emp2);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			emps.get(0).setEndTime(EmployeeDao.addHours(emps.get(0).getStartTime(),hours));
			emps.get(1).setEndTime(EmployeeDao.addHours(emps.get(1).getStartTime(),hours));
			//rDao.checkUpdateDates(hours,service.getCenterId());
			
			
			
		}
		
		
		// TODO Auto-generated method stub
		return emps;
	}
	
	public void saveMaintenance(Repair service, Employee employee) {
		// TODO Auto-generated method stub
		//REPAIR
		//SCHEDULE
		//INVENTORY
		//CAR
		//MECH_HOURS
		int hour=(int)(employee.getEndTime().getTime()-employee.getStartTime().getTime())/3600000;
		service.setStartTime(employee.getStartTime());
		service.setEndTime(employee.getEndTime());
		service.setMechanicId(employee.geteId());
		
		EmployeeDao empDao=new EmployeeDao();
		empDao.updateMechHours(employee.geteId(),hour);
		CarDao carDao=new CarDao();
		carDao.updateCar(service.getCar(),service.getServiceType(),service.getStartTime());
		InventoryDao inDao=new InventoryDao();
		inDao.updateInventory(service.getCenterId(),service.getMid());
		RepairDao rDao=new RepairDao();
		int rId=rDao.insertRepair(service);
		service.setrId(rId);
		rDao.insertSchedule(service);
		
		
		
	}

}
