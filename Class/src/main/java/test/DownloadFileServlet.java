package test;
 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.classroom.Classroom;
import com.google.api.services.classroom.model.Announcement;
import com.google.api.services.classroom.model.Course;
import com.google.api.services.classroom.model.CourseWork;
import com.google.api.services.classroom.model.ListAnnouncementsResponse;
import com.google.api.services.classroom.model.ListCourseWorkResponse;
import com.google.api.services.classroom.model.Material;
import com.google.api.services.drive.Drive;

 
public class DownloadFileServlet extends HttpServlet {
 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        // reads input file from an absolute path
        String filePath = "/home/med/eclipse-workspace/Class/src/main/resources/cours.pdf";
        File downloadFile = new File(filePath);
        FileInputStream inStream = new FileInputStream(downloadFile);
         
        // if you want to use a relative path to context root:
        String relativePath = getServletContext().getRealPath("");
        System.out.println("relativePath = " + relativePath);
         
        // obtains ServletContext
        ServletContext context = getServletContext();
         
        // gets MIME type of the file
        String mimeType = context.getMimeType(filePath);
        if (mimeType == null) {        
            // set to binary type if MIME mapping not found
            mimeType = "application/octet-stream";
        }

        System.out.println("MIME type: " + mimeType);
         
        // modifies response
        response.setContentType(mimeType);
        response.setContentLength((int) downloadFile.length());
         
        // forces download
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
        response.setHeader(headerKey, headerValue);
         
        // obtains response's output stream
        OutputStream outStream = response.getOutputStream();
         
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
         
