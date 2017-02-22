import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

public class JDBC {
	Connection conn = null;
	
	public void connect(){
		try {
			conn = DriverManager.getConnection("jdbc:mysql://mysql.stud.ntnu.no/larsekle_tdt4140database?user=larsekle_tdt4140&password=PUgruppe38");
		} catch (Exception ex){
			System.out.println("SQLException: "+ex.getMessage());
		}		
	}
	
	Statement stmt = null; 
	ResultSet rs = null;
	ResultSet countRs = null;
	
	public void view(int studID, int Ass, int Ex, int tag){
		try {
			stmt = conn.createStatement();  //her kommer nullpointerException, og koden stopper.
			String query = "SELECT * FROM Failures";
			String count = "SELECT COUNT(*) FROM Failures WHERE (StudentID="+studID+" AND Assignment="+Ass+" AND Exercise="+Ex+" AND Tag="+tag+")";
			

			if (stmt.execute(query)){
				rs = stmt.getResultSet();
			}
			
			if(stmt.execute(count)){
				countRs = stmt.getResultSet();
			}
			
			while (countRs.next()){
				System.out.println("Count: "+countRs.getString(1));
			}
			
			while (rs.next()){
				String StudentID = rs.getString(2); //hopper ut her.
				String Assignment = rs.getString(4);
				String Exercise = rs.getString(5);
				String Tag = rs.getString(6);
				
				System.out.println(String.format("StudentID : %s, Assignment : %s, Exercise : %s, Tag : %s", StudentID, Assignment, Exercise, Tag));
			}
		} catch (SQLException ex){
			System.out.println("SQLException: " + ex.getMessage());
		}
	}
	
	public void insert(int StudentID, int DateTime, int Assignment, int Exercise, int Tag, int Codeline, char FE){
		try{
			String query = String.format("INSERT INTO Failures (StudentID, DateTime, Assignment, Exercise, Tag, Codeline, FE) VALUES (%s, %s, %s, %s, %s, %s, '%s');", StudentID, DateTime, Assignment, Exercise, Tag, Codeline, FE);
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
			
		} catch (SQLException ex){
			System.out.println("SQLException: " + ex.getMessage());
		}
	}
	public static void main(String[] args) {
		JDBC jdbc = new JDBC();
		jdbc.connect();
		jdbc.view(1, 0, 3, 0);
		
		//Hashtag ht = new Hashtag(jdbc);
		//ht.checkLimitDB();
	}
	
	
}
