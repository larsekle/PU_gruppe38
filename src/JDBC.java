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
	
	public void view(){
		try {
			stmt = conn.createStatement();
			
			String query = "SELECT * FROM Failures";
			if (stmt.execute(query)){
				rs = stmt.getResultSet();
			}
			
			while (rs.next()){
				String StudentID = rs.getString(2);
				String Assignment = rs.getString(4);
				String Exercise = rs.getString(5);
				String Tag = rs.getString(6);
				
				System.out.println(String.format("StudentID : %s, Assignment : %s, Exercise : %s, Tag : %s", StudentID, Assignment, Exercise, Tag));
			}
		} catch (SQLException ex){
			System.out.println("SQLException: " + ex.getMessage());
		}
	}
	
	public void insert(String FailID, String StudentID, String DateTime, String Assignment, String Exercise, String Tag, String Codeline, char FE){
		try{
			String query = String.format("INSERT INTO Failures VALUES (%s, %s, %s, %s, %s, %s, %s, '%s');", FailID, StudentID, DateTime, Assignment, Exercise, Tag, Codeline, FE);
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
			
		} catch (SQLException ex){
			System.out.println("SQLException: " + ex.getMessage());
		}
	}
	public static void main(String[] args) {
		JDBC jdbc = new JDBC();
		jdbc.connect();
		jdbc.view();
	}
}
