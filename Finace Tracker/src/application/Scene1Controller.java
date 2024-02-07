package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.cj.protocol.Resultset;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class Scene1Controller {
    @FXML
    private TextField Uname,Rname;
    @FXML
   private Button register;
    @FXML
    private PasswordField Upass,Rpass;
    @FXML
    private BorderPane Loginpage,siguppage;
    private Alert alert;
	
	
	public void Login(ActionEvent event)  {
    	 try {
		   String query = "Select * from Users";
		   Connection con = JDBCconct.DataBaseconnection();
	  	   Statement st =con.createStatement();
		   ResultSet rs=st.executeQuery(query);
		   boolean found=false;
		   while(rs.next()) {
	       if(rs.getString(1).equals(Uname.getText()) && rs.getString(2).equals(Upass.getText())) {
	    	   JDBCconct.Database=Uname.getText();
	    	   found = true;
	    	   JDBCconct.Database=Uname.getText();
	    	   Parent root = FXMLLoader.load(getClass().getResource("Scene2.fxml"));
	    	   Scene scene = new Scene(root);
	    	   Stage stage =(Stage)((Node)event.getSource()).getScene().getWindow();
	    	   stage.setScene(scene);
	    	   stage.show();
	    	}
	       if(!found) {
	        	alert = new Alert(AlertType.ERROR);	
	            alert.setTitle("Error message");
	      		alert.setHeaderText(null);
	      		alert.setContentText("Incorrect username Or passwors");
	      	    alert.showAndWait();  	
	        }
	        }
    	 }
    	 
    	 catch(Exception e) {
    		 e.printStackTrace();
    	 }
	    
	        
	        
	}
    
    
    
	public void switchscene(ActionEvent event) {
    	siguppage.setVisible(true);
    }
	
	public void switchscene1(ActionEvent event) {
    	siguppage.setVisible(false);
    }
	
 public void signin(ActionEvent event) {
try {
	 if(Rname.getText().length() <4 || Rpass.getText().length()<8) {
		alert = new Alert(AlertType.ERROR);	
        alert.setTitle("Error message");
		alert.setHeaderText(null);
		alert.setContentText("Make sure your password length is 8 and above \nAnd your Username length is 4 and above");
	    alert.showAndWait();  	
	}
	 
	 else if(JDBCconct.check(Rname.getText())) {
		    alert = new Alert(AlertType.ERROR);	
	        alert.setTitle("Error message");
			alert.setHeaderText(null);
			alert.setContentText("Username already exixts");
		    alert.showAndWait();  	
	}
		
	 
	 
	else {	
		String query="insert into Users values(?,?)";
	    Connection con = JDBCconct.DBconnection();
		PreparedStatement pt =con.prepareStatement(query);
		pt.setString(1, Rname.getText());
		pt.setString(2, Rpass.getText());
		pt.executeUpdate();
		siguppage.setVisible(false);
	    JDBCconct.createDB(Rname.getText()); 
	       
	}
 }	
	catch(Exception e) {
		e.printStackTrace();
	}
	
}
	
	
	
}
