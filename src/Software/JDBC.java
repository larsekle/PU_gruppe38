package Software;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
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
	
	// Creates SQL-statement and sends to database. Database returns subset of Failures table based on input variables.
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
	
	// Creates SQL-statement and sends to database. Database returns subset of Failures table based on input variables.
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
	
	// Inserts new row into Failures table
	public void insertFailure(int Assignment, int Exercise, int Tag, int Codeline, char FE){
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
	
	// Inserts new row into Feedback table
	public void insertFeedback(int linkID, int studID, double rating){
		try{
			
			// Creates query and sends it til the database
			String query = String.format("INSERT INTO Feedback (StudentID, LinkID, Rating) VALUES (%s, '%s', %s, %s, %s, %s, '%s');", studID, linkID, rating);
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
	
	// Get linkID from Resources table based on entire link
	public int getLinkID(String link){
		try  {
			stmt = conn.createStatement();
			String query = "SELECT * FROM Resources WHERE Link = '" + link + "'";
			
			if (stmt.execute(query)){
				rs = stmt.getResultSet();
			}
			
			while (rs.next()){
				return rs.getInt(1);
			}
			
		}  catch (SQLException ex){
			System.out.println("SQLException: " + ex.getMessage());
		}
		return 0; 
	}

	// Creates SQL-statement and sends to database. Database returns top 3 links from Resources table based on type
	public ArrayList<String> getLinks(String type){
		// Create array containing top 3 links within each type, with empty string as default
		ArrayList<String> links = new ArrayList<String>(Arrays.asList("", "", "")); 
		double worstAVG = 0; 
		
		try  {
			stmt = conn.createStatement();
			String query = "SELECT * FROM Resources WHERE Type = '" + type + "'";
			
			if (stmt.execute(query)){
				rs = stmt.getResultSet();
			}
			
			while (rs.next()){
				
				// Check if Link is amongst top 3 based on average against worstAVG which contains AVG for current link nr.3
				double currAVG = getAVG(rs.getInt(1)); 
				if (currAVG>worstAVG){				
					worstAVG = currAVG;
					links.remove(2); 
					links.add(rs.getString(2));		
				}
			}
			
		}  catch (SQLException ex){
			System.out.println("SQLException: " + ex.getMessage());
		}
		
		return links; 
	}
	
	// Return average rating for LinkID
	public double getAVG(int linkID){
		ResultSet subRs = null;
		try  {
			stmt = conn.createStatement();
			String query = "SELECT AVG(Rating) FROM Feedback WHERE LinkID = " + linkID;
			
			if (stmt.execute(query)){
				subRs = stmt.getResultSet();
			}
			
			while (subRs.next()){
				if (subRs.getDouble(1) == 0){
					return 2.5; 
				}
				return subRs.getDouble(1);
			}
		}  catch (SQLException ex){
			System.out.println("SQLException: " + ex.getMessage());
		}
		return -1; 
	}
	
}
