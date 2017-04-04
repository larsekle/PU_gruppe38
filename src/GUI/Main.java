package GUI;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.tracing.dtrace.ArgsAttributes;

import Software.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application implements Runnable{
	
	public static boolean test = false; 

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	Application.launch(Main.class, (java.lang.String[])null);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
        	JDBC database = new JDBC();  
        	database.connect();
        	
        	AnchorPane page;
        	Scene scene;
        	
        	if (!database.userExists() || test){
        		 page= (AnchorPane) FXMLLoader.load(Main.class.getResource("Register.fxml"));
                 scene = new Scene(page);
                 scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
                 primaryStage.setScene(scene);
                 primaryStage.setTitle("BuddyBOT Registration Window");
                 if (!test) primaryStage.show();
                                        
        	} 
        	if (database.userExists() || test){
        		page = (AnchorPane) FXMLLoader.load(Main.class.getResource("BuddyGUI.fxml"));
                scene = new Scene(page);
                scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
                primaryStage.setScene(scene);
                primaryStage.setTitle("BuddyBOT Execution Window");
                primaryStage.show();
//                primaryStage.close();
        	} 
        	
        	if (test) new AskForHelp().start(primaryStage);
        	
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

	@Override
	public void run() {
		main((String[]) null);
	} 
}
