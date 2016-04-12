/**
 * 
 */
package org.adobe.initiation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author njain
 *
 */
public class Request {
	private InputStream input;
	private String uri;
	private String method;
	private String protocol;
	private HashMap<String, String> headers;
	private List<String> body;

	public Request(InputStream inputStream) {
		input = inputStream;
	}
	public void parseRequest() throws IOException {

		BufferedReader reader = new BufferedReader(
				new InputStreamReader(input));
		String line = reader.readLine();
		if (line == null)
			throw new IOException("Invalid HTTP Request");
		String[] requestHeaders = line.split(" ", 3);
		if (requestHeaders.length != 3) {
			throw new IOException(
					"Cannot parse request line from \"" + line + "\"");
		}
		method = requestHeaders[0];
		uri = requestHeaders[1];
		protocol = requestHeaders[2];

		line = reader.readLine();
		headers = new HashMap<String, String>();
		while (line != null && !line.isEmpty()) {
			requestHeaders = line.split(":", 2);
			if (requestHeaders.length != 2) {
				throw new IOException("Headers invalid");
			} else {
				headers.put(requestHeaders[0], requestHeaders[1]);
			}
			line = reader.readLine();
		}

		body = new ArrayList<String>();
		while (reader.ready()) {
			line = reader.readLine();
			body.add(line);
		}

	}
	public String parseUri(String requestString) {
		int index1, index2;
		index1 = requestString.indexOf(' ');

		if (index1 != -1) {
			index2 = requestString.indexOf(' ', index1 + 1);
			if (index2 > index1)
				return requestString.substring(index1 + 1, index2);
		}

		return null;
	}
	public String getUri() {
		return uri;

	}

	public String getMethod() {
		return method;
	}

}
