package Software;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;


import java.text.SimpleDateFormat;

public class JDBC {
	private final int LIMIT_INCREMENT;  
	private Connection conn = null;
	
	public JDBC(){
		LIMIT_INCREMENT = 0;
	}
	
	public JDBC(int LIMIT_INCREMENT){
		this.LIMIT_INCREMENT = LIMIT_INCREMENT;  
	}
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
	
		
	// Inserts new row into Failures table
	public void insertFailure(int Assignment, int Exercise, String tag, int Codeline, char FE){
		try{
			
			
			// Sets date based on system clock and formats as SQL DateTime
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentTime = sdf.format(dt);
			
			// Creates query and sends it to the database
			String query = String.format("INSERT INTO Failures (StudentID, DateTime, Assignment, Exercise, Tag, Codeline, FE) VALUES (%s, '%s', %s, %s, '%s', %s, '%s');", getStudentID(), currentTime, Assignment, Exercise, tag, Codeline, FE);
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
			
		} catch (SQLException ex){
			System.out.println("SQLException: " + ex.getMessage());
		}
	}
	
	// Inserts new row into Feedback table
	public void insertFeedback(int linkID, int studID, double rating){
		try{
			
			// Creates query and sends it to the database
			String query = String.format("INSERT INTO Feedback (StudentID, LinkID, Rating) VALUES (%s, %s, %s);", studID, linkID, rating);
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
			
		} catch (SQLException ex){
			System.out.println("SQLException: " + ex.getMessage());
		}
	}
		
	// Gets the users StudentID based om eclipse project filepath
	public int getStudentID(){
		java.net.URL location = JDBC.class.getProtectionDomain().getCodeSource().getLocation();
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
	public ArrayList<String> getLinks(String type, String tag){
		// Create array containing top 3 links within each type, with empty string as default
		HashMap<String, Double> links = new HashMap<String, Double>();	
		
		// in case 
		for (int i = 0; i<3; i++){
			links.put("", 0.0); 
		}
		
		try  {
			stmt = conn.createStatement();
			String query = "SELECT * FROM Resources WHERE Type = '" + type + "' AND Tag = '" + tag + "'";
			
			if (stmt.execute(query)){
				rs = stmt.getResultSet();
			}
			
			while (rs.next()){
				
				// Insert links-map with each link and their corresponding average rating 			
				links.put(rs.getString(2), getAVG(rs.getInt(1))); 
			}	
		}  catch (SQLException ex){
			System.out.println("SQLException: " + ex.getMessage());
		}
		
		// Sorts rating with largest first
		ArrayList<Double> rating = new ArrayList<Double>(links.values()); 
		ArrayList<String> topLinks = new ArrayList<String>(); 
		Collections.sort(rating);
				
		// Adds all links with highest average rating first 
		if (links.size()<3){
			
			// Get as many links as the database contains
			for (int i=0; i<links.size(); i++){
				double valueForLink = rating.remove(rating.size()-1); 
				for (String link : links.keySet() ){
					if (links.get(link).equals(valueForLink)){
						topLinks.add(link);
					}
				}
			}
			
			// Fill up empty space with blank lines
			for (int i=0; i<(3-links.size()); i++){
				topLinks.add("");
			}
		} else{
			// Fill up with three top links
			for (int i=0; i<3; i++){
				double valueForLink = rating.remove(rating.size()-1); 
				for (String link : links.keySet() ){
					if (links.get(link).equals(valueForLink)){
						topLinks.add(link);
					}
				}
			}
		}
		
		return new ArrayList<String>(topLinks.subList(0, 3)); 
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
	
	// Gets the last Tag value inserted to the Failures table, based on largest FailID on the StudentID. 
	public String getLastTag(){
		try  {
			stmt = conn.createStatement();
			String query = "SELECT Tag FROM Failures WHERE DateTime = (SELECT MAX(FailID) FROM Failures WHERE StudentID = '"+ getStudentID() +"')";
			
			if (stmt.execute(query)){
				rs = stmt.getResultSet();
			}
			
			while (rs.next()){
				return rs.getString(1);
			}
		}  catch (SQLException ex){
			System.out.println("SQLException: " + ex.getMessage());
		}
		return "-1"; 
	}
	
	// Return true if limit is reached
	public boolean limitReached(String tag, int assignment, int exercise){
		// Check if limit is 0, and if so create new row. If limit not 0 then change existing row.
		int limit = 0;
		int count = 0; 
		int studID = getStudentID(); 
		
		
		try  {
			stmt = conn.createStatement();
			
			
			// Get count of failures registered on student, assignment, exercise 
			String query = String.format("SELECT COUNT(*) FROM Failures WHERE Assignment = %s AND Exercise = %s AND StudentID = %s AND Tag = %s", assignment, exercise, studID, tag); 
			
			if (stmt.execute(query)){
				rs = stmt.getResultSet();
			}
			
			if (rs.next()){
				count = rs.getInt(1); 
			} else{
				throw new IllegalStateException(); 
			}
			
			// Get current limit for when BuddyBOT should assist the student, and compare with 'count'
			query = String.format("SELECT CurrentLimit FROM FailureLimit WHERE Assignment = %s AND Exercise = %s AND StudentID = %s AND Tag = '%s'", assignment, exercise, studID, tag);
			
			if (stmt.execute(query)){
				rs = stmt.getResultSet();
			}
			
			if (rs.next()){
				limit = rs.getInt(1); 
				
				// Increase CurrentLimit in StudentLimit table
				if (count >= limit){
					
					query = String.format("UPDATE FailureLimit SET CurrentLimit = %s WHERE  Assignment = %s AND Exercise = %s AND StudentID = %s AND Tag = '%s'", limit+LIMIT_INCREMENT, assignment, exercise, studID, tag); 
					stmt.executeUpdate(query);
					
				}
				return count >= limit; 
				
			} else{
				
				// Creates new row with start limit
				
				limit += LIMIT_INCREMENT; 
				query = String.format("INSERT INTO FailureLimit(Assignment, Exercise, Tag, StudentID, CurrentLimit) VALUES (%s, %s, '%s', %s, %s)", assignment, exercise, tag, studID, limit); 
				stmt.executeUpdate(query);
				
				return count >= limit; 
			}
			
		}  catch (SQLException ex){
			System.out.println("SQLException: " + ex.getMessage());
		}
		
		return false; 
	}
	
	// Gets connection for test purpose
	public Connection getConnection(){
		return conn; 
	}
	
}
