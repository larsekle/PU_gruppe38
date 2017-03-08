package Software;

import java.util.ArrayList;
import java.util.Arrays;

import sun.applet.Main;

public class Hashtag {
	
	private int Codeline = 12; 
	private char FE = 'F';
	private final int LIMIT_INCREMENT = 3;
	private final ArrayList<String> TAGS = new ArrayList<String>(Arrays.asList("#constructor", "#validation", "#encapsulation", "#objectstructures", "#interfaces", "#inheritage")); 
	private JDBC database;
	private int assignment; 
	private int exercise; 
	private int tag; 
	
	
	public Hashtag (){
		this.database = new JDBC(LIMIT_INCREMENT);
	}
	
	public void talkToStudent(String tag){
		System.out.println("LIMIT er nådd for "+ tag + "! Snakker til student.");
		GUI.Main.main((String[]) null);
	}
	
	public void sendToDB(String tag, int assignment, int exercise){
		database.connect();
		this.assignment = assignment; 
		this.exercise = exercise; 
		this.tag = TAGS.indexOf(tag); 
		
		database.insertFailure(assignment, exercise, TAGS.indexOf(tag), Codeline, FE);
		if (database.limitReached(TAGS.indexOf(tag), assignment, exercise)){
			talkToStudent(tag);
		}
	}
	
	public JDBC getDatabase(){
		return database;
	}
	
	public int getAss(){
		return assignment;
	}
	
	public int getEx(){
		return exercise;
	}
	
	public int getTag(){
		return tag;
	}
	
	public int getCodeline(){
		return Codeline;
	}
	
	public char getFE(){
		return FE;
	}

}