        while ((bytesRead = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
         
        inStream.close();
        outStream.close();     
    }

	//@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
		System.out.println("INSIDE POST METHOD OF DOWNLOADFILESERVLET");

		
        String[] selected_courses = req.getParameterValues("matieres");
        
        for(String s : selected_courses) {
            System.out.println("Name : " + s);
        }
	//	in this part we extract a list of strings , strings of classroom_id 
        
        
		
        List<String> courses_to_download =  Arrays.asList(selected_courses);
        
        List<String> existing_courses = Arrays.asList();
		try {
			existing_courses = DbUtil.get_existing_courses();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("courses to download "+courses_to_download);
		System.out.println("existing courses  = " + existing_courses);

        for( String course : courses_to_download) {
        	System.out.println(course);
        	if ( !existing_courses.contains(course) ) {
        		initial_download_course(course, req);
        		
        	}
        	else {
        		System.out.println("CLASSROOM ALREADY EXISTS ON SERVER "+course);
        	}
        }
        
        //download courses on server finished here, now we prepare the ZIP file
  
		
        

        

	}

	 void initial_download_course(String course_id, HttpServletRequest req ) throws IOException, ServletException {
		
		// TODO Auto-generated method stub
		Credential credential = InitializeFlowTool.getValidCredential(AuthorizationServlet.getId(req));

        Classroom service = new Classroom.Builder(new NetHttpTransport(), new GsonFactory(), credential)
                .setApplicationName("testing haha")
                .build();
	//      System.out.println("AUTHENTICATED USER IS A TEACHER : " +Oauth2callback.isTeacher(service));

        System.out.println("Course id : "+course_id);
        
		Course course = service.courses().get(course_id).execute();
		
		String path = "/home/med/eclipse-workspace/Class/src/main/resources/classrooms/"+course.getName().replaceAll(" ", "");
		
		File course_folder = new File(path);
		
		course_folder.mkdir();
		
		(new File(path+"/Cours")).mkdir();
		(new File(path+"/TD+TP")).mkdir();		
		System.out.println("New course folder created : "+course.getName().replaceAll(" ", ""));
		

        ListAnnouncementsResponse announcements_reponse = service.courses().announcements().list(course_id).execute();
        List<Announcement> announcements = announcements_reponse.getAnnouncements();
        System.out.println("Downloading announcements of "+course.getName());
        try{
               download_announcements(announcements, course.getName(), credential);
        }
        catch(NullPointerException e){
                System.out.println("This Course does't have any announcements!");
        }
		
		
        ListCourseWorkResponse works_response = service.courses().courseWork().list(course_id).execute();
        List<CourseWork> works = works_response.getCourseWork();
		
        try{
           download_works(works, course.getName(), credential);
	    }
	
	    catch (NullPointerException e){
	            System.out.println("this course doesn't have any work!");
	    }
        
        
//dont forget to add the classroom_id to existing courses on database after downloading
		
        try {
			DbUtil.insert_classroom(course.getId(), course.getName());
			
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
     public void download_announcements(List<Announcement> announcements, String course_name, Credential credential) throws NullPointerException, IOException, ServletException{

 		System.out.println("Downloading course work...");
 		String path = "/home/med/eclipse-workspace/Class/src/main/resources/classrooms/"+course_name.replaceAll(" ", "")+"/Cours";

         Drive drive_service = new Drive.Builder(new NetHttpTransport(), new GsonFactory(), credential)
                 .setApplicationName("testing haha")
                 .build();
         for (Announcement announcement : announcements){
                 List<Material> materials = announcement.getMaterials();

                 try{
                         for (Material material : materials){
                                 String file_id = material.getDriveFile().getDriveFile().getId();
                                 String file_name = material.getDriveFile().getDriveFile().getTitle();
                                 if (verif(file_name)) {
                                 	System.out.println("Downloading : "+file_name);
                                         try{
     										file_download(file_id, file_name, path, drive_service);
                                            //   downloads.add(file_name);
                                         }
                                         catch(IOException e) {
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
         }
 }
	private  void download_works(List<CourseWork> works, String course_name, Credential credential) throws IOException, ServletException {
		// TODO Auto-generated method stub
		System.out.println("Downloading course work...");
		String path = "/home/med/eclipse-workspace/Class/src/main/resources/classrooms/"+course_name.replaceAll(" ", "")+"/TD+TP";
	//	Credential credential = InitializeFlowTool.initializeFlow().loadCredential("123456");

        Drive drive_service = new Drive.Builder(new NetHttpTransport(), new GsonFactory(), credential)
                .setApplicationName("testing haha")
                .build();
        for (CourseWork work : works){
            List<Material> materials = work.getMaterials();

            try{
                    for (Material material : materials){
                            String file_id = material.getDriveFile().getDriveFile().getId();
                            String file_name = material.getDriveFile().getDriveFile().getTitle();
                            File file = new File(path+"/"+file_name);
                       //     if ( (!files.contains(file)) && verif(file_name)){
                            if (verif(file_name)) {
                            	System.out.println("Downloading : "+file_name);
                                    try{
										file_download(file_id, file_name, path, drive_service);
                                       //   downloads.add(file_name);
                                    }
                                    catch(IOException e) {
                                    e.printStackTrace();
                                    }
                    		}
                            else {
                            	System.out.println("Extension not supported " + file_name);
                            }
                           // }
                    }
            }
            catch(NullPointerException e){
            }
         
}   
		
	}

	private  void file_download(String file_id, String file_name, String path, Drive drive_service) throws IOException {
		System.out.println("Downloading "+file_name);
		// TODO Auto-generated method stub
		FileOutputStream outputstream = new FileOutputStream(path+"/"+file_name);



		drive_service.files().get(file_id).executeMediaAndDownloadTo(outputstream);

		FileChannel fchannel = outputstream.getChannel();
        long fileSize = fchannel.size();
     //   this.download_size += fileSize;
		outputstream.flush();
		outputstream.close();
	}
    private  boolean verif(String file_name){
        String extension = file_name.substring(file_name.lastIndexOf(".") + 1);
        List<String> extensions = Arrays.asList("pdf", "docx", "pptx", "png", "jpg", "html", "css", "js", "java",
"class", "txt", "r", "m", "sql", "doc", "mp3", "rar", "zip");
        return extensions.contains(extension);
}
	
}