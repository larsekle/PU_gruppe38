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
	
	public void insert(int StudentID, int DateTime, int Assignment, int Exercise, int Tag, int Codeline, char FE){
		try{
			String query = String.format("INSERT INTO Failures (StudentID, DateTime, Assignment, Exercise, Tag, Codeline, FE) VALUES (%s, %s, %s, %s, %s, %s, '%s');", StudentID, DateTime, Assignment, Exercise, Tag, Codeline, FE);
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
			
		} catch (SQLException ex){
			System.out.println("SQLException: " + ex.getMessage());
		}
	}
	
	public String getStudentID(){
		java.net.URL location = Account.class.getProtectionDomain().getCodeSource().getLocation();
        int count = 0;
        int index = 0;
        while (count<5){
	        	if (location.getFile().charAt(index) == '/'){
	        		count++;
	        	}
	        	index++;
        }        
        return location.getFile().substring(0,index);
	}
	
	private int filepathToID(String filepath){
	try {
		stmt = conn.createStatement();
		
		String query = "SELECT COUNT(*) FROM STUDENTID WHERE Filepath = '"+filepath+"';";
		if (stmt.execute(query)){
			rs = stmt.getResultSet();
		}
		int count = 0;
		while (rs.next()){
			count = Integer.parseInt(rs.getString(1));
		}
		if (count == 0){
			//SETT INN FILEPATH i StudentID-tabellen
		}
		//HENT UT ID BASERT PÅ FILEPATH
		
		
	} catch (SQLException ex){
		System.out.println("SQLException: " + ex.getMessage());
	}
	return 0;
	}
	
	
	
	public static void main(String[] args) {
		JDBC jdbc = new JDBC();
		jdbc.connect();
		jdbc.insert(2, 2, 2, 2, 2, 2, 'F');
		jdbc.view();
	}
}
