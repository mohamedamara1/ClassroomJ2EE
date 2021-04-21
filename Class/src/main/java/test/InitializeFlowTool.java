package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.classroom.Classroom;
import com.google.api.services.classroom.ClassroomScopes;
import com.google.api.services.drive.DriveScopes;

public class InitializeFlowTool {
	
	/** Application name. */
    private static final String APPLICATION_NAME =
        "Classroom API Java Test";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
        System.getProperty("user.home"), ".credentials/classroom-java-test");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
    		 new GsonFactory();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/drive-java-quickstart
     */
    private static final List<String> SCOPES = Arrays.asList( 
    		ClassroomScopes.CLASSROOM_COURSES, //https://www.googleapis.com/auth/admin.directory.group\n"
    	    ClassroomScopes.CLASSROOM_ANNOUNCEMENTS,
            ClassroomScopes.CLASSROOM_COURSEWORK_ME_READONLY,
            ClassroomScopes.CLASSROOM_COURSEWORK_STUDENTS_READONLY,
    	    ClassroomScopes.CLASSROOM_ROSTERS,
    	    ClassroomScopes.CLASSROOM_PROFILE_EMAILS,
    	    ClassroomScopes.CLASSROOM_PROFILE_PHOTOS,
    	    DriveScopes.DRIVE
);


    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }
  //  private final static String REDIRECT_URI = "https://9e7a6dd2941919.localhost.run/Class/Oauth2callback";
    private final static String REDIRECT_URI = "http://localhost:8080/Class/Oauth2callback";
	
	public static String getREDIRECT_URI() {
		return REDIRECT_URI;
	}
	
	private static GoogleAuthorizationCodeFlow flow;

	public static AuthorizationCodeFlow initializeFlow() throws ServletException, IOException {
		// TODO Auto-generated method stub
		InputStream in = InitializeFlowTool.class.getResourceAsStream("/client_secret.json");
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
		if(flow == null)
			flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
					clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY)
							.setClientId("404366420582-i7m41p0cv9vgijdbu7rmv4sog8d3r61h.apps.googleusercontent.com")
							.setAccessType("offline").build();

		return flow;
	}
/*	public static AuthorizationCodeFlow initializeFlowDrive() throws ServletException, IOException {
		// TODO Auto-generated method stub
		InputStream in = InitializeFlowTool.class.getResourceAsStream("/client_secret_drive.json");
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
		if(flow == null)
			flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
					clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY)
							.setClientId("603357487526-1qo3kn7rrmalo8krbsv9ebgo0ih53rjv.apps.googleusercontent.com")
							.setAccessType("offline").build();

		return flow;
	}*/
	public static Classroom initializeClassroom(Credential credential) {
        Classroom service = new Classroom.Builder(new NetHttpTransport(), new GsonFactory(), credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
        return service;
	}
	
	public static Credential getValidCredential(String userId) throws ServletException {
		try {
			Credential credential = initializeFlow().loadCredential(userId);
         //   Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize(userId);

			System.out.println("CREDENTIAL DATA STORE = "+credential);
			if (credential != null && credential.getAccessToken() != null) {
				System.out.println("cred not null and access token not null");
				return credential;
	
			}
				/*if (credential.getExpiresInSeconds() > 0)
					return credential;
				else
				if (credential.getExpiresInSeconds() < 3000)
					if (credential.refreshToken())*/
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
