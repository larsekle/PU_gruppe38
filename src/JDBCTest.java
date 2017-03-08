import java.sql.Connection;

public class JDBCTest extends junit.framework.TestCase{
	
	private JDBC jdbc = null;
	private Connection conn = null;
	private Hashtag ht = null;
	
	protected void setUp(){
		jdbc = new JDBC();
		ht = new Hashtag(jdbc);
	}
	
	public void testConnectException(){ //Trenger ikke denne, det blir for vanskelig sier han
		try {
		      jdbc.connect();
		   } catch (Exception e){
			 assertEquals(jdbc.getConnection(), null);
		   }
		}
	
	public void testViewOneException(){
		try {
		      jdbc.view();
		   } catch (Exception e){
		      fail(); //litt usikker på hvordan jeg skal implementere dette
		   }
		catch (Exception e){
		      fail(); 
		   }
		}
	
	public void testViewTwoException(){
		try {
		      jdbc.view(ht.getAss(),ht.getEx(),ht.getTag());
		   } catch (Exception e){
		      fail(); //litt usikker på hvordan jeg skal implementere dette
		   }
		catch (SQLException e){
		      fail(); 
		   }
		}
	
	public void testInsertException(){
		try {
		      jdbc.insert(ht.getAss(),ht.getEx(),ht.getTag(),ht.getCodeline(),ht.getFE());
		   } catch (Exception e){
		      fail(); //litt usikker på hvordan jeg skal implementere dette
		   }
		catch (SQLException e){
		      fail(); 
		   }
		}
	
	
	//må også teste getstudentID. da må jeg ha mer info fra de andre om hvordan den funker
}
