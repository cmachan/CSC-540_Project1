package controllers;

import java.util.Scanner;

import views.ManagerView;

public class ManagerController {
	Scanner console = new Scanner(System.in);
	ManagerView view;
	
	public ManagerController(ManagerView view) {
		this.view = view;
	}
	
	public ManagerView getView() {
		return view;
	}
	
	public void setView(ManagerView view) {
		this.view = view;
	}
}
