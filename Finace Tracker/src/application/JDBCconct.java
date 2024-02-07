package application;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.TableView;



public class JDBCconct{
	 static double  ent_count=0;
     static double  bus_count=0;
     static double  sprt_count=0;
     static double  fit_count=0;
     static double  others_count=0;
     static double  outing_count=0;
     static long    Totalincome=0;
     static long    Totalexpense=0;
     private static Connection con;
     private static PreparedStatement ps;
     private static Statement st;
     private static ResultSet rs;
     protected static String Database;
     
     
     public static void createDB(String DB) throws SQLException{
      String url ="jdbc:mysql://localhost:3306/";
   	  String username ="root",pass ="Chan@2001";
   	  con = DriverManager.getConnection(url,username,pass);
      st = con.createStatement();
      st.executeUpdate("create database "+DB); 
      st.executeUpdate("use "+DB);
      st.executeUpdate("CREATE TABLE Details (id bigint primary key auto_increment,income long ,incomereason VARCHAR(100),amtspend long,expensereason VARCHAR(100),category VARCHAR(50),date DATE,totalincome long,totalexpense long);");
      st.executeUpdate("create table piechart(Entertainment double,bussiness double,sports double,fitness double,Outing double,Others double);");
      st.executeUpdate("insert into piechart  values(0,0,0,0,0,0);");
     }
     
