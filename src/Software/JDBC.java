package Software;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;


import java.text.SimpleDateFormat;

public class JDBC {
	private final int LIMIT_INCREMENT;  
	private Connection conn = null;
	private Statement stmt = null; 
	private ResultSet rs = null;
	
	public JDBC(){
		LIMIT_INCREMENT = 2;
		connect(); 
	}
	
	public JDBC(int LIMIT_INCREMENT){
		this.LIMIT_INCREMENT = LIMIT_INCREMENT;  
		connect(); 
	}
	
	// Connects Eclipse user to SQL database
	public boolean connect(){
		try {
			conn = DriverManager.getConnection("jdbc:mysql://mysql.stud.ntnu.no/larsekle_tdt4140database?user=larsekle_tdt4140&password=PUgruppe38");
			stmt = conn.createStatement();
			return true; 
		} catch (Exception ex){
			System.out.println("SQLException: "+ex.getMessage());
			return false; 
		}
	}
			
	// Inserts new row into Failures table
	public boolean insertFailure(int Assignment, int Exercise, String tag, String FE){
		
		if (!Hashtag.TAGS.contains(tag) && !tag.equals("TEST")){
			return false; 
		}
		
		try{
			
			// Sets date based on system clock and formats as SQL DateTime
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentTime = sdf.format(dt);
			
			// Creates query and sends it to the database
			String query = String.format("INSERT INTO Failures (StudentID, DateTime, Assignment, Exercise, Tag, FE) VALUES (%s, '%s', %s, %s, '%s', '%s');", getStudentID(), currentTime, Assignment, Exercise, tag, FE);
			stmt.executeUpdate(query);
			return true; 
		} catch (SQLException ex){
			System.out.println("SQLException: " + ex.getMessage());
			return false; 
		}
	}
	
