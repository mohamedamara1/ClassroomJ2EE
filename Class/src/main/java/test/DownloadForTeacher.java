package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import com.google.api.services.classroom.model.Course;
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
		String zipFullPath = (String) request.getSession().getAttribute("path");
	      String filename = (String) request.getSession().getAttribute("name");

	        System.out.println("Downloading "+filename+".zip");

	        /* Now the zip is saved on zipFullPath */


	        response.setContentType("application/zip");
	        response.setHeader("Content-Disposition", "attachment;filename=\"" + filename + "\"");
	        OutputStream out = response.getOutputStream();

	        FileInputStream fis = new FileInputStream(zipFullPath);
	        int bytes;
	        while ((bytes = fis.read()) != -1) {
	            out.write(bytes);
	        }
	        fis.close();
	        response.flushBuffer();

	        System.out.println(".zip file downloaded at client successfully");
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
	    
        String[] selected_compterendu = req.getParameterValues("compterendu");
        
	    Map <String, String> course_compterendu = new HashMap<String, String>();

        
        for (String composed_string : selected_compterendu) {
        	String[] course_cr_str = composed_string.split(",");
        	
        	course_compterendu.put(course_cr_str[0], course_cr_str[1]);
        }
        
        for (Map.Entry<String, String> entry : course_compterendu.entrySet()) {
            System.out.println(entry.getKey() + "/" + entry.getValue());
            try {
            	System.out.println("Downloading compte rendu..");
            	System.out.println("work id "+entry.getKey()+" course id "+entry.getValue());
            	download_compte_rendu(entry.getKey(),entry.getValue(), drive_service, classroom_service);
    			System.out.println("Finished download compte rendu");
    			   			
    		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IOException
    				| GeneralSecurityException | SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        }
     
        //download courses on server finished here, now we prepare the ZIP file
        
        int randomNum = ThreadLocalRandom.current().nextInt(1,  1000000);
      //  List<String> classrooms = new ArrayList<String>();
        Set<String> classrooms = new HashSet<String>();

        
    		for (String course_id : course_compterendu.values()) {
    			
    			//String	classroom_name = DbUtil.get_classroom_name(course_id).replaceAll(" ", "");
    		        String classroom_name = classroom_service.courses().get(course_id).execute().getName().replaceAll(" ", "");

    				
    				System.out.println("CLASSROOPM NAME = "+classroom_name);
    				classrooms.add(ClassroomFolder.path+classroom_name);
    		        System.out.println("Course id : "+course_id);
	
    				String path = ClassroomFolder.path+classroom_name;
    				
    				File course_folder = new File(path);
    		if (! course_folder.exists()) {
    			
	   				course_folder.mkdir();
					
					(new File(path+"/Student")).mkdir();
					(new File(path+"/Student")).mkdir();		
					
					
					(new File(path+"/Student/Cours")).mkdir();
					(new File(path+"/Student/TD+TP")).mkdir();
					System.out.println("New course folder created : ");	
	    		}
 
    				
    		}
     	    		
	    String[] myFiles = new String[ classrooms.size() ];
	    classrooms.toArray( myFiles );
	    
	    String zipname= Integer.toString(randomNum)+".zip";
        String zipFile= ClassroomFolder.path+zipname;
	    
        
	    ZipUtilityTeacher.zip(myFiles, zipFile);
	    

	    req.getSession().setAttribute("path", zipFile);
	    req.getSession().setAttribute("name", zipname);
	    doGet(req,resp);
	}
	
    public void  download_compte_rendu(String work_id, String course_id, Drive drive_service, Classroom classroom_service) throws IOException, GeneralSecurityException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{

        String course_name = classroom_service.courses().get(course_id).execute().getName();
        String work_name = classroom_service.courses().courseWork().get(course_id, work_id).execute().getTitle();

        ListStudentSubmissionsResponse submissions_response = classroom_service.courses().courseWork().studentSubmissions().list(course_id, work_id).execute();
        List<StudentSubmission> submissions = submissions_response.getStudentSubmissions();  

        System.out.println("COURSE NAME = "+course_name + " WORK NAME = "+ work_name);
        for (StudentSubmission submission : submissions){
            String student_name = classroom_service.courses().students().get(course_id, submission.getUserId()).execute().getProfile().getName().getFullName();
            System.out.println("STUDENT NAME = "+student_name);
         File   student_folder =new File(ClassroomFolder.path+course_name.replaceAll(" ", "")+"/Teacher/"+work_name+"/"+student_name);
         System.out.println(student_folder.getPath());
            if (! student_folder.exists()) {
                student_folder.mkdirs(); 
                System.out.println("Creadted folder!!!"+student_folder.getPath());           	
            }

            List<Attachment> attachments = submission.getAssignmentSubmission().getAttachments();
            try {
				for (Attachment attachment : attachments){
				    DriveFile drive_file = attachment.getDriveFile();
				    String file_id = drive_file.getId();
				    String file_name = drive_file.getTitle();
				    
				    //(String file_id, String file_name, String path, String course_id, Drive drive_service)
				    DownloadFileServlet.file_download(file_id, file_name, student_folder.getPath(),course_id, drive_service, true);
				}
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        }
    }

}
