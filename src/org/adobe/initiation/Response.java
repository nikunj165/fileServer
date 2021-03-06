package org.adobe.initiation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;

public class Response {
	private File file;
	private HashMap<String, String> responseHeader = new HashMap<String, String>();
	private StatusCode statusCode;

	public void renderResponse(OutputStream outputStream) {
		FileInputStream fis = null;
		byte[] body = null;
		try {
			fis = new FileInputStream(file);
			int length = fis.available();
			body = new byte[length];
			fis.read(body);
			fis.close();
			setContentLength(length);
			setContentType(ContentType.TEXT);
			setStatusCode(StatusCode.OK);
		} catch (FileNotFoundException e) {
			setStatusCode(StatusCode.NOT_FOUND);
			String errorMessage = "<h4>File Not Found</h4>";
			setContentType(ContentType.HTML);
			setContentLength(errorMessage.length());
			body = errorMessage.getBytes();

		} catch (IOException e) {
			setStatusCode(StatusCode.SERVER_ERROR);
			String errorMessage = "<h1>Internal Server Error</h1>";
			setContentType(ContentType.HTML);
			setContentLength(errorMessage.length());
			body = errorMessage.getBytes();
		} finally {
			StringBuilder response = new StringBuilder("HTTP/1.1 ")
					.append(statusCode.getCode()).append("\n");

			for (String key : responseHeader.keySet()) {
				response.append(key).append(": ")
						.append(responseHeader.get(key)).append("\n");
			}
			response.append("\r\n");
			if (body != null)
				response.append(new String(body));
			PrintWriter writer = new PrintWriter(outputStream);
			writer.write(response.toString());
			writer.flush();
		}
	}
	private void setContentType(ContentType type) {
		responseHeader.put("Content-Type", type.getContentType());
	}
	private void setStatusCode(StatusCode statusCode) {
		this.statusCode = statusCode;
	}
	public void setContentLength(int value) {
		responseHeader.put("Content-Length", String.valueOf(value));
	}
	public void setFile(File f) {
		this.file = f;
	}
	public void setDate(Date date) {
		responseHeader.put("Date", date.toString());
	}
	public void renderMethodNotSupported(OutputStream out) {
		String errorMessage = "<h3>Method Not Allowed</h3>";
		setContentType(ContentType.HTML);
		setStatusCode(StatusCode.METHOD_NOT_ALLOWED);
		setContentLength(errorMessage.length());
		responseHeader.put("ALLOW", "GET");

		StringBuilder response = new StringBuilder("HTTP/1.1 ")
				.append(statusCode.getCode()).append("\n");

		for (String key : responseHeader.keySet()) {
			response.append(key).append(": ").append(responseHeader.get(key))
					.append("\n");
		}
		response.append("\r\n");
		response.append(errorMessage);
		PrintWriter writer = new PrintWriter(out);
		writer.write(response.toString());
		writer.flush();
	}
	public void renderBadRequest(OutputStream out) {
		String errorMessage = "<h3>Bad Request</h3>";
		setContentType(ContentType.HTML);
		setStatusCode(StatusCode.BAD_REQUEST);
		setContentLength(errorMessage.length());

		StringBuilder response = new StringBuilder("HTTP/1.1 ")
				.append(statusCode.getCode()).append("\n");

		for (String key : responseHeader.keySet()) {
			response.append(key).append(": ").append(responseHeader.get(key))
					.append("\n");
		}
		response.append("\r\n");
		response.append(errorMessage);
		PrintWriter writer = new PrintWriter(out);
		writer.write(response.toString());
		writer.flush();
	}
}
