package application;

import java.net.URL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class Scene2Controller implements Initializable{
      @FXML
      private PieChart piechart;
      @FXML
      private ComboBox<String> Category;
      @FXML
      private TableColumn<Details, String> cat;
       @FXML
      private TableColumn<Details, Date> dt;
       @FXML
      private TableColumn<Details, Long> expense;
       @FXML
       private TableColumn<Details, String> rsn2;
       @FXML
       private TableColumn<Details, String> rsn1;
       @FXML
       private TableColumn<Details, Long> income;
       @FXML
       private TableColumn<Details, Long> Total_income;
       @FXML
       private TableColumn<Details, Long> Total_expense;
       @FXML
       private TableView<Details> Tview;
       @FXML
       private TextField Income,reasonbox1,Expense,reasonbox2,limitbox; 
       
     
       private static Alert alert;
      
       
       
       ObservableList<Details> Tablelist =FXCollections.observableArrayList();
       ObservableList<PieChart.Data> pielist =FXCollections.observableArrayList();
       String[] arr = {"Entertainment","bussiness","sports","fitness","Outing","Others"};
      
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
            pielist.add(new PieChart.Data("Entertainment",0));
    	    pielist.add(new PieChart.Data("bussiness",0));
    	    pielist.add(new PieChart.Data("sports",0));
    	    pielist.add(new PieChart.Data("fitness",0));
    	    pielist.add(new PieChart.Data("Outing",0));
    	    pielist.add(new PieChart.Data("Others",0));
	        piechart.getData().addAll(pielist);
	        piechart.setTitle("Budget Details");
	        Category.getItems().addAll(arr);

	income.setCellValueFactory(new PropertyValueFactory<Details,Long>("income"));
	rsn1.setCellValueFactory(new PropertyValueFactory<Details,String>("incomereason"));
	expense.setCellValueFactory(new PropertyValueFactory<Details,Long>("amountspent"));
	rsn2.setCellValueFactory(new PropertyValueFactory<Details,String>("expensereason"));
	cat.setCellValueFactory(new PropertyValueFactory<Details,String>("category"));
	dt.setCellValueFactory(new PropertyValueFactory<Details,Date>("date"));
    
    Total_income.setCellValueFactory(new PropertyValueFactory<Details,Long>("Tincome"));
    Total_expense.setCellValueFactory(new PropertyValueFactory<Details,Long>("Texpense"));
    

	try {
		JDBCconct.display(Tablelist, pielist);
	} catch (SQLException e) {
		e.printStackTrace();
}
	Tview.setItems(Tablelist);
	   Tview.setOnMouseClicked(event -> {
	        if(event.getClickCount() ==1) {
		    	Details SelectedItem =Tview.getSelectionModel().getSelectedItem();
		    	if(SelectedItem != null) {
		    		if(SelectedItem.income==0)Income.setText("");
		    		else Income.setText(String.valueOf(SelectedItem.income));
		    		reasonbox1.setText(SelectedItem.incomereason);
		    	    if(SelectedItem.amountspent==0)Expense.setText("");
		    	    else Expense.setText(String.valueOf(SelectedItem.amountspent));	
		    	    reasonbox2.setText(SelectedItem.expensereason);
		    	    Category.setValue(SelectedItem.category);   
		    	}
		    	}
	     	}
		    	
		    );	
  }

   
    
    public void add(){
  try {
  if(Income.getText().isEmpty() && Expense.getText().isEmpty()) alertError();
  else if ((!Income.getText().isEmpty()  && reasonbox1.getText().equals("")) ||(Income.getText().isEmpty()   && !reasonbox1.getText().equals("")) )alertError(); 
  else if ((!Expense.getText().isEmpty() && reasonbox2.getText().equals("")) || (Expense.getText().isEmpty()  &&  !reasonbox2.getText().equals("")))alertError(); 
  else if ((!Expense.getText().isEmpty() && !reasonbox2.getText().equals("") && Category.getValue() ==null) || (!Expense.getText().isEmpty()  &&  !reasonbox2.getText().equals("")&& Category.getValue() ==null))alertError();
  else if(!Income.getText().equals("") && Expense.getText().equals("") && Category.getValue() !=null) {
	  alertInfo();
      Category.setValue(null);
  }
  
  else {

    if(Income.getText().equals(""))  Income.setText("0");
   
   if(Expense.getText().equals(""))    Expense.setText("0");
   
	 
     JDBCconct.adddata(Long.parseLong(Income.getText()),
				          reasonbox1.getText(),
	    		          Long.parseLong(Expense.getText()),
	     		          reasonbox2.getText(),
	    		          Category.getValue(),
	    		          Tablelist,
	    		          Tview
	    		           );
      if(Category.getValue()!=null) {
    	  updatepiechart(pielist,Category.getValue());
    	  Category.setValue(null);
          Category.setPromptText("hi");
     
      }

      clears();
  }
 
  }
 
  catch(NumberFormatException e) {
	  alert = new Alert(AlertType.ERROR);	
      alert.setTitle("Error Message");
      alert.setHeaderText(null);
      alert.setContentText("Kindly enter the appropriate one in textBox");
      alert.showAndWait();
  } 

  catch(Exception e) {
	  e.printStackTrace();
  }
  
   }

	    
       	   
           public void clear() throws SQLException {
   	    	   try {   
	        Tablelist.removeAll(Tablelist);
	    	JDBCconct.cleardata();
			Tview.setItems(Tablelist);
	        for(int i=0;i<pielist.size();i++) {
	        pielist.get(i).setPieValue(0);		
	        pielist.get(i).setName(arr[i]+" "+pielist.get(i).getPieValue()+"%");
	        }
	        clears();
        	}
        	catch(Exception e) {
        		e.printStackTrace();
        	}
	        
	        
	    }

	    
	    public void delete() {
      	Details SelectedItem =Tview.getSelectionModel().getSelectedItem();
      	try {
      	if(SelectedItem !=null ) {
      		if(Tablelist.size()==1)clear();
      		else JDBCconct.Deletedata(SelectedItem,Tview,Tablelist,pielist);
      		clears();
      	}
      	}
      	catch(Exception e) {
      		e.printStackTrace();
      	}
      	
      	   }
	    
	    
	    
	  public void update() {
       try {
	   if(Income.getText().isEmpty() && Expense.getText().isEmpty()) alertError();
       else if ((!Income.getText().isEmpty()  && reasonbox1.getText().equals("")) ||(Income.getText().isEmpty()   && !reasonbox1.getText().equals("")) )alertError(); 
       else if ((!Expense.getText().isEmpty() && reasonbox2.getText().equals("")) || (Expense.getText().isEmpty()  &&  !reasonbox2.getText().equals("")))alertError(); 
       else if ((!Expense.getText().isEmpty() && !reasonbox2.getText().equals("") && Category.getValue() ==null) || (!Expense.getText().isEmpty()  &&  !reasonbox2.getText().equals("")&& Category.getValue() ==null))alertError();
       else if(!Income.getText().equals("") && Expense.getText().equals("") && Category.getValue() !=null) {
     	  alertInfo();
           Category.setValue(null);
       }
       else { 
    	if(Income.getText().equals(""))  Income.setText("0");
    	if(Expense.getText().equals(""))    Expense.setText("0");
    	
    	Details SelectedItem =Tview.getSelectionModel().getSelectedItem();
        if(SelectedItem !=null ) { 
		 JDBCconct.updatedata(SelectedItem,Long.parseLong(Income.getText()),
			           reasonbox1.getText(),
			           Long.parseLong(Expense.getText()),
			           reasonbox2.getText(),
			           Category.getValue(),
			           Tablelist,
			           Tview,
			           pielist);
		   clears();
		   }
       }
	  
	} 
       catch(NumberFormatException e) {
    		  alert = new Alert(AlertType.ERROR);	
    	      alert.setTitle("Error Message");
    	      alert.setHeaderText(null);
    	      alert.setContentText("Kindly enter the appropriate one in textBox");
    	      alert.showAndWait();
    	  } 

    	  catch(Exception e) {
    		  e.printStackTrace();
    	  }	  
	}
	
	    
   public static void updatepiechart(ObservableList<PieChart.Data> list,String category) {
   double count1=0;
   if(category.equals("Entertainment"))count1 = JDBCconct.ent_count;
   else if(category.equals("bussiness"))count1=JDBCconct.bus_count;
   else if(category.equals("sports"))count1=   JDBCconct.sprt_count;
   else if(category.equals("fitness"))count1=  JDBCconct.fit_count;
   else if(category.equals("Outing"))count1=   JDBCconct.outing_count;
   else count1=JDBCconct.others_count;
  
									  
		  for(int i=0;i<list.size();i++) {
	         String sub=list.get(i).getName().substring(0, 2);
	         if(sub.equals(category.substring(0, 2))) {
	        	 list.get(i).setPieValue(count1);
	        	 list.get(i).setName(category+" "+list.get(i).getPieValue()+"% ");
	        	 break;
	         };
		  }
		  
   
	    }
	  
	  
  public static void alertError() {
	  alert = new Alert(AlertType.ERROR);	
      alert.setTitle("Error Message");
      alert.setHeaderText(null);
      alert.setContentText("Kindly fill the missing textBox");
      alert.showAndWait();
}

  public static void alertInfo() {
	  alert = new Alert(AlertType.INFORMATION);	
      alert.setTitle("Information");
      alert.setHeaderText(null);
      alert.setContentText("Category is only applicable for Expenses");
      alert.showAndWait();
}  
	  
  
  public void clears() {
	  Income.setText("");
      Expense.setText("");
      reasonbox1.setText("");
      reasonbox2.setText("");
      Category.setValue(null);
  }

    }



