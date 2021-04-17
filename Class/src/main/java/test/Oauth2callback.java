package test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.classroom.Classroom;
import com.google.api.services.classroom.model.Course;
import com.google.api.services.classroom.model.ListCoursesResponse;
import com.google.api.services.classroom.model.UserProfile;



/**
 * Servlet implementation class Oauth2callback
 */
public class Oauth2callback extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Oauth2callback() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		/*VerificationCodeReceiver receiver = new LocalServerReceiver();
		String code = receiver.waitForCode();*/
		String code = request.getParameter("code");
	    System.out.println("Got a code. Attempting to exchange for access token.");
		System.out.println("########: " + code);
		AuthorizationCodeFlow flow = InitializeFlowTool.initializeFlow();
		
	/*	GoogleTokenResponse resp =
				 new GoogleAuthorizationCodeTokenRequest(new NetHttpTransport(), new GsonFactory(),
				 "404366420582-i7m41p0cv9vgijdbu7rmv4sog8d3r61h.apps.googleusercontent.com", "Ml0d4hgQ2_Nw8vPALO_ErsDM",
				 code, InitializeFlowTool.getREDIRECT_URI())
				 .execute();
		*/
	
		TokenResponse tokenResponse =
		          flow.newTokenRequest(code)
		              .setRedirectUri(InitializeFlowTool.getREDIRECT_URI()).execute();
        System.out.println("Access token: " + tokenResponse.getAccessToken());

	//	String userId = "123456";
	    // Extract the Google User ID from the ID token in the auth response
  //    System.out.println("Code exchange worked. User " + userId + " logged in.");

	      // Set it into the session
	//      AuthorizationServlet.setUserId(request, userId);
	//      flow.createAndStoreCredential(tokenResponse, userId);
	      
	      System.out.println("token saved and user id saved");
	      
	      
	  //    Credential cred = new Credential(BearerToken.authorizationHeaderAccessMethod())
	//    		  .setFromTokenResponse( tokenResponse);
	      
	      Credential credential = new Credential.Builder(BearerToken.authorizationHeaderAccessMethod()).setTransport(
	    		  new NetHttpTransport())
	    		  .setJsonFactory(new GsonFactory())
	    		  .setTokenServerEncodedUrl(
	    		  InitializeFlowTool.getREDIRECT_URI())
	    		  .setClientAuthentication(new BasicAuthentication("404366420582-i7m41p0cv9vgijdbu7rmv4sog8d3r61h.apps.googleusercontent.com", "Ml0d4hgQ2_Nw8vPALO_ErsDM"))
	    		  .build()
	    		  .setFromTokenResponse(tokenResponse);
	     System.out.println(credential);
	      
	 //     System.out.println("CRED expires in: "+credential.getExpiresInSeconds());
	        
	      
	      Classroom service = new Classroom.Builder(new NetHttpTransport(), new GsonFactory(), credential)
	                .setApplicationName("testing haha")
	                .build();
	      
	   //    System.out.println("SERVICE : " + service);
	       
	//        System.out.println("AUTHENTICATED USER IS A TEACHER : " +isTeacher(service));
	      
	      
	      String userId = getUserid(service);
	      System.out.println("USER ID :"+userId);
	      AuthorizationServlet.setUserId(request, userId);
	      flow.createAndStoreCredential(tokenResponse, userId);
	      
	        
	        
          ListCoursesResponse reponse_list = service.courses().list().execute();
          List<Course> courses = reponse_list.getCourses();
          
    		request.setAttribute("courses", courses);
  	      request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
    /*      
        //  List<String> user_courses =  Arrays.asList();
          
          List<String> user_courses = new LinkedList<String>();

          
          for (Course course : courses) {
        	  user_courses.add(course.getId());
          }
          
          List<String> existing_courses = Arrays.asList("234265883466");
  		try {
  			existing_courses = DbUtil.get_existing_courses();
  			System.out.println("existing courses inside try = " + existing_courses);
  		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e1) {
  			// TODO Auto-generated catch block
  			e1.printStackTrace();
  		}
        */


         /* for( String course : user_courses) {
          	if (! existing_courses.contains(course) ) {
          		DownloadFileServlet.initial_download_course(course);
          		
          	}
          }*/

       
	  //    response.sendRedirect("http://localhost:8080/Class/Dashboard.jsp");

	      return;
	}
	
	/*
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	static boolean isTeacher(Classroom service) throws IOException {
		UserProfile user = service.userProfiles().get("me").execute();
		System.out.println(user);
		return user.getVerifiedTeacher();
	}
	
	static String getUserid(Classroom service) throws IOException {
		UserProfile user = service.userProfiles().get("me").execute();

		return user.getId();
		
	}
	

}
