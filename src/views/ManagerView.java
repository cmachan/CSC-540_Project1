package views;

import java.util.Scanner;

import controllers.ManagerController;

public class ManagerView {
	private static Scanner console = new Scanner(System.in);
	private ManagerController controller;
	
	public ManagerController getController() {
		return controller;
	}
	public void setController(ManagerController controller) {
		this.controller = controller;
	}
}
