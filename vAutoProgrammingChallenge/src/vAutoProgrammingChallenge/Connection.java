package vAutoProgrammingChallenge;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Connection {
	
	//API GET Request over HTTP
	public String connectToAPI(String urlPass) throws IOException {

		URL url = new URL(urlPass);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setConnectTimeout(5000);
		connection.setReadTimeout(5000);
		int responsecode = connection.getResponseCode();
			
		if(responsecode != 200) {
			throw new RuntimeException("HttpResponseCode: " + responsecode);				
		}
		else {	
			//get result
			BufferedReader in = new BufferedReader(
			new InputStreamReader(connection.getInputStream()));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			connection.disconnect();
			return content.toString();
		}
	}
	
	//Method to send Post
	public void sendToAPI(String urlPass, String param) throws IOException {
		
		URL url = new URL(urlPass);
		HttpURLConnection connectionOut = (HttpURLConnection) url.openConnection();
		connectionOut.setRequestMethod("POST");
		connectionOut.setConnectTimeout(5000);
		connectionOut.setReadTimeout(5000);
		connectionOut.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		connectionOut.setRequestProperty("Content-Length", Integer.toString(param.length()));
		connectionOut.setDoOutput(true);
		connectionOut.setDoInput(true);
		connectionOut.connect();
		
		System.out.println(param);
		
	    OutputStream os = connectionOut.getOutputStream();
	    os.write(param.getBytes());
	    os.flush();
	    os.close();
		
		int responsecode = connectionOut.getResponseCode();
		if(responsecode != 200) {
			System.out.println("Response Code is " + responsecode);				
		}
		if(responsecode == 200) {
			System.out.println("IT WORKED");
		}
	}
}
