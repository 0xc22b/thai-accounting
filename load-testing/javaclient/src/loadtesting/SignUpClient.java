package loadtesting;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class SignUpClient {
	
	enum Method { GET, POST, PUT, DELETE };
	
	final static private String UTF8 = "UTF-8";
	
	private String url;
	
	public SignUpClient(String url) {
		this.url = url;
	}
	
	public String signUp(String username, String email, String password, String repeatPassword) {

	    Map<String, String> params = new HashMap<String, String>();
		params.put("username", username);
		params.put("email", email);
		params.put("password", password);
		params.put("repeatPassword", repeatPassword);
		
		return POST(url, params);
	}
	
	private String POST(String url, Map<String, String> params) {
		return doRequest(url, Method.POST, params);
	}

	/*private String GET(String url, Map<String, String> params) {
		return doRequest(url, Method.GET, params);
	}*/

    private String doRequest(String urlBase, Method method, Map<String, String> params) {
		StringBuilder encoded = new StringBuilder();
		if (params != null) {
			for (String key : params.keySet()) {
				if (encoded.length() > 0) {
					encoded.append("&");
				}
				try {
					encoded.append(key).append("=").append(URLEncoder.encode(params.get(key), UTF8));
				} catch (UnsupportedEncodingException e) {
					// wow -- don't have UTF-8 ???
				}
			}
		}

		// build up our URL
		StringBuilder url = new StringBuilder();
		url.append(urlBase);

		// if method is a GET, and there are params, then append them to the URL
		// as query args.
		if (method.equals(Method.GET) && (encoded.length() > 0)) {
			url.append("?").append(encoded.toString());
		}
            
		try {
			URL resturl = new URL(url.toString());
			HttpURLConnection con = (HttpURLConnection) resturl.openConnection();
			con.setDoOutput(true); // This implicitly sets req method to POST.
			con.setRequestProperty("Accept-Charset", UTF8);
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + UTF8);

			// initialize a new curl object
			if (method.equals(Method.GET)) {
				con.setRequestMethod("GET");
			} else if (method.equals(Method.POST)) {
				con.setRequestMethod("POST");
				OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
				out.write(encoded.toString());
				out.close();
			} else if (method.equals(Method.PUT) ) {
				con.setRequestMethod("PUT");
				OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
				out.write(encoded.toString());
				out.close();
			} else if (method.equals(Method.DELETE) ) {
				con.setRequestMethod("DELETE");
			} else {
				throw new IllegalArgumentException("Unknown method " + method.toString());
			}

			// prepare to read the response from the server.
			Reader in = null;
			try {
				if (con.getInputStream() != null) {
					in = new InputStreamReader(con.getInputStream(), UTF8);
				}				
			} catch (IOException e) {
				if (con.getErrorStream() != null) {
					in = new InputStreamReader(con.getErrorStream(), UTF8);
				}
			}
			if (in == null) {
				throw new RuntimeException("Unable to read response from server");
			}

			// accumulate the server response
			final char[] buffer = new char[1024];
			StringBuilder response = new StringBuilder();
			int read;
			do {
				read = in.read(buffer, 0, buffer.length);
				if (read > 0) {
					response.append(buffer, 0, read);
				}
			} while (read >= 0);
			in.close();

			// get result code
			int responseCode = con.getResponseCode();
			if ( responseCode != 200 ) {
				throw new RuntimeException("the server responded with an error code: " + responseCode);
			}

			// return the string that we got from the server.
			return response.toString();
		}
		catch (MalformedURLException e) {
			throw new RuntimeException(e.getMessage());
		}
		catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
