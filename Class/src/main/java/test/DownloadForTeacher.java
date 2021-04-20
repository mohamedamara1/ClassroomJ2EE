package test;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.classroom.Classroom;
import com.google.api.services.classroom.model.Attachment;
import com.google.api.services.classroom.model.DriveFile;
import com.google.api.services.classroom.model.ListStudentSubmissionsResponse;
import com.google.api.services.classroom.model.StudentSubmission;
import com.google.api.services.drive.Drive;

/**
 * Servlet implementation class DownloadForTeacher
 */
public class DownloadForTeacher extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DownloadForTeacher() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
//		super.doPost(req, resp);
		System.out.println("INSIDE POST TEACHER");
		
		Credential credential = InitializeFlowTool.getValidCredential(AuthorizationServlet.getId(req));

		
        Classroom classroom_service = new Classroom.Builder(new NetHttpTransport(), new GsonFactory(), credential)
                .setApplicationName("testing haha")
                .build();
        
	    Drive drive_service = new Drive.Builder(new NetHttpTransport(), new GsonFactory(), credential)
	                 .setApplicationName("testing haha")
	                 .build();
	    
  /*      String[] selected_courses = req.getParameterValues("matieres");
        
        for(String s : selected_courses) {
            System.out.println("Name : " + s);
        }*/
	//	in this part we extract a list of strings , strings of classroom_id 
        
        
		
     //   List<String> courses_to_download =  Arrays.asList(selected_courses);
	    
        try {
        	System.out.println("Downloading compte rendu..");
			download_compte_rendu("173288968576","184819152798", drive_service, classroom_service);
			System.out.println("Finished download compte rendeu");
			
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IOException
				| GeneralSecurityException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

        
        
        //download courses on server finished here, now we prepare the ZIP file
        
        int randomNum = ThreadLocalRandom.current().nextInt(1,  1000000);
        List<String> classrooms = new ArrayList<String>();

        
	/*	for (String course : courses_to_download) {
			
			try {
			String	classroom_name = DbUtil.get_classroom_name(course).replaceAll(" ", "");
				System.out.println("CLASSROOPM NAME = "+classroom_name);
				classrooms.add("/home/med/eclipse-workspace/Class/src/main/resources/classrooms/"+classroom_name);

			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}*/
        String course_name = classroom_service.courses().get("173288968576").execute().getName();
        String work_name = classroom_service.courses().courseWork().get("173288968576", "184819152798").execute().getTitle();

		
		classrooms.add("/home/med/eclipse-workspace/Class/src/main/resources/classrooms/"+course_name.replaceAll(" ", ""));
		
	    String[] myFiles = new String[ classrooms.size() ];
	    classrooms.toArray( myFiles );
	    
	    String zipname= Integer.toString(randomNum)+".zip";
        String zipFile= "/home/med/eclipse-workspace/Class/src/main/resources/classrooms/"+zipname;
	    
        
	    ZipUtilityTeacher.zip(myFiles, zipFile);
	    
		//req.setAttribute("path", zipFile);
	//    req.setAttribute("name", Integer.toString(randomNum));
	    
	    
	    req.getSession().setAttribute("path", zipFile);
	    req.getSession().setAttribute("name", zipname);
	    doGet(req,resp);
	}
	
    public void  download_compte_rendu(String course_id, String work_id, Drive drive_service, Classroom classroom_service) throws IOException, GeneralSecurityException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{

        String course_name = classroom_service.courses().get(course_id).execute().getName();
        String work_name = classroom_service.courses().courseWork().get(course_id, work_id).execute().getTitle();



        ListStudentSubmissionsResponse submissions_response = classroom_service.courses().courseWork().studentSubmissions().list(course_id, work_id).execute();
        List<StudentSubmission> submissions = submissions_response.getStudentSubmissions();  

        for (StudentSubmission submission : submissions){
            String student_name = classroom_service.courses().students().get(course_id, submission.getUserId()).execute().getProfile().getName().getFullName();

      //      File student_folder = new File("./ClassroomCompteRendu/"+course_name+"/"+work_name+"/"+student_name);
         File   student_folder =new File("/home/med/eclipse-workspace/Class/src/main/resources/classrooms/"+course_name.replaceAll(" ", "")+"/Teacher/"+work_name+"/"+student_name);
         System.out.println(student_folder.getPath());
            if (! student_folder.exists()) {
                student_folder.mkdirs(); 
                System.out.println("Creadet folder!!!"+student_folder.getPath());
            	
            }

            List<Attachment> attachments = submission.getAssignmentSubmission().getAttachments();
            for (Attachment attachment : attachments){
                DriveFile drive_file = attachment.getDriveFile();
                String file_id = drive_file.getId();
                String file_name = drive_file.getTitle();
                
                //(String file_id, String file_name, String path, String course_id, Drive drive_service)
                DownloadFileServlet.file_download(file_id, file_name, student_folder.getPath(),course_id, drive_service, true);
            }

        }
    }

}
