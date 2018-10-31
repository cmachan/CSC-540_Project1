package main;

import databaseUtilities.*;

public class Run {
	public static void main(String args[]) {
		DatabaseUtil db = new DatabaseUtil();
		db.getConnection();
		System.out.println("Created");
		db.closeConnection();
	}
}
