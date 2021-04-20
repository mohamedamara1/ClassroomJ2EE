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
	    System.out.println("SSSSSSSS"+rs);
	    while(rs.next()) {
	    	String course_id = Long.toString(rs.getLong(1));
	    	existing_courses.add(  course_id);
	    	System.out.println("Existing course : "+course_id) ;
	    }
	    conn.close();
	    	
	//	return existing_courses;
		return existing_courses;
	}
	
	public static String get_classroom_name(String classroom_id) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		connectionToMysql();
		Statement stmt = conn.createStatement();
		 String classroom_name="";
		

	    String query = ("SELECT classroom_name FROM classroomtest.classroom where classroom_id = ?");  

	    PreparedStatement s = conn.prepareStatement(query);
	    
	    s.setString(1, classroom_id);

	    ResultSet rs = s.executeQuery();
	   // ResultSet rs=stmt.executeQuery("SELECT file_id FROM classroomtest.files where file_id='{}'"));  
	    while (rs.next()) {
		     classroom_name = rs.getString("classroom_name");
	
	    }
	    conn.close();
	    	
	    
	//	return existing_courses;
		return classroom_name;
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
	public static void insert_file(String file_id, String file_name, String classroom_id) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
//		long classroom_id = Long.parseLong(id);
		String query1 = " insert into classroomtest.files (file_id, file_name) values (?, ?)";
		
		String query2 = " insert into classroomtest.classroom_files (file_id, classroom_id) values (?, ?)";

		connectionToMysql();

		PreparedStatement preparedStmt1 = conn.prepareStatement(query1);
	    preparedStmt1.setString (1, file_id);
	    preparedStmt1.setString (2, file_name);
	      
		PreparedStatement preparedStmt2 = conn.prepareStatement(query2);
		
	    preparedStmt2.setString (2, classroom_id);
	    preparedStmt2.setString (1, file_id);

	      	      
	      
	      System.out.println(preparedStmt1);
	      System.out.println(preparedStmt2);
	      try {
		      preparedStmt1.execute();
		      preparedStmt2.execute();  	  
	      }
	      catch(java.sql.SQLIntegrityConstraintViolationException e ) {
	    	  System.out.println(e);
	    	  
	      }


	      System.out.println("inserted into file table " + file_id+ " "+file_name);
	      System.out.println("inserted into classroom_file table " + classroom_id+ " "+file_id);

	      conn.close();
	}
	public static boolean check_file(String file_id, String classroom_id) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		connectionToMysql();
		Statement stmt = conn.createStatement();
		String query = "SELECT file_id FROM classroomtest.classroom_files where file_id=? and classroom_id=?";
	    PreparedStatement s = conn.prepareStatement(query);
	    
	    s.setString(1, file_id);
	    s.setString(2, classroom_id);

	    ResultSet rs = s.executeQuery();
	   // ResultSet rs=stmt.executeQuery("SELECT file_id FROM classroomtest.files where file_id='{}'"));  
	    
	    boolean notempty = rs.next();
	    
	    conn.close();

	    return notempty;
		
	}
		
}
