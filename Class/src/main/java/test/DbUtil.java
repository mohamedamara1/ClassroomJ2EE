package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.derby.client.ClientAutoloadedDriver;

//import org.apache.derby.iapi.jdbc.ClientDriver;
 
public class DbUtil {
//	public static final String db_path = "/home/med/eclipse-workspace/Class/src/main/resources";
//	public String dbUrl = "jdbc:derby://home/med/eclipse-workspace/Class/src/main/resources/demo;create=true";
	public static Connection conn;
	
	  public static void connectionToDerby() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		  System.out.println("Establishing connection to DB");
		    // -------------------------------------------
		    // URL format is
		    // jdbc:derby:<local directory to save data>
		    // -------------------------------------------
		//  DriverManager.registerDriver(new ClientAutoloadedDriver());

			String dbUrl = "jdbc:derby://localhost:1527/testinga;create=true";
	  	    conn = DriverManager.getConnection(dbUrl);
	  	    System.out.println("Connexion = "+conn);
		  }
	  public static void normalDbUsage() throws SQLException {
		    Statement stmt = conn.createStatement();
		 
		    // drop table
		    // stmt.executeUpdate("Drop Table users");
		 
		    // create table
		    stmt.executeUpdate("Create table users (id int primary key, name varchar(50))");
		 
		    // insert 2 rows
		    stmt.executeUpdate("insert into users values (1,'tom')");
		    stmt.executeUpdate("insert into users values (2,'peter')");
		 
		    // query
		    ResultSet rs = stmt.executeQuery("SELECT * FROM users");
		 
		    // print out query result
		    while (rs.next()) { 
		      System.out.printf("%d\t%s\n", rs.getInt("id"), rs.getString("name"));
		    }
		  }
		
}
