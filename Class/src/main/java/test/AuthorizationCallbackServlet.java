/**
 * 
 */
package test;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeCallbackServlet;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.classroom.Classroom;
import com.google.api.services.classroom.model.Course;
import com.google.api.services.classroom.model.ListCoursesResponse;
import com.google.api.services.drive.Drive;

/**
 * @author REX-012
 *
 */
public class AuthorizationCallbackServlet extends AbstractAuthorizationCodeCallbackServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public AuthorizationCallbackServlet() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeCallbackServlet#initializeFlow()
	 */
	@Override
	protected AuthorizationCodeFlow initializeFlow() throws ServletException, IOException {
		// TODO Auto-generated method stub
		return InitializeFlowTool.initializeFlow();
	}

	/* (non-Javadoc)
	 * @see com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeCallbackServlet#getRedirectUri(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected String getRedirectUri(HttpServletRequest req) throws ServletException, IOException {
		// TODO Auto-generated method stub
		return InitializeFlowTool.getREDIRECT_URI();
	}

	/* (non-Javadoc)
	 * @see com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeCallbackServlet#getUserId(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected String getUserId(HttpServletRequest req) throws ServletException, IOException {
		// TODO Auto-generated method stub
		return AuthorizationServlet.getId(req);
	}

	  /**
	   * Handles a successfully granted authorization.
	   *
	   * <p>
	   * Default implementation is to do nothing, but subclasses should override and implement. Sample
	   * implementation:
	   * </p>
	   *
	   * <pre>
	      resp.sendRedirect("/granted");
	   * </pre>
	   *
	   * @param req HTTP servlet request
	   * @param resp HTTP servlet response
	   * @param credential credential
	   * @throws ServletException HTTP servlet exception
	   * @throws IOException some I/O exception
	   */
	  protected void onSuccess(HttpServletRequest req, HttpServletResponse resp, Credential credential)
	      throws ServletException, IOException {
		  resp.getWriter().append("!!!Credentials are already obtained!!!");
		  System.out.println("Congratulations!!!");
			resp.sendRedirect("/Class/Dashboard.jsp");
			
	        Classroom service = new Classroom.Builder(new NetHttpTransport(), new GsonFactory(), credential)
	                .setApplicationName("testing haha")
	                .build();
	        
		    Drive drive_service = new Drive.Builder(new NetHttpTransport(), new GsonFactory(), credential)
		                 .setApplicationName("testing haha")
		                 .build();
          ListCoursesResponse reponse_list = service.courses().list().execute();
          List<Course> courses = reponse_list.getCourses();
          
          for (Course course : courses){
          	System.out.println(course);
          } 
   		req.setAttribute("courses", courses);
  	    req.getRequestDispatcher("Dashboard.jsp").forward(req, resp);
  	    
	 	Oauth2callback.update_db(service, courses, drive_service);

    System.out.println("CALLED UPDATE FUNCTION");

	  }

	  /**
	   * Handles an error to the authorization, such as when an end user denies authorization.
	   *
	   * <p>
	   * Default implementation is to do nothing, but subclasses should override and implement. Sample
	   * implementation:
	   * </p>
	   *
	   * <pre>
	      resp.sendRedirect("/denied");
	   * </pre>
	   *
	   * @param req HTTP servlet request
	   * @param resp HTTP servlet response
	   * @param errorResponse error response ({@link AuthorizationCodeResponseUrl#getError()} is not
	   *        {@code null})
	   * @throws ServletException HTTP servlet exception
	   * @throws IOException some I/O exception
	   */
	  protected void onError(
	      HttpServletRequest req, HttpServletResponse resp, AuthorizationCodeResponseUrl errorResponse)
	      throws ServletException, IOException {
	  }
}
