import java.util.HashMap;
import java.util.Map;

public class Hashtag {
	
	private Map<String, Integer> hashtags = new HashMap<String, Integer>();
	private final static int LIMIT = 2;
	
	public void addHash(String ht){
		if(!hashtags.containsKey(ht)){
			hashtags.put(ht, 1);
			checkLimit(ht);
		}else{
			hashtags.put(ht, hashtags.get(ht)+1);
			checkLimit(ht);
			}
		sendToDB(); //Denne kan flyttes
	}
	
	public void checkLimit(String s){
		if (hashtags.get(s) == LIMIT){
			talkToStudent(s);
			hashtags.remove(s);
		}
	}
	
	public Map<String, Integer> getHashtags(){
		return hashtags;
	}
	
	public void talkToStudent(String tag){
		System.out.println("LIMIT er nådd for "+tag+"! Snakker til student.");
		return;
	}
	
	public void sendToDB (){
		//SEND TIL DATABASE!!!!
	}

}
