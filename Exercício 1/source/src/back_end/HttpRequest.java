package back_end;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpRequest {
	
	private HttpRequest() {}
	
	public static String get(String targetURL) {
		return get(targetURL, null);
	}
	
	public static String get(String targetURL, Map<String, String> headerDict) {
		  HttpURLConnection connection = null;
		  
		  try {
		    URL url = new URL(targetURL);
		    connection = (HttpURLConnection) url.openConnection();
		    
		    connection.setRequestMethod("GET");
		    
		    if (headerDict != null) {
		    	for (Map.Entry<String, String> entry : headerDict.entrySet()) {
		    		connection.setRequestProperty(entry.getKey(), entry.getValue());
		    	}
		    }
		      
		    connection.setUseCaches(false);

		    InputStream iStream = connection.getInputStream();
		    BufferedReader rStream = new BufferedReader(new InputStreamReader(iStream));
		    StringBuilder response = new StringBuilder();
		    String line;
		    while ((line = rStream.readLine()) != null) {
		      response.append(line);
		      response.append('\r');
		    }
		    rStream.close();
		    return response.toString();
		  } catch (Exception e) {
		    throw new RuntimeException();
		  } finally {
		    if (connection != null) {
		      connection.disconnect();
		    }
		  }
		}
}