	// Inserts new row into Feedback table
	public boolean insertFeedback(int linkID, int studID, int rating){
		
		try{
			// Creates query and sends it to the database
			String query = String.format("INSERT INTO Feedback (StudentID, LinkID, Rating) VALUES (%s, %s, %s);", studID, linkID, rating);
			stmt.executeUpdate(query);
			return true;
		} catch (SQLException ex){
			System.out.println("SQLException: " + ex.getMessage());
			return false;
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
		return -1; 
	}
	
	// Get linkID from Resources table based on entire link
	public int getLinkID(String link){
		try  {
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
		return -1; 
	}

	// Creates SQL-statement and sends to database. Database returns top 3 links from Resources table based on type and tag
	public ArrayList<String> getLinks(String type, String tag){
		
		ArrayList<String> links = new ArrayList<String>(3); 
		
		// Lets SQL do the work on calculating and ordering best average rating on links
		try  {
			String query = String.format("SELECT Resources.Link, Resources.Type, Resources.Tag FROM Resources "
					+ "INNER JOIN Feedback ON Resources.LinkID=Feedback.LinkID "
					+ "WHERE Tag = '%s' AND Type = '%s' "
					+ "GROUP BY Resources.LinkID "
					+ "ORDER BY AVG(Feedback.Rating) DESC;", tag, type);
			
			
			if (stmt.execute(query)){
				rs = stmt.getResultSet();
			}
			
			for (int i = 0; i<3; i++){
				if (rs.next()){
					links.add(rs.getString(1)); 
				} else{
					links.add("");
				}
			}
			
			return links; 
						
		}  catch (SQLException ex){
			System.out.println("SQLException: " + ex.getMessage());
		}
		
		return null; 
}
	
	// Gets the last Tag value inserted to the Failures table, based on largest FailID on the StudentID. 
	public String getLastTag(){
		try  {
			// Sets date based on system clock and formats as SQL DateTime
			Date dt = new Date();
			dt = new Date(dt.getTime() - 120000); 
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentTime = sdf.format(dt);
			
			
			String query = String.format("SELECT Tag, DateTime FROM Failures WHERE DateTime = (SELECT MAX(DateTime) FROM Failures WHERE StudentID = %s) AND DateTime > '%s'", getStudentID(), currentTime) ;
			
			if (stmt.execute(query)){
				rs = stmt.getResultSet();
			}
			
			if (rs.next()){
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
			
			// Get count of failures registered on student, assignment, exercise 
			String query = String.format("SELECT COUNT(*) FROM Failures WHERE Assignment = %s AND Exercise = %s AND StudentID = %s AND Tag = '%s'", assignment, exercise, studID, tag); 
			
			if (stmt.execute(query)){
				rs = stmt.getResultSet();
			}
			
			if (rs.next()){
				count = rs.getInt(1); 
			} else{
				throw new IllegalStateException(); 
			}
			
			// Check whether Tag exists in FailureLimit and creates row if not
			query = String.format("SELECT COUNT(CurrentLimit) FROM FailureLimit WHERE Assignment = %s AND Exercise = %s AND StudentID = %s AND Tag = '%s'", assignment, exercise, studID, tag);
			
			if (stmt.execute(query)){
				rs = stmt.getResultSet();
			}
			
			if (rs.next()){
				limit = rs.getInt(1); 
				
				// Create new CurrentLimit row for Student, Assignment, Exercise and Tag
				if (limit < 1){
					
					query = String.format("INSERT INTO FailureLimit(StudentID, Assignment, Exercise, Tag, CurrentLimit) VALUES (%s, %s, %s, '%s', %s);", studID, assignment, exercise, tag, LIMIT_INCREMENT); 
					stmt.executeUpdate(query);
				}
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
	
	// Sends user data from registration form to User table in database
	public boolean sendUserData(String username, String password, String firstName, String lastName, String emailAddress, String studentAssistant){
		try{
			
			// Creates query and sends it to the database
			String query = String.format("INSERT INTO Users (Username, Password, Firstname, Lastname, Email, Position, StudentID, StudentAssistantID) VALUES ('%s', '%s', '%s', '%s', '%s', 'Student', %s, '%s');", username, password, firstName, lastName, emailAddress, getStudentID(), getStudentAssistantID(studentAssistant));
			stmt.executeUpdate(query);
			return true; 
		} catch (SQLException ex){
			System.out.println("SQLException: " + ex.getMessage());
			return false; 
		}
	}
	
	// Convert student assistant name to ID
	public int getStudentAssistantID(String name){
		String query = String.format("SELECT StudentAssistantID FROM Users WHERE CONCAT(FirstName,' ', LastName) = '%s' AND Position = 'StudentAssistant'", name); 
	
		try {
			
			if (stmt.execute(query)){
				rs = stmt.getResultSet();
			} if (rs.next()){
				return rs.getInt(1); 
			} else{
				throw new IllegalStateException(); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} return 0; 
	}
	
	// Check whether user is already in DB 
	public boolean userExists(){
		try  {
			String query = "SELECT COUNT(*) FROM Users WHERE Position = 'Student' AND StudentID = " + getStudentID();
			
			if (stmt.execute(query)){
				rs = stmt.getResultSet();
			}
			
			if (rs.next() && rs.getInt(1)>0){
				return true;
			}
			
		}  catch (SQLException ex){
			System.out.println("SQLException: " + ex.getMessage());
		}
		return false; 
	}

	// Gets all student assistants from Users table
	public ArrayList<String> getStudentAssistants(){
		ArrayList<String> studentAssistant = new ArrayList<String>(); 
			
		try  {
			String query = String.format("SELECT FirstName, LastName FROM Users WHERE Position = 'StudentAssistant';");	
			
			if (stmt.execute(query)){
				rs = stmt.getResultSet();
			}
			
			while (rs.next()){
				studentAssistant.add(rs.getString(1)+" "+rs.getString(2)); 		
			}
			
			return studentAssistant; 
						
		}  catch (SQLException ex){
			System.out.println("SQLException: " + ex.getMessage());
		}
		
		return null; 
	}
	
	// Inserts optional query in DB
	public boolean insertQuery(String query){
		
		try{
			
			// Sends query to DB
			stmt.executeUpdate(query);
			return true; 
		} catch (SQLException ex){
			System.out.println("SQLException: " + ex.getMessage());
			return false; 
		}
	}
	
	// Gets connection for test purpose
	public Connection getConnection(){
		return conn; 
	}

}
