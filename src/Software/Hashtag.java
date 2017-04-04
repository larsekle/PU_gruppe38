package Software;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import GUI.Main;

public class Hashtag {
	
	private final int LIMIT_INCREMENT = 3;
	private JDBC database; 
	private boolean talkedToStudent = false; 
	private boolean test = false;  
	
	// TAGS must always contain less than 30 elements, or else SelfhelpGUI will not be able to show all hashtags.
	public static final ArrayList<String> TAGS = new ArrayList<String>(Arrays.asList("OOT", "interface", "inheritance", "pattern", "class", "vararg", "lambda", "functional interface", "type", "encapsulation", "valid state", "abstract class", "super class", "delegation", "observable", "anonymous class", "collection", "iteration", "text handling", "value types", "scanner", "arrayList", "compare", "IO", "casting")); 
	
	public Hashtag (){
		this.database = new JDBC(LIMIT_INCREMENT);
		database.connect(); 
	}  
	 
	public Hashtag (boolean test){
		this.database = new JDBC(LIMIT_INCREMENT);
		database.connect(); 
		this.test = test; 
	}
	
	public boolean talkToStudent(String tag) {
		if (!talkedToStudent){
			if (!test){
				ExecutorService service = Executors.newSingleThreadExecutor(); 
				service.execute(new Main());
				service.shutdown();
				try {
					service.awaitTermination(100000, TimeUnit.DAYS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} 
			}
			talkedToStudent = true; 
			return true; 
		} return false; 
	}
	
	public boolean sendToDB(int assignment, int exercise, String tag, String FE){
		if (TAGS.indexOf(tag) < 0 && !test){
			throw new IllegalArgumentException(tag + ":  Tag does not exist, contact supervisor"); 
		}
		database.insertFailure(assignment, exercise, tag, FE);
		if (database.limitReached(tag, assignment, exercise)){
			talkToStudent(tag);
		}
		
		return true; 
	}
	
	public JDBC getDatabase(){
		return database; 
	}
}
