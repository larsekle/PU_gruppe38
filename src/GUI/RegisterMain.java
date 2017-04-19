package GUI;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * @author Lars E. Kleiven, Ingrid E. Hermanrud, Sigrid L. Fosen, Helena Van de Pontseele
 *
 */
public class RegisterMain extends Application implements Runnable{
	
	public static boolean test = false; 
	public static Stage stage = null; 
	
    /**
     * Main used to start the GUI
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	Application.launch(RegisterMain.class, (java.lang.String[])null);
    }

    /**
     * Start method to set up and load GUI for user registration.
     * CSS is added to FXML and shown to the user. 
     * @see javafx.application.Application#start(javafx.stage.Stage)
     */
    @Override
    public void start(Stage primaryStage) {
        try {
        	stage = primaryStage; 
        	AnchorPane page= (AnchorPane) FXMLLoader.load(RegisterMain.class.getResource("Register.fxml"));
        	Scene scene = new Scene(page);
	        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
	        primaryStage.setScene(scene);
	        primaryStage.setResizable(false);
	        primaryStage.setTitle("BuddyBOT Registration Window");
	        primaryStage.show();
	    	
	    } catch (Exception ex) {
	        Logger.getLogger(RegisterMain.class.getName()).log(Level.SEVERE, null, ex);
	    }
    }
    
	/** 
	 * Run method is used to run main method as Runnable object through the ExecutorService class.
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		test = true; 
		main((String[]) null);
	} 
}
