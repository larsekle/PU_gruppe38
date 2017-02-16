import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

public class JDBC {
	Connection conn = null;
	
	public void connect(){
		try {
			conn = DriverManager.getConnection("jdbc://mysql.stud.ntnu.no/larsekle_tdt4140database?user=larsekle_tdt4140&password=PUgruppe38");
		} catch (SQLException ex){
			System.out.println("SQLException: "+ex.getMessage());
		}		
	}
	
	
	Statement stmt = null; 
	ResultSet rs = null;
	
	public void sporring(){
		try {
			stmt = conn.createStatement();
			
			String query = "SELECT * FROM BRUKERE";
			if (stmt.execute(query)){
				rs = stmt.getResultSet();
			}
			
			while (rs.next()){
				String kolonne1 = rs.getString(1);
				String kolonne2 = rs.getString(2);
				
				System.out.println(kolonne1 + " - " + kolonne2);
			}
		} catch (SQLException ex){
			System.out.println("SQLException: " + ex.getMessage());
		}
	}
	
	public void insett(){
		try{
			
			String query = "INSERT INTO Failures (FailID, StudentID, DateTime, Exercise) VALUES (1, 1, '16.02.2017, '3';";
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
			
		} catch (SQLException ex){
			System.out.println("SQLException: " + ex.getMessage());
		}
	}
	public static void main(String[] args) {
		JDBC jdbc = new JDBC();
		jdbc.connect();
		jdbc.insett();
	}
}
