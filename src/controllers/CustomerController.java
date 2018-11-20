package controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

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
import models.Fault;
import models.Invoice;
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
	public   ArrayList<Repair> getUpcomingServices() {
		 RepairDao repair=new RepairDao();
		 return repair.getUpcomingServices(customer.getcId());
		
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
			
		case CONSTANTS.CUSTOMER_SERVICE_RESCHEDULE:
					ArrayList<Repair> upcomingServices=getUpcomingServices();
				
					choice=view.viewServiceReSchedule(upcomingServices,customer);
					break;
		case CONSTANTS.CUSTOMER_SERVICE_INVOICE:
			 	HashMap<String, Repair> completedservices=getCompletedServices();
			
				choice=view.viewServiceInvoice(completedservices,customer);
				break;
			
					
		default:
			return;
		}
		
		controlFlow(choice);
		
	}
	private  HashMap<String, Repair> getCompletedServices() {
		 RepairDao repair=new RepairDao();
		 return repair.getCompletedServices(customer.getcId());
		
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
	
	
	
	
	public ArrayList<Employee> findDates(Repair service,Date startdate) {
		
		RepairDao rDao=new RepairDao();
		InventoryDao in=new InventoryDao();
		ArrayList<BaseService>baseServices;
		if (service.getFault()!=null) {
			baseServices= service.getFault().getBs();
		}else {
			baseServices= rDao.getBaseServices(service);
		}
		
		service.setBaseServices(baseServices);
		
		
		ArrayList<Part> parts= in.validateParts(service);
		ArrayList<Employee> emps=null;
		if (parts.size()!=0) {
			int max=0;
			for (int i =0;i<parts.size();i++) {
				max=Math.max(max, parts.get(i).getDeliveryTime());
				
			}
			
			rDao.placeOrder(parts);
			System.out.println(" Parts not available for the service, Please try again after "+max+" days.");
			
		}
		else {
			float hours=0;
			for (int i =0;i<baseServices.size();i++) {
				hours+=baseServices.get(i).getHour();	
			}
			emps=new ArrayList<>();
			EmployeeDao empDao=new EmployeeDao();
			if (service.getMechanicId()==0) {
				
				emps=empDao.getFreeMechanic(service.getCenterId(),hours,startdate);
				
				
			}else {
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				try {	
				Employee emp1= new Employee();
				emp1.seteId(service.getMechanicId());
				emp1.seteName(service.getMechanicName());
				Employee emp2= new Employee();
				emp2.seteId(service.getMechanicId());
				emp2.seteName(service.getMechanicName());
				ArrayList< Date> date=(empDao.getStartDate(service.getMechanicId(),hours,startdate));
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
		service.setServiceType("Maintainance");
		float hour=(float)((employee.getEndTime().getTime()-employee.getStartTime().getTime())/3600000);
		
		service.setStartTime(employee.getStartTime());
		service.setEndTime(employee.getEndTime());
		service.setMechanicId(employee.geteId());
		ArrayList<Invoice> invoice=new  ArrayList<>();
		calculateFee(service,invoice);
		EmployeeDao empDao=new EmployeeDao();
		empDao.updateMechHours(employee.geteId(),hour);
		
		CarDao carDao=new CarDao();
		
		carDao.updateCar(service.getCar(),service.getServiceType(),service.getStartTime());
		InventoryDao inDao=new InventoryDao();
		inDao.updateInventory(service.getCenterId(),service.getMid(),service.getCar().getCarTypeID());
		RepairDao rDao=new RepairDao();
		int rId=rDao.insertRepair(service);
		service.setrId(rId);
		rDao.insertSchedule(service);
		rDao.saveInvoice(service.getrId(),invoice);
		
		CustomerDao  cusDao=new CustomerDao();
		cusDao.updateCustomerService(service.getcId(),service.getBaseServices(),service.getStartTime(),service.getrId(),false);
		
		
		
	}
	public ArrayList<Fault> getAllFaults(Repair service) {
		// TODO Auto-generated method stub
		RepairDao rDao=new RepairDao();
		return rDao.getAllFaults();
		
	}
	public void getFaultDetails(Fault fault, int carType,int customerId,String make) {
		// TODO Auto-generated method stub
		RepairDao rDao=new RepairDao();
		rDao.getFaultDetails(fault,carType,customerId,make);
	}
	public void saveRepair(Repair service, Employee employee, Fault fault) {
		// TODO Auto-generated method stub
		service.setServiceType("Repair");
		RepairDao rDao=new RepairDao();
		
		float hour=(float)(employee.getEndTime().getTime()-employee.getStartTime().getTime())/3600000;
		service.setStartTime(employee.getStartTime());
		service.setEndTime(employee.getEndTime());
		service.setMechanicId(employee.geteId());
		ArrayList<Invoice> invoice=new  ArrayList<>();
		calculateFee(service,invoice);
		
		EmployeeDao empDao=new EmployeeDao();
		empDao.updateMechHours(employee.geteId(),hour);
		CarDao carDao=new CarDao();
		carDao.updateCar(service.getCar(),service.getServiceType(),service.getStartTime());
		InventoryDao inDao=new InventoryDao();
		inDao.updateInventory(service.getCenterId(),service.getMid(),service.getCar().getCarTypeID());
		
		int rId=rDao.insertRepair(service);
		service.setrId(rId);
		rDao.insertSchedule(service);
		CustomerDao  cusDao=new CustomerDao();
		rDao.saveInvoice(service.getrId(),invoice);
		cusDao.updateCustomerService(service.getcId(),service.getBaseServices(),service.getStartTime(),service.getrId(),false);
		
		
	}
	private void calculateFee(Repair service, ArrayList<Invoice> invoice) {
		float cost=0;
		
		for (int i=0;i<service.getBaseServices().size();i++) {
			boolean warranty=true;
			BaseService bs=service.getBaseServices().get(i);
			if (bs.getLastService()==null) {
				for (int j=0;j<bs.getParts().size();j++) {
					
					cost+=bs.getParts().get(j).getUnitPrice()*bs.getParts().get(j).getQuantity();
					Invoice inv=new Invoice();
					
					inv.setPartId(bs.getParts().get(j).getPartID());
					inv.setCost(bs.getParts().get(j).getUnitPrice()*bs.getParts().get(j).getQuantity());
					inv.setFirst(true);
					inv.setWarranty(false);
					inv.setBid(bs.getbId());
					invoice.add(inv);
					
				}
			}
			else {
				 LocalDate startDate = new java.sql.Date(service.getStartTime().getTime()).toLocalDate();
				 LocalDate endDate = new java.sql.Date(service.getEndTime().getTime()).toLocalDate();

				int diff =(int) ChronoUnit.MONTHS.between(endDate,startDate);
				for (int j=0;j<bs.getParts().size();j++) {
					if (bs.getParts().get(j).getWarranty()==0 || diff>bs.getParts().get(j).getWarranty())
						{ 	
							warranty=false;
							
							cost+=bs.getParts().get(j).getUnitPrice()*bs.getParts().get(j).getQuantity();
							Invoice inv=new Invoice();
							
							inv.setPartId(bs.getParts().get(j).getPartID());
							inv.setCost(bs.getParts().get(j).getUnitPrice()*bs.getParts().get(j).getQuantity());
							inv.setFirst(false);
							inv.setWarranty(false);
							inv.setBid(bs.getbId());
							invoice.add(inv);
						}
					else {
						Invoice inv=new Invoice();
						
						inv.setPartId(bs.getParts().get(j).getPartID());
						inv.setCost(0);
						inv.setFirst(false);
						inv.setWarranty(true);
						inv.setBid(bs.getbId());
						invoice.add(inv);
					}
				if (!warranty) {
					cost+=bs.getLabourCharge()*bs.getHour();
					
				}
					
				
			}
			}
			
		}
		
		service.setFees(cost);
	}
	public void rescheduleService(Repair selectedRepair, Employee employee) {
		// TODO Auto-generated method stub
		RepairDao rDao=new RepairDao();
		
		float hour=(float)(employee.getEndTime().getTime()-employee.getStartTime().getTime())/3600000;
		selectedRepair.setStartTime(employee.getStartTime());
		selectedRepair.setEndTime(employee.getEndTime());
		int prevMechanic=selectedRepair.getMechanicId();
		selectedRepair.setMechanicId(employee.geteId());
		EmployeeDao empDao=new EmployeeDao();
		empDao.deleteMechHours(prevMechanic,hour);
		empDao.updateMechHours(employee.geteId(),hour);
		CarDao carDao=new CarDao();
		carDao.updateCar(selectedRepair.getCar(),selectedRepair.getServiceType(),selectedRepair.getStartTime());
		
		rDao.updateRepair(selectedRepair);
		
		rDao.updateSchedule(selectedRepair);

		CustomerDao  cusDao=new CustomerDao();
		
		cusDao.updateCustomerService(selectedRepair.getcId(),selectedRepair.getBaseServices(),selectedRepair.getStartTime(),selectedRepair.getrId(),true);
		
	}
	
	
	
	public ArrayList<Employee> findDatesReschedule(Repair selectedRepair, Date rdate) {
		float hour=(float)(selectedRepair.getEndTime().getTime()-selectedRepair.getStartTime().getTime())/3600000;
		
	
		ArrayList<Employee> emps=new ArrayList<>();
		EmployeeDao empDao=new EmployeeDao();
		emps=empDao.getFreeMechanic(selectedRepair.getCenterId(),hour,rdate);
			
			
		
		emps.get(0).setEndTime(EmployeeDao.addHours(emps.get(0).getStartTime(),hour));
		emps.get(1).setEndTime(EmployeeDao.addHours(emps.get(1).getStartTime(),hour));
		//rDao.checkUpdateDates(hours,service.getCenterId());
		
	
	// TODO Auto-generated method stub
	return emps;
	}
	public void getServiceDetail(Repair service) {
		// TODO Auto-generated method stub
		RepairDao rDao=new RepairDao();
		rDao.getServiceDetail(service);
		
	}

}
