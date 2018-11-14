package views;

import java.util.Scanner;

public class ReceptionistView {
	Scanner sc = null;
	int employeeId;
	
	
	
	public ReceptionistView(int employeeId) {
		this.employeeId = employeeId;
		sc = new Scanner(System.in);
	}

	public void mainMenu() {
		int choice;
		System.out.println("1. Profile");
		System.out.println("2. View Customer Profile");
		System.out.println("3. Add New Employees");
		System.out.println("4. Payroll");
		System.out.println("5. Inventory");
		System.out.println("6. Orders");
		System.out.println("7. Notifications");
		System.out.println("8. New Car Model");
		System.out.println("9. Car Service Details");
		System.out.println("10. Service History");
		System.out.println("11. Invoices");
		System.out.println("12. Logout");
		System.out.println("Enter choice (1-12): ");
		
		choice = sc.nextInt();	
		
		switch (choice) {
	        case 1:  EmployeeView empView = new EmployeeView(employeeId);
	        		 empView.profile();
	                 break;
	        case 2:  
	                 break;
	        case 3:  
	                 break;
	        case 4:  
	                 break;
	        case 5:  
	                 break;
	        case 6:  
	                 break;
	        case 7:  
	                 break;
	        case 8:  
	                 break;
	        case 9:  
	                 break;
	        case 10: 
	                 break;
	        case 11: 
	                 break;
	        case 12: 
	                 break;
	        default: System.out.println("Invalid Entry");
	        		 mainMenu();
	                 break;
		}
		
	}
	
	
	
}
