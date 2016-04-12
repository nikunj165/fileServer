package org.adobe.initiation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;

public class Response {
	private static final int BUFFER_SIZE = 1024;
	private File file;
	private Request request;
	private OutputStream output;
	private HashMap<String, String> responseHeader = new HashMap<String, String>();
	private StatusCode statusCode;
	private ContentType contentType;
	public Response(OutputStream outputStream) {
		// TODO Auto-generated constructor stub
	}
	public Request getRequest() {
		return request;
	}
	public void setRequest(Request request) {
		this.request = request;
	}
	// public void sendStaticResource() throws IOException {
	//
	// FileInputStream fis = new FileInputStream(file);
	// int length = fis.available();
	// byte[] body = new byte[length];
	// fis.read(body);
	// fis.close();
	// setContentLength(length);
	// output.write(body);
	// try {
	// File file = new File(FileServer.WEB_ROOT + request.getUri());
	// if (file.exists()) {
	// fis = new FileInputStream(file);
	// int ch = fis.read(bytes, 0, BUFFER_SIZE);
	//
	// while (ch != -1) {
	// output.write(bytes, 0, ch);
	// ch = fis.read(bytes, 0, BUFFER_SIZE);
	// }
	// } else {
	// // file not found
	// String errorMessage = "HTTP/1.1 404 File Not Found\r\n"
	// + "Content-Type: text/html\r\n"
	// + "Content-Length: 23\r\n" + "\r\n"
	// + "<h1>File Not Found</h1>";
	// output.write(errorMessage.getBytes());
	// }
	// } catch (Exception e) {
	// // thrown if cannot instantiate a File object
	// System.out.println(e.toString());
	// } finally {
	// if (fis != null)
	// fis.close();
	// }
	// }
	public void setContentLength(long value) {
		responseHeader.put("Content-Length", String.valueOf(value));
	}
	public void setFile(File f) {
		this.file = f;
	}
	public void setDate(Date date) {
		responseHeader.put("Date", date.toString());
	}
	public void renderResponse(OutputStream outputStream) {
		FileInputStream fis = null;
		byte[] body;
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
			// String errorMessage = "HTTP/1.1 404 File Not Found\r\n"
			// + "Content-Type: text/html\r\n" + "Content-Length: 23\r\n"
			// + "\r\n" + "<h1>File Not Found</h1>";
			String errorMessage = "<h1>File Not Found</h1>";
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
			String response = "HTTP/1.1 " + statusCode.getCode() + "\r\n"
					+ "Content-Type: " + contentType.getContentType();

		}
	}
	private void setContentType(ContentType type) {
		this.contentType = type;
	}
	private void setStatusCode(StatusCode statusCode) {
		this.statusCode = statusCode;
	}

}
