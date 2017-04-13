package Software;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;


import java.text.SimpleDateFormat;

/**
 * @author Lars E. Kleiven, Ingrid E. Hermanrud, Sigrid L. Fosen, Helena Van de Pontseele
 *
 */
public class JDBC {
	private final int LIMIT_INCREMENT;  
	private Connection conn = null;
	private Statement stmt = null;  
	private ResultSet rs = null;
	
	/**
	 * Constructor for JDBC with default LIMIT_INCREMENT for number of errors between each time BuddyBOT should help
	 */
	public JDBC(){
		LIMIT_INCREMENT = 2;
		connect(); 
	}
	
	/**
	 * Constructor for JDBC with input LIMIT_INCREMENT for number of errors between each time BuddyBOT should help
	 * @param LIMIT_INCREMENT
	 */
	public JDBC(int LIMIT_INCREMENT){
		this.LIMIT_INCREMENT = LIMIT_INCREMENT;  
		connect(); 
	}
	
	/**
	 * Constructor for JDBC for test-use. Does not connect to DB. 
	 * @param test 
	 */
	public JDBC(boolean test){
		if (!test) connect();  
		LIMIT_INCREMENT = 2;
	}
	
	/**
	 * Creates connection to SQL database. Will only succeed when connected to the NTNU VPN.
	 * @return whether the operation was successful 
	 */
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
			
	/**
	 * Inserts new row into Failures table
	 * @param Assignment
	 * @param Exercise
	 * @param tag
	 * @param FE
	 * @return whether the operation was successful
	 */
	public boolean insertFailure(int Assignment, int Exercise, String tag, String FE){
		
		if (!Hashtag.TAGS.contains(tag) && !tag.equals("TEST")){
			return false; 
		}
		try{
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentTime = sdf.format(dt);
			
			String query = String.format("INSERT INTO Failures (StudentID, DateTime, Assignment, Exercise, Tag, FE) VALUES (%s, '%s', %s, %s, '%s', '%s');", getStudentID(), currentTime, Assignment, Exercise, tag, FE);
			stmt.executeUpdate(query);
			return true; 
		} catch (Exception e){
			System.out.println("Exception: " + e.getMessage());
			return false; 
		}
	}
	
	/**
	 * Inserts new row into Feedback table by creating a query and sending it to the database.
	 * @param linkID
	 * @param studID
	 * @param rating
	 * @param assignment
	 * @return whether the operation was successful
	 */
	public boolean insertFeedback(int linkID, int studID, int rating, int assignment){
		try{
			String query = String.format("INSERT INTO Feedback (StudentID, LinkID, Rating, Assignment) VALUES (%s, %s, %s, %s);", studID, linkID, rating, assignment);
			stmt.executeUpdate(query);
			return true;
		} catch (Exception e){
			System.out.println("Exception: " + e.getMessage());
			return false;
		}
	}
		
