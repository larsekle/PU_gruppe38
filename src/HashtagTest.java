
public class HashtagTest extends junit.framework.TestCase {
	
	private JDBC jdbc = null;
	private Hashtag ht = null;

	protected void setUp(){
		jdbc = new JDBC();
		ht = new Hashtag(jdbc);
	}
	
	 protected void tearDown() {
	      jdbc = null;
	      ht = null;
	   }
	 
	 public void testConstructur(){
		 assertEquals(jdbc, ht.getDatabase());
	 }
		
	public void testAddHashtag(){
		 ht.addHash("BuddyBot");
		 assertEquals("{BuddyBot=1}",ht.getHashtags());
		 ht.addHash("BuddyBot");
		 assertEquals("{BuddyBot=2}",ht.getHashtags());
		 ht.addHash("Database");
		 assertEquals("{BuddyBot=2, Database=1}",ht.getHashtags());
		 ht.addHash("BuddyBot");
		 assertEquals("{Database=1}",ht.getHashtags());
		 
	}
		 
		 
	 }
	


	
