package test;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.classroom.Classroom;
import com.google.api.services.classroom.model.Course;
import com.google.api.services.classroom.model.ListCoursesResponse;

/**
 * Servlet implementation class Welcome
 */
public class Welcome extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Welcome() {
        super();
        // TODO Auto-generated constructor stub
    }

	private String getUserId(HttpServletRequest req) throws ServletException, IOException {
		// TODO Auto-generated method stub
		return "test";
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			System.out.println("request credentials starting!!!");
			Credential credential = InitializeFlowTool.getValidCredential(this.getUserId(request));
			if (credential != null) {
				response.setHeader("Content-type", "text/html;charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().append("!!!Credentials are already obtained!!!<br/>");
			
		        Classroom service = new Classroom.Builder(new NetHttpTransport(), new GsonFactory(), credential)
		                .setApplicationName("testing haha")
		                .build();
		        
	          ListCoursesResponse reponse_list = service.courses().list().execute();
	          List<Course> courses = reponse_list.getCourses();
	          
	          for (Course course : courses){
	          	System.out.println(course);
	          } 
				response.sendRedirect("/Class/Dashboard.jsp");

			} else {
				System.out.println("creds = null, redirecting to Authorization Servlet");
				response.sendRedirect("/Class/AuthorizationServlet");
			}
			System.out.println("request credentials end!!!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