	/**
	 * Gets the users StudentID based on eclipse project filepath. Converts the filepath to StudentID from database.
	 * @return the StudentID from database or -1 if operation not successful
	 * @see filepathToID
	 */
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
	
	 
	/**
	 * Converts filepath to StudentID from database. First check whether student exists in database. 
	 * If student does not exist, a new studentID connected to the filepath will be created. 
	 * Then the studentID will be gathered from database.
	 * @param filepath
	 * @return the studentID from database or -1 if operation not successful.
	 */
	private int filepathToID(String filepath){
		try {
			
			String query = "SELECT COUNT(*) FROM StudentID WHERE Filepath = '"+filepath+"';";
			if (stmt.execute(query)){
				rs = stmt.getResultSet();
			}
			int count = 0;
			while (rs.next()){
				count = Integer.parseInt(rs.getString(1));
			}
			
			
			if (count == 0){
				query = String.format("INSERT INTO StudentID (Filepath) VALUES ('%s');", filepath);
				stmt.executeUpdate(query);
			}
			
			query = "SELECT StudentID FROM StudentID WHERE Filepath = '"+filepath+"';";
			if (stmt.execute(query)){
				rs = stmt.getResultSet();
			}
			while (rs.next()){
				return rs.getInt(1);
			}
			
		} catch (Exception e){
			System.out.println("Exception: " + e.getMessage());
		}
		return -1; 
	}
	
	 
	/**
	 * Get linkID from Resources table based on link-URL.
	 * @param link
	 * @return linkID from database and -1 if not found
	 */
	public int getLinkID(String link){
		try  {
			String query = "SELECT * FROM Resources WHERE Link = '" + link + "'";
			
			if (stmt.execute(query)){
				rs = stmt.getResultSet();
			}
			
			while (rs.next()){
				return rs.getInt(1);
			}
			
		}  catch (Exception e){
			System.out.println("Exception: " + e.getMessage());
		}
		return -1; 
	}

	 
	/**
	 * Requests links from database based on type and tag, and return them ordered by average rating from other students.
	 * @param type
	 * @param tag
	 * @return top three links from database or null if operation not successful
	 */
	public ArrayList<String> getLinks(String type, String tag){
		
		ArrayList<String> links = new ArrayList<String>(3); 
		
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
			} return links; 
		}  catch (Exception e){
			System.out.println("Exception: " + e.getMessage());
		}  return null; 
	}
	
	 
	/**
	 * Gets the last Tag value inserted to the Failures table, based on largest FailID on the StudentID if there any was inserted less than two minutes ago. 
	 * @return last Tag value if failures where inserted within time limit
	 */
	public String getLastTag(){
		try  {
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
		}  catch (Exception e){
			System.out.println("Exception: " + e.getMessage());
		}
		return "-1"; 
	}
	
	 
	/**
	 * Gets the last Assignment value inserted to the Failures table, based on largest FailID on the StudentID if there any was inserted less than two minutes ago. 
	 * @return last Assignment value if failures where inserted within time limit
	 */
	public int getLastAssignment(){
		try  {
			Date dt = new Date();
			dt = new Date(dt.getTime() - 120000); 
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentTime = sdf.format(dt);
			
			String query = String.format("SELECT Assignment, DateTime FROM Failures WHERE DateTime = (SELECT MAX(DateTime) FROM Failures WHERE StudentID = %s) AND DateTime > '%s'", getStudentID(), currentTime) ;
			
			if (stmt.execute(query)){
				rs = stmt.getResultSet();
			}
			if (rs.next()){
				return rs.getInt(1);
			}
		}  catch (Exception e){
			System.out.println("Exception: " + e.getMessage());
		}  return -1; 
	}
	
	/**
	 * Check whether it is time for BuddyBOT to help the student. Compares number of failures within assignment, exercise and tag with next limit for when GUI gets activated.
	 * Get count of failures registered on student, assignment, exercise 
	 * Check whether Tag exists in FailureLimit and creates row if not
	 * Get current limit for when BuddyBOT should assist the student, and compare with 'count'
	 * Increase CurrentLimit in StudentLimit table if limit is reached. 
	 * Increase CurrentLimit based on LIMIT_INCREMENT
	 * @param tag
	 * @param assignment
	 * @param exercise
	 * @return true if limit reached, false if not. 
	 */
	public boolean limitReached(String tag, int assignment, int exercise){
		int limit = 0;
		int count = 0; 
		int studID = getStudentID(); 
		
		try  {	
			String query = String.format("SELECT COUNT(*) FROM Failures WHERE Assignment = %s AND Exercise = %s AND StudentID = %s AND Tag = '%s'", assignment, exercise, studID, tag); 
			
			if (stmt.execute(query)){
				rs = stmt.getResultSet();
			}
			if (rs.next()){
				count = rs.getInt(1); 
			} else{
				throw new IllegalStateException(); 
			}
			
			query = String.format("SELECT COUNT(CurrentLimit) FROM FailureLimit WHERE Assignment = %s AND Exercise = %s AND StudentID = %s AND Tag = '%s'", assignment, exercise, studID, tag);
			
			if (stmt.execute(query)){
				rs = stmt.getResultSet();
			}			
			if (rs.next()){
				limit = rs.getInt(1);
				if (limit < 1){
					query = String.format("INSERT INTO FailureLimit(StudentID, Assignment, Exercise, Tag, CurrentLimit) VALUES (%s, %s, %s, '%s', %s);", studID, assignment, exercise, tag, LIMIT_INCREMENT); 
					stmt.executeUpdate(query);
				}
			}
			
			query = String.format("SELECT CurrentLimit FROM FailureLimit WHERE Assignment = %s AND Exercise = %s AND StudentID = %s AND Tag = '%s'", assignment, exercise, studID, tag);
			
			if (stmt.execute(query)){
				rs = stmt.getResultSet();
			}			
			if (rs.next()){
				limit = rs.getInt(1); 
				if (count >= limit){
					query = String.format("UPDATE FailureLimit SET CurrentLimit = %s WHERE  Assignment = %s AND Exercise = %s AND StudentID = %s AND Tag = '%s'", limit+LIMIT_INCREMENT, assignment, exercise, studID, tag); 
					stmt.executeUpdate(query);
				} 
				return count >= limit; 
			} else{
				limit += LIMIT_INCREMENT; 
				query = String.format("INSERT INTO FailureLimit(Assignment, Exercise, Tag, StudentID, CurrentLimit) VALUES (%s, %s, '%s', %s, %s)", assignment, exercise, tag, studID, limit); 
				stmt.executeUpdate(query);
				return count >= limit; 
			}
		}  catch (Exception e){
			System.out.println("Exception: " + e.getMessage());
		}  return false; 
	}
	
	 
	/**
	 * Sends user data from registration form to User table in database.
	 * All data are already validated in RegisterController.
	 * @param username
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @param emailAddress
	 * @param studentAssistant
	 * @return whether operation was successful
	 */
	public boolean sendUserData(String username, String password, String firstName, String lastName, String emailAddress, String studentAssistant){
		try{
			String query = String.format("INSERT INTO Users (Username, Password, Firstname, Lastname, Email, Position, StudentID, StudentAssistantID) VALUES ('%s', '%s', '%s', '%s', '%s', 'Student', %s, '%s');", username, password, firstName, lastName, emailAddress, getStudentID(), getStudentAssistantID(studentAssistant));
			stmt.executeUpdate(query);
			return true; 
		} catch (Exception e){
			System.out.println("Exception: " + e.getMessage());
			return false; 
		}
	}
	
	/**
	 * Convert student assistant name to ID
	 * @param name
	 * @return student assistant ID or -1 if operation not successful
	 */
	public int getStudentAssistantID(String name){
		String query = String.format("SELECT StudentAssistantID FROM Users WHERE CONCAT(FirstName,' ', LastName) = '%s' AND Position = 'StudentAssistant'", name); 
	
		try {
			if (stmt.execute(query)){
				rs = stmt.getResultSet();
			} if (rs.next()){
				return rs.getInt(1); 
			} else{
				return -1;  
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} return -1; 
	}
	
	/**
	 * Check whether user is already in database
	 * @return whether user exists in database
	 */
	public boolean userExists(){
		try  {
			String query = "SELECT COUNT(*) FROM Users WHERE Position = 'Student' AND StudentID = " + getStudentID();
			
			if (stmt.execute(query)){
				rs = stmt.getResultSet();
			}
			if (rs.next() && rs.getInt(1)>0){
				return true;
			}
		}  catch (Exception e){
			System.out.println("Exception: " + e.getMessage());
		}  return false; 
	}

	/**
	 * Gets all student assistants from Users table
	 * @return list of names of all student assistants
	 */
	public ArrayList<String> getStudentAssistants(){
		ArrayList<String> studentAssistant = new ArrayList<String>(); 
			
		try  {
			String query = String.format("SELECT FirstName, LastName FROM Users WHERE Position = 'StudentAssistant';");	
			
			if (stmt.execute(query)){
				rs = stmt.getResultSet();
			}
			while (rs.next()){
				studentAssistant.add(rs.getString(1)+" "+rs.getString(2)); 		
			} return studentAssistant; 
						
		}  catch (Exception e){
			System.out.println("Exception: " + e.getMessage());
		}  return null; 
	}
	
	/**
	 * Inserts optional query in database. Only statements accepted by the executeUpdate method are accepted. 
	 * @param query
	 * @return whether operation was successful 
	 */
	public boolean insertQuery(String query){
		try{
			stmt.executeUpdate(query);
			return true; 
		} catch (Exception e){
			System.out.println("Exception: " + e.getMessage());
			return false; 
		}
	}
	
	/**
	 * Return the connection established with database
	 * @return connection with database
	 */
	public Connection getConnection(){
		return conn; 
	}

}
