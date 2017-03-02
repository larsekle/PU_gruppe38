package Software;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.text.SimpleDateFormat;

public class JDBC {
	private Connection conn = null;
	
	// Connects Eclipse user to SQL database
	public void connect(){
		try {
			conn = DriverManager.getConnection("jdbc:mysql://mysql.stud.ntnu.no/larsekle_tdt4140database?user=larsekle_tdt4140&password=PUgruppe38");
		} catch (Exception ex){
			System.out.println("SQLException: "+ex.getMessage());
		}		
	}
	
	private Statement stmt = null; 
	private ResultSet rs = null;
	private ResultSet countRs = null;
	
	// Creates SQL-statement and sends to database. Database returns subset of database based on input variables.
	public void view(){
		try  {
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
			
		}  catch (SQLException ex){
			System.out.println("SQLException: " + ex.getMessage());
		}
	}
	
	// Creates SQL-statement and sends to database. Database returns subset of database based on input variables.
	public void view(int Ass, int Ex, int tag){
		try {
			
			stmt = conn.createStatement();
			
			String count = "SELECT COUNT(*) FROM Failures WHERE (StudentID="+getStudentID()+" AND Assignment="+Ass+" AND Exercise="+Ex+" AND Tag="+tag+")";

			if(stmt.execute(count)){
				countRs = stmt.getResultSet();
			}
			
			while (countRs.next()){
				System.out.println("Count: "+countRs.getString(1));
			}
			

		} catch (SQLException ex){
			System.out.println("SQLException: " + ex.getMessage());
		}
	}
	
	public void insert(int Assignment, int Exercise, int Tag, int Codeline, char FE){
		try{
			
			
			// Sets date based on system clock and formats as SQL DateTime
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentTime = sdf.format(dt);
			
			// Creates query and sends it til the database
			String query = String.format("INSERT INTO Failures (StudentID, DateTime, Assignment, Exercise, Tag, Codeline, FE) VALUES (%s, '%s', %s, %s, %s, %s, '%s');", getStudentID(), currentTime, Assignment, Exercise, Tag, Codeline, FE);
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
			
		} catch (SQLException ex){
			System.out.println("SQLException: " + ex.getMessage());
		}
	}
	
	// Gets the users StudentID based om eclipse project filepath
	public int getStudentID(){
		java.net.URL location = Account.class.getProtectionDomain().getCodeSource().getLocation();
        int count = 0;
        int index = 0;
        while (count<5){
	        	if (location.getFile().charAt(index) == '/'){
	        		count++;
	        	}
	        	index++;
        }
        return filepathToID(location.getFile().substring(0,index));
	}
	
	// Converts filepath to StudentID from database
	private int filepathToID(String filepath){
		try {
			stmt = conn.createStatement();
			
			
			// Checks how many times the student is listed in table (0 or 1)
			String query = "SELECT COUNT(*) FROM StudentID WHERE Filepath = '"+filepath+"';";
			if (stmt.execute(query)){
				rs = stmt.getResultSet();
			}
			int count = 0;
			while (rs.next()){
				count = Integer.parseInt(rs.getString(1));
			}
			
			
			// If new student, assign studentID in table StudentID
			if (count == 0){
				query = String.format("INSERT INTO StudentID (Filepath) VALUES ('%s');", filepath);
				stmt = conn.createStatement();
				stmt.executeUpdate(query);
			}
			
			// Get the studentID 
			query = "SELECT StudentID FROM StudentID WHERE Filepath = '"+filepath+"';";
			if (stmt.execute(query)){
				rs = stmt.getResultSet();
			}
			while (rs.next()){
				return rs.getInt(1);
			}
			
		} catch (SQLException ex){
			System.out.println("SQLException: " + ex.getMessage());
		}
		return 0; 
	}
	
	
	public static void main(String[] args) {
		JDBC jdbc = new JDBC();
		jdbc.connect();
		System.out.println(jdbc.getStudentID());
		
		
		//Hashtag ht = new Hashtag(jdbc);
		//ht.checkLimitDB();
	}
	
	
}
