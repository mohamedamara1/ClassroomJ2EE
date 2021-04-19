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
import com.google.api.services.classroom.model.Announcement;
import com.google.api.services.classroom.model.Course;
import com.google.api.services.classroom.model.CourseWork;
import com.google.api.services.classroom.model.ListAnnouncementsResponse;
import com.google.api.services.classroom.model.ListCourseWorkResponse;
import com.google.api.services.classroom.model.ListCoursesResponse;
import com.google.api.services.classroom.model.Material;
import com.google.api.services.classroom.model.UserProfile;
import com.google.api.services.drive.Drive;



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
	      

	      Drive drive_service = new Drive.Builder(new NetHttpTransport(), new GsonFactory(), credential)
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
  	      

	  	     
	  	     //UPDATE FUNCTION THAT NEEDS A FUNCTION THAT TELLS HER WHAT ARE THE EXISTING FILES ON GOOGLE SERVERS
  	   
  	      
  	      	update_db(service, courses, drive_service);
        System.out.println("CALLED UPDATE FUNCTION");

       
	      return;
	}
	
	
	
	
	


	
	protected static void update_db(Classroom service, List<Course> courses, Drive drive_service) throws IOException {
		
		   List<String> existing_courses = Arrays.asList();
			try {
				existing_courses = DbUtil.get_existing_courses();
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
 	     for (Course course : courses) {
  	    	 
         	if ( existing_courses.contains(course.getId()) ) {
         		System.out.println("*******UPdating "+course.getName());
   
        		
        	
         	
	  	        ListAnnouncementsResponse announcements_reponse = service.courses().announcements().list(course.getId()).execute();
	  	        List<Announcement> announcements = announcements_reponse.getAnnouncements();
  			String cours_path = "/home/med/eclipse-workspace/Class/src/main/resources/classrooms/"+course.getName().replaceAll(" ", "")+"/Cours";

  			
	  	        
	  	        ListCourseWorkResponse works_response = service.courses().courseWork().list(course.getId()).execute();
	  	        List<CourseWork> works = works_response.getCourseWork(); 
  			String work_path = "/home/med/eclipse-workspace/Class/src/main/resources/classrooms/"+course.getName().replaceAll(" ", "")+"/TD+TP";

  			
	  	        try {
					for (Announcement annonc : announcements) {

					     List<Material> materials = annonc.getMaterials();
					     
						 boolean skip = false;


					     try{
					    	 
					             for (Material material : materials){
					                     String file_id = material.getDriveFile().getDriveFile().getId();
					                     String file_name = material.getDriveFile().getDriveFile().getTitle();
					                     if (DownloadFileServlet.verif(file_name)) {
					                    	 
					     	  	        	try {
					    						if (! DbUtil.check_file(file_id, course.getId())) {
					    							
					    					//		DownloadFileServlet.file_download(annoc.getId)
					                             	System.out.println("Downloading : "+file_name);
					                                 try{
					                                	 
					            							DownloadFileServlet.file_download(file_id, file_name, cours_path, course.getId(), drive_service);
					                                    //   downloads.add(file_name);
					                                 }
					                                 catch(IOException e) {
					                                 e.printStackTrace();
					                                 }
					    						}
					    						else {
					    							System.out.println("File exists on DB, skipping to next classroom");
					    							skip=true;
					    						}
					    					} catch (InstantiationException | IllegalAccessException | ClassNotFoundException
					    							| SQLException e) {
					    						// TODO Auto-generated catch block
					    						e.printStackTrace();
					    					}

					             		}
					                     else {
					                     	System.out.println("Extension not supported " + file_name);
					                     }
					             }
					     }
					     catch(NullPointerException e){
					     }
					     if (skip) {
					    	 break;
					     }
						
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("No announcements found for this classroom!");
					e.printStackTrace();
				}
	  	    	 //COURSE WORK HERE
	  	        
	  	     }		
 	     }
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	
	
	public static boolean isTeacher(Classroom service) throws IOException {
		UserProfile user = service.userProfiles().get("me").execute();
		System.out.println(user);
		return user.getVerifiedTeacher();
	}
	
	public static String getUserid(Classroom service) throws IOException {
		UserProfile user = service.userProfiles().get("me").execute();

		return user.getId();
		
	}
	
}
