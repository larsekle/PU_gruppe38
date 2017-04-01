package Software;

import java.util.ArrayList;
import java.util.Arrays;

public class Hashtag {
	
	private int codeline = 12; 
	private char FE = 'F';
	private final int LIMIT_INCREMENT = 3;
	private JDBC database;
	private int assignment; 
	private int exercise; 
	private String tag; 
	private boolean talkedToStudent = false; 
	
	// TAGS must always contain less than 30 elements, or else SelfhelpGUI will not be able to show all hashtags.
	public static final ArrayList<String> TAGS = new ArrayList<String>(Arrays.asList("OOT", "interface", "inheritance", "pattern", "class", "vararg", "lambda", "functional interface", "type", "encapsulation", "valid state", "abstract class", "super class", "delegation", "observable", "anonymous class", "collection", "iteration", "text handling", "value types", "scanner", "arrayList", "compare", "IO", "casting")); 
	
	public Hashtag (){
		this.database = new JDBC(LIMIT_INCREMENT);
	}
	
	public void talkToStudent(String tag){
		System.out.println("LIMIT is reached for "+ tag + "! Talking to student.");
		if (!talkedToStudent){
			GUI.Main.main((String[]) null);
			talkedToStudent = true; 
		}
		
	}
	
	public void sendToDB(String tag, int assignment, int exercise, String FE){
		database.connect();
		this.assignment = assignment; 
		this.exercise = exercise; 
		if (TAGS.indexOf(tag) < 0){
			throw new IllegalArgumentException(tag + ":  Tag does not exist, contact supervisor"); 
		}
		database.insertFailure(assignment, exercise, tag, codeline, FE);
		if (database.limitReached(tag, assignment, exercise)){
			talkToStudent(tag);
		}
		
		this.tag = tag;
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
	
	public String getTag(){
		return tag;
	}
	
	public int getCodeline(){
		return codeline;
	}
	
	public char getFE(){
		return FE;
	}

}
