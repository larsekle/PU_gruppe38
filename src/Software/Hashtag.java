package Software;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Hashtag {
	
	private int StudentID = 11;
	private int DateTime = 1;
	private int Assignment = 3;
	private int Exercise = 5;
	private int Tag = 9; 
	private int Codeline = 12; 
	private char FE = '0';
	private Map<String, Integer> hashtags = new HashMap<String, Integer>();
	private final static int LIMIT = 2;
	private JDBC database;
	
	public Hashtag (JDBC database){
		this.database = database;
	}
	
	public void addHash(String ht){
		if(!hashtags.containsKey(ht)){
			hashtags.put(ht, 1);
			checkLimit(ht);
		}else{
			hashtags.put(ht, hashtags.get(ht)+1);
			checkLimit(ht);
			}
		sendToDB(); 
	}
	
	public void checkLimit(String s){
		if (hashtags.get(s) == LIMIT){
			talkToStudent(s);
			hashtags.remove(s);
		}
	}
	
	public void checkLimitDB(){
		database.view(Assignment, Exercise, Tag); 
	}
	
	public Map<String, Integer> getHashtags(){
		return hashtags;
	}
	
	public void talkToStudent(String tag){
		System.out.println("LIMIT er nådd for "+tag+"! Snakker til student.");
		return;
	}
	
	public void sendToDB (){
		database.connect();
		database.insertFailure(Assignment, Exercise, Tag, Codeline, FE);

	}
	
	
	public static void main(String[] args) {
	
		
	}

}
