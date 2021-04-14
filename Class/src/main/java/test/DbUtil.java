package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


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
	  			"jdbc:mysql://localhost:3306/testingdb","root","");  
	  	    System.out.println("Connexion = "+conn);
	  	    
	  	    
		  }
	  public static void normalDbUsage() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		  System.out.println("2 - Connexion = "+conn);
		    Statement stmt = conn.createStatement();
		 

		    // select *
		    ResultSet rs=stmt.executeQuery("SELECT * FROM testingdb.students;");  
		    while(rs.next())  
		    System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));  
		    conn.close();  
		  }
		
}
