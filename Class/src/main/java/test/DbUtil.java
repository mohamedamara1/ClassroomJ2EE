package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


//import org.apache.derby.iapi.jdbc.ClientDriver;
 
public class DbUtil {
//	public static final String db_path = "/home/med/eclipse-workspace/Class/src/main/resources";
//	public String dbUrl = "jdbc:derby://home/med/eclipse-workspace/Class/src/main/resources/demo;create=true";
	public static Connection conn;
	
	  public static  void connectionToMysql() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		  System.out.println("Establishing connection to DB");
		    // -------------------------------------------
		    // URL format is
		    // jdbc:derby:<local directory to save data>
		    // -------------------------------------------
		//  DriverManager.registerDriver(new ClientAutoloadedDriver());

		//	String dbUrl = "jdbc:derby://localhost:1527/testinga;create=true";
	  	  //  conn = DriverManager.getConnection(dbUrl);
		  Class.forName("com.mysql.cj.jdbc.Driver");

	  	   conn=DriverManager.getConnection(  
	  			"jdbc:mysql://localhost:3306/classroomtest","root","");  
	  	    System.out.println("Connexion = "+conn);
	  	    
	  	    
		  }
	  public static void normalDbUsage() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		  System.out.println("2 - Connexion = "+conn);
		    Statement stmt = conn.createStatement();
		 

		    // select *
		    ResultSet rs=stmt.executeQuery("SELECT * FROM classroomtest.classroom;");  
		    while(rs.next())  
		    System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));  
		    conn.close();  
		  }
	public static List<String> get_existing_courses() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		connectionToMysql();
		Statement stmt = conn.createStatement();
		
		List<String> existing_courses = new ArrayList<>();

	    ResultSet rs=stmt.executeQuery("SELECT classroom_id FROM classroomtest.classroom;");  

	    while(rs.next()) {
	    	String course_id = Long.toString(rs.getLong(1));
	    	existing_courses.add(  course_id);
	    	System.out.println("Existing course : "+course_id) ;
	    }
	    conn.close();
	    	
	//	return existing_courses;
		return existing_courses;
	}
	public static void insert_classroom(String id, String name) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
//		long classroom_id = Long.parseLong(id);
		String query = " insert into classroomtest.classroom (classroom_id, classroom_name)"
		        + " values (?, ?)";
		connectionToMysql();

	    PreparedStatement preparedStmt = conn.prepareStatement(query);
	      preparedStmt.setString (1, id);
	      preparedStmt.setString (2, name);
	      
	      System.out.println(preparedStmt);
	      
	      preparedStmt.execute();
	      System.out.println("inserted into classroom table " + id + " "+name);
	      conn.close();



	}
		
}
