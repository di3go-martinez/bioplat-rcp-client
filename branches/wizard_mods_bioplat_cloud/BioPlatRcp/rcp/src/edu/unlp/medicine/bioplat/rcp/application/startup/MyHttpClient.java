package edu.unlp.medicine.bioplat.rcp.application.startup;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

public class MyHttpClient {
	private static final  String USER_AGENT = "Mozilla/5.0";
	
//	public static void main(String[] args) throws ClientProtocolException, IOException {
//		
//		// BufferedReader rd = new BufferedReader(
//		// new InputStreamReader(response.getEntity().getContent()));
//		//
//		// StringBuffer result = new StringBuffer();
//		// String line = "";
//		// while ((line = rd.readLine()) != null) {
//		// result.append(line);
//		// }
//	}
	
	public MyHttpClient() {
		// TODO Auto-generated constructor stub
	}
	
	
	private int testConnection(){
		String url = "http://www.google.com/search?q=httpClient";

		//No tiene en cuenta las variables del sistema a propósito (http.proxyHost, etc.) ver javadoc
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);

		// add request header
		request.addHeader("User-Agent", USER_AGENT);
		HttpResponse response;
		int statusCode =-1;	
		try {
			response = client.execute(request);
			statusCode = response.getStatusLine().getStatusCode();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return statusCode;

	}
	
	public boolean connectionAvailable(){
		return testConnection()==200;
	}
	
	public static void main(String[] args) {
		System.out.println(new MyHttpClient().connectionAvailable());
	}
}
