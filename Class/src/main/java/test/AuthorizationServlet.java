package test;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeServlet;
import com.google.api.client.http.GenericUrl;

/**
 * @author REX-012
 *
 */
public class AuthorizationServlet extends AbstractAuthorizationCodeServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public AuthorizationServlet() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.api.client.extensions.servlet.auth.oauth2.
	 * AbstractAuthorizationCodeServlet#initializeFlow()
	 */
	@Override
	protected AuthorizationCodeFlow initializeFlow() throws ServletException, IOException {
		// TODO Auto-generated method stub
		return InitializeFlowTool.initializeFlow();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.api.client.extensions.servlet.auth.oauth2.
	 * AbstractAuthorizationCodeServlet#getRedirectUri(javax.servlet.http.
	 * HttpServletRequest)
	 */
	@Override
	protected String getRedirectUri(HttpServletRequest req) throws ServletException, IOException {
		// TODO Auto-generated method stub
		return InitializeFlowTool.getREDIRECT_URI();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.api.client.extensions.servlet.auth.oauth2.
	 * AbstractAuthorizationCodeServlet#getUserId(javax.servlet.http.
	 * HttpServletRequest)
	 */
	
	 /**
	   * Get the current user's ID from the session
	   *
	   * @return string user id or null if no one is logged in
	   */
	  public  String getUserId(HttpServletRequest request) {
	    HttpSession session = request.getSession();
	    System.out.println("PRITING USEEEER ID HEEEEEEEE " +(String) session.getAttribute("userId"));
	    return (String) session.getAttribute("userId");
	  }
	  
	  public static String getId(HttpServletRequest request) {
		    HttpSession session = request.getSession();
		    System.out.println("PRITING USEEEER ID HEEEEEEEE " +(String) session.getAttribute("userId"));
		    return (String) session.getAttribute("userId");
		  }
	  public static void setUserId(HttpServletRequest request, String userId) {
	    HttpSession session = request.getSession();
	    session.setAttribute("userId", userId);
	  }

	/*  public static void clearUserId(HttpServletRequest request) throws IOException {
	    // Delete the credential in the credential store
	    String userId = getUserId(request);
	    store.delete(userId, getCredential(userId));

	    // Remove their ID from the local session
	    request.getSession().removeAttribute("userId");
	  }*/
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Credential credential = InitializeFlowTool.getValidCredential(this.getUserId(req));
		if (credential != null) {
			
			AuthorizationCodeFlow flow = InitializeFlowTool.initializeFlow();
			
			  GenericUrl url =
				        flow.newAuthorizationUrl().setRedirectUri(InitializeFlowTool.getREDIRECT_URI());
				    resp.sendRedirect(url.build());
			
		//	this.service(req, resp);
			
			
		}
		else {
			System.out.println("CREDEnTIAL not null, so yeah");
		}
System.out.println("Get of AuthorizationServlet ended");

	}
}