     public static Connection DataBaseconnection(){
    	 String url ="jdbc:mysql://localhost:3306/Myproject";
		   String username ="root",pass ="Chan@2001";
		   Connection con=null;
		try {
			con = DriverManager.getConnection(url,username,pass);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	  	   return con;   
	
     }
     
     
     public static Connection DBconnection(){
		   String url ="jdbc:mysql://localhost:3306/"+Database;
		   String username ="root",pass ="Chan@2001";
		   Connection con=null;
		try {
			con = DriverManager.getConnection(url,username,pass);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	  	   return con;   
	}


     public static boolean check(String v1) throws SQLException {
     Connection con=DBconnection();
	 String query = "select	* from Users";
	 Statement st =con.createStatement();
	 ResultSet rs=st.executeQuery(query);

	 while(rs.next()) {
		 if(rs.getString(1).equals(v1) ) return true;
	  }
  return false;
}
	
	
public static void adddata(long income,String incomereason,long amt,String expensereason,String catg, ObservableList<Details> detailslist,TableView<Details> Tview) throws SQLException {
  String qurey="INSERT INTO Details(income,incomereason,amtspend,expensereason, category, date,totalincome,totalexpense) VALUES(?,?,?,?,?,?,?,?)";
  con = JDBCconct.DBconnection();
  ps=con.prepareStatement(qurey);
      Date date = new Date(System.currentTimeMillis());
	  Totalincome+=income;
	  Totalexpense +=amt;
      ps.setLong(1, income);
	  ps.setString(2, incomereason);
	  ps.setLong(3, amt);      
      ps.setString(4, expensereason);
      ps.setString(5, catg);
      ps.setDate(6, date);
      ps.setLong(7, Totalincome);
      ps.setLong(8, Totalexpense);
      ps.executeUpdate();
  
       qurey ="select id,date from Details";
       st = con.createStatement(); 
       rs=st.executeQuery(qurey);
       Details dt =new Details(++Details.objid,income,incomereason,amt,expensereason,catg,date,Totalincome,Totalexpense);;
       detailslist.add(dt);
       Tview.setItems(detailslist);
       addpievalue(catg);
      
  }

	
		

   public static void display(ObservableList<Details> tablelist,ObservableList<PieChart.Data> pielist)throws SQLException {
    	   String query ="select * from Details";
	       con = DBconnection();
           st = con.createStatement();
           rs=st.executeQuery(query);
           while(rs.next()){
           Details dt=new Details();
    	   dt.income=rs.getLong(2);
    	   dt.incomereason=rs.getString(3);
    	   dt.amountspent=rs.getLong(4);
    	   dt.expensereason=rs.getString(5);
    	   dt.category=rs.getString(6);
    	   dt.date=rs.getDate(7); 
    	   dt.Tincome=rs.getLong(8);
    	   dt.Texpense=rs.getLong(9);
    	   dt.id=rs.getLong(1);
    	   tablelist.add(dt);
  }
     if(tablelist.size()>0)Details.objid=tablelist.get(tablelist.size()-1).id;
        query ="select * from piechart";
        rs=st.executeQuery(query); 
        if(rs.next()) {     
        for(int i=0,j=1;i<pielist.size();i++) {
        	pielist.get(i).setPieValue(rs.getDouble(j));
        	j++;
        }
        ent_count=rs.getDouble(1);
        bus_count=rs.getDouble(2);
        sprt_count=rs.getDouble(3);
        fit_count=rs.getDouble(4);
        outing_count=rs.getDouble(5); 
        others_count=rs.getDouble(6);
        }
        pielist.forEach(data -> data.setName(data.getName()+" "+data.getPieValue()+"% "));  
        if(tablelist.size()>0){
        Totalincome=tablelist.get(tablelist.size()-1).Tincome;
        Totalexpense=tablelist.get(tablelist.size()-1).Texpense;
        }
}

 
 	public static void cleardata() throws SQLException {
    	String query1="SET SQL_SAFE_UPDATES = 0;";
		String query="delete from Details";
		con = JDBCconct.DBconnection();
	    st = con.createStatement();
        st.executeUpdate(query1);	
        st.executeUpdate(query);
        st.executeUpdate("alter table Details auto_increment = 0;");
        st.executeUpdate("delete from piechart;");	
        st.executeUpdate("insert into piechart  values(0,0,0,0,0,0);");
        ent_count=0;
        bus_count=0;
        sprt_count=0;
        fit_count=0;
        outing_count=0; 
        others_count=0;
        Details.objid=0;
        JDBCconct.Totalexpense=0;
        JDBCconct.Totalincome=0;
    }

	public static void Deletedata(Details selectedItem, TableView<Details> tview, ObservableList<Details> detailslist, ObservableList<Data> list) throws SQLException {
	String query ="delete from Details where id =?";
	con = DBconnection();
	ps=con.prepareStatement(query);
	ps.setLong(1, selectedItem.id);
	ps.executeUpdate();
	String temp=selectedItem.getCategory();
	if(temp!=null) updatepiechart(list,temp);
    tview.getItems().remove(selectedItem);
    
    Totalincome =0;
	Totalexpense =0;
	query="SET SQL_SAFE_UPDATES=0;";
	st=con.createStatement();
	st.executeUpdate(query);
	
	rs=st.executeQuery("select income,amtspend,totalincome,totalexpense from Details;");
	while(rs.next()) {
		Totalincome +=rs.getLong(1);
		Totalexpense +=rs.getLong(2);
        ps=con.prepareStatement("update Details set totalincome =?,totalexpense=? where totalincome =? and totalexpense=?");
		ps.setLong(1,Totalincome);
		ps.setLong(2,Totalexpense);
		ps.setLong(3,rs.getLong(3));
		ps.setLong(4,rs.getLong(4));
		ps.executeUpdate();     	
	}
	
	 rs=st.executeQuery("select totalincome,totalexpense from Details;");
	
	     int i=0;
	 	while(rs.next()){
			detailslist.get(i).Tincome=rs.getLong(1);
    		detailslist.get(i).Texpense=rs.getLong(2);
			i++;
		}
		
	}

	
	public static void updatepiechart(ObservableList<PieChart.Data> list,String catg) throws SQLException {
		  String qurey="update piechart set "+catg+"=? where "+catg+"=?";
		  con=DBconnection();
		  ps=con.prepareStatement(qurey);
		    	
	    	if(catg.equals("Entertainment")) {
	     	 ent_count--;
	     	 ps.setDouble(1,ent_count);
	     	 ps.setDouble(2,ent_count+1);
	  }
	      
	      
	      else if(catg.equals("bussiness")) {
	     	 bus_count--;
	     	 ps.setDouble(1,bus_count);
	     	 ps.setDouble(2,bus_count+1);
	      }
	      
	      else if(catg.equals("sports")) {
	     	 sprt_count--;
	     	 ps.setDouble(1,sprt_count);
	     	 ps.setDouble(2,sprt_count+1);
	          
	      }
	      
	      else if(catg.equals("fitness")) {
	     	 fit_count--;
	     	 ps.setDouble(1,fit_count);
	     	 ps.setDouble(2,fit_count+1);
	      }
	      else if(catg.equals("Outing")) {
	     	 outing_count--;
	     	 ps.setDouble(1,outing_count);
	     	 ps.setDouble(2,outing_count+1);
	      }
	      else {
	     	 others_count--;
	     	 ps.setDouble(1,others_count);
	     	 ps.setDouble(2,others_count+1);
	      }
	      ps.executeUpdate();
	    Scene2Controller.updatepiechart(list, catg);
	}



  public static void updatedata( Details SelectedItem,long income, String incomereason, long expense, 
      String expensereason, String Category,ObservableList<Details> tablelist, TableView<Details> tview, ObservableList<Data> pielist) throws SQLException {
	  
	  if(SelectedItem.category!=null || Category!=null)updatepie(SelectedItem,pielist,Category);
	  String  query="SET SQL_SAFE_UPDATES=0;";
	  st=con.createStatement();
	  st.executeUpdate(query);
	  
	  query="update Details set income=?,incomereason=?,amtspend=?,expensereason=?,category=? where id ="+SelectedItem.id+";";	
      ps=con.prepareStatement(query);
      ps.setLong(1, income);
	  ps.setString(2, incomereason);
	  ps.setLong(3, expense);      
      ps.setString(4, expensereason);
      ps.setString(5, Category);
      ps.executeUpdate();

      Totalincome =0;
  	 Totalexpense =0;
  	
  	rs=st.executeQuery("select income,amtspend,totalincome,totalexpense from Details;");
  	while(rs.next()) {
  		Totalincome +=rs.getLong(1);
  		Totalexpense +=rs.getLong(2);
          ps=con.prepareStatement("update Details set totalincome =?,totalexpense=? where totalincome =? and totalexpense=?;");
  		ps.setLong(1,Totalincome);
  		ps.setLong(2,Totalexpense);
  		ps.setLong(3,rs.getLong(3));
  		ps.setLong(4,rs.getLong(4));
  		ps.executeUpdate();     	
  	}
 
  	
  	SelectedItem.setIncome(income);
  	SelectedItem.setIncomereason(incomereason);
  	SelectedItem.setAmountspent(expense);
  	SelectedItem.setExpensereason(expensereason);
  	SelectedItem.setCategory(Category);
  	
  	
  	 rs=st.executeQuery("select totalincome,totalexpense from Details;");
  	    int i=0;
  	 	while(rs.next()){
  	 		tablelist.get(i).setTincome(rs.getLong(1));
  	 		tablelist.get(i).setTexpense(rs.getLong(2));;
  	        i++;
  	 	}
  		
     tview.refresh();
  
  }

	public static void updatepie(Details selectedItem, ObservableList<Data> pielist, String category) throws SQLException{
    if(selectedItem.category !=null && category ==null ) updatepiechart(pielist, selectedItem.category);
	else if(selectedItem.category ==null &&category !=null ) {
		 addpievalue(category);
		 Scene2Controller.updatepiechart(pielist,category);	
	}
	
	else {
       	if(!selectedItem.category.equals(category)) {
       		 updatepiechart(pielist, selectedItem.category);
     		 addpievalue(category);
    		 Scene2Controller.updatepiechart(pielist,category);	
       	}
		
	}
	}
	
	
	  public static void addpievalue(String catg) throws SQLException {
		 String qurey="update piechart set "+catg+"=? where "+catg+"=?";
	        ps=con.prepareStatement(qurey);
	       if(catg!=null) {  
	         if(catg.equals("Entertainment")) {
	        	 ent_count++;
	        	 ps.setDouble(1,ent_count);
	        	 ps.setDouble(2,ent_count-1);
	     }
	         
	         
	         else if(catg.equals("bussiness")) {
	        	 bus_count++;
	        	 ps.setDouble(1,bus_count);
	        	 ps.setDouble(2,bus_count-1);
	         }
	         
	         else if(catg.equals("sports")) {
	        	 sprt_count++;
	        	 ps.setDouble(1,sprt_count);
	        	 ps.setDouble(2,sprt_count-1);
	             
	         }
	         
	         else if(catg.equals("fitness")) {
	        	 fit_count++;
	        	 ps.setDouble(1,fit_count);
	        	 ps.setDouble(2,fit_count-1);
	         }
	         else if(catg.equals("Outing")) {
	        	 outing_count++;
	        	 ps.setDouble(1,outing_count);
	        	 ps.setDouble(2,outing_count-1);
	         }
	         else {
	        	 others_count++;
	        	 ps.setDouble(1,others_count);
	        	 ps.setDouble(2,others_count-1);
	         }
	         ps.executeUpdate();
	       }
	  }
	  
	
	
	
}


