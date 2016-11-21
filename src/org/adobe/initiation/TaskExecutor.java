/**
 * 
 */
package org.adobe.initiation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

/**
 * @author njain
 *
 */
public class TaskExecutor implements Runnable {

	Socket socket;
	FileServer server;

	public TaskExecutor(Socket socket, FileServer server) {
		this.socket = socket;
		this.server = server;

	}
	@Override
	public void run() {
		Response response = new Response();

		try {
			System.out.println("Executing Request");
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			Request request = new Request(in);
			request.parseRequest();

			if (request.getMethod().equals("GET")) {
				File f = new File(server.getWEB_ROOT() + request.getUri());
				response.setFile(f);
				response.setDate(new Date());
				response.renderResponse(out);
			} else {
				response.renderMethodNotSupported(out);
			}
			in.close();
			out.close();

		} catch (IOException e1) {
			System.err
					.println("IOException occurred while executing the request "
							+ e1.getMessage());
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}
	}
}
