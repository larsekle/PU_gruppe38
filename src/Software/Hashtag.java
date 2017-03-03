package Software;

import java.util.ArrayList;
import java.util.Arrays;

import GUI.BuddyController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Hashtag {
	
	private int Codeline = 12; 
	private char FE = 'F';
	private final int LIMIT_INCREMENT = 5;
	private final ArrayList<String> TAGS = new ArrayList<String>(Arrays.asList("#constructor", "#validation", "#encapsulation", "#objectstructures", "#interfaces", "#inheritage")); 
	private JDBC database;
	
	public Hashtag (){
		this.database = new JDBC(LIMIT_INCREMENT);
	}
	
	public void talkToStudent(String tag){
		System.out.println("LIMIT er nådd for "+ tag + "! Snakker til student.");
		
		return;
	}
	
	public void sendToDB (String tag, int assignment, int exercise){
		database.connect();
		
		database.insertFailure(assignment, exercise, TAGS.indexOf(tag), Codeline, FE);
		if (database.limitReached(TAGS.indexOf(tag), assignment, exercise)){
			talkToStudent(tag);
		}
	}
	

}
