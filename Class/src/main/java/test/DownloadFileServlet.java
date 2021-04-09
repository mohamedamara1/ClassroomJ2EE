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
import com.google.api.services.classroom.model.Course;
import com.google.api.services.classroom.model.CourseWork;
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
		
	//	in this part we extract a list of strings , strings of classroom_id 
		
        List<String> courses_to_download =  Arrays.asList("257955132266","234265883466");
        
        List<String> existing_courses =  Arrays.asList("234265883466");
        
        for( String course : courses_to_download) {
        	if (! existing_courses.contains(course) ) {
        		initial_download_course(course);
        		
        	}
        }
        
        try {
			DbUtil.connectionToDerby();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			DbUtil.normalDbUsage();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

        

	}

	private void initial_download_course(String course_id) throws IOException, ServletException {
		
		// TODO Auto-generated method stub
		Credential credential = InitializeFlowTool.initializeFlow().loadCredential("123456");

        System.out.println("CREDENTIAL = "+ credential);
        Classroom service = new Classroom.Builder(new NetHttpTransport(), new GsonFactory(), credential)
                .setApplicationName("testing haha")
                .build();
		Course course = service.courses().get(course_id).execute();
		
		String path = "/home/med/eclipse-workspace/Class/src/main/resources/classrooms/"+course.getName().replaceAll(" ", "");
		
		File course_folder = new File(path);
		
		course_folder.mkdir();
		
		(new File(path+"/Cours")).mkdir();
		(new File(path+"/TD+TP")).mkdir();		
		System.out.println("New course folder created : "+course.getName().replaceAll(" ", ""));
		

        ListCourseWorkResponse works_response = service.courses().courseWork().list(course_id).execute();
        List<CourseWork> works = works_response.getCourseWork();
		
        try{
           download_works(works, course.getName());
	    }
	
	    catch (NullPointerException e){
	            System.out.println("this course doesn't have any work!");
	    }

		
	}

	private void download_works(List<CourseWork> works, String course_name) throws IOException, ServletException {
		// TODO Auto-generated method stub
		System.out.println("Downloading course work...");
		String path = "/home/med/eclipse-workspace/Class/src/main/resources/classrooms/"+course_name.replaceAll(" ", "")+"/TD+TP";
		Credential credential = InitializeFlowTool.initializeFlow().loadCredential("123456");

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

	private void file_download(String file_id, String file_name, String path, Drive drive_service) throws IOException {
		System.out.println("Inside file_download method, downloading file!!");
		// TODO Auto-generated method stub
		System.out.println(path+"/"+file_name);
		FileOutputStream outputstream = new FileOutputStream(path+"/"+file_name);



		drive_service.files().get(file_id).executeMediaAndDownloadTo(outputstream);

		FileChannel fchannel = outputstream.getChannel();
        long fileSize = fchannel.size();
     //   this.download_size += fileSize;
		outputstream.flush();
		outputstream.close();
	}
    private boolean verif(String file_name){
        String extension = file_name.substring(file_name.lastIndexOf(".") + 1);
        List<String> extensions = Arrays.asList("pdf", "docx", "pptx", "png", "jpg", "html", "css", "js", "java",
"class", "txt", "r", "m", "sql", "doc", "mp3", "rar", "zip");
        return extensions.contains(extension);
}
	
}