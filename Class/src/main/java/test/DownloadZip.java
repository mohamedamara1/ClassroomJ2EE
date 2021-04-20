package test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DownloadZip
 */
public class DownloadZip extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DownloadZip() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
		//String zipFullPath = (String) request.getAttribute("path");
	        
       // String filename = (String) request.getAttribute("name");
        
		String zipFullPath= (String)request.getSession().getAttribute("path");
		String filename= (String)request.getSession().getAttribute("name");
        // zipFullPath = parent_dir + "/" + filename;

        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment;filename=\"" + filename + "\"");
        OutputStream out = response.getOutputStream();

        FileInputStream fis = new FileInputStream(zipFullPath);
        int bytes;
        while ((bytes = fis.read()) != -1) {
            System.out.println(bytes);
            out.write(bytes);
        }
        fis.close();
        response.flushBuffer();

        System.out.println(".zip file downloaded at client successfully");
	    }
   

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
