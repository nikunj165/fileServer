/**
 * 
 */
package org.adobe.initiation;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

/**
 * @author njain
 *
 */
public class TaskExecutor implements Runnable {

	Socket socket;
	FileServer server;

	public TaskExecutor(Socket socket, FileServer server) throws IOException {
		this.socket = socket;
		this.server = server;

	}
	@Override
	public void run() {
		try {
			Request request = new Request(socket.getInputStream());
			try {
				request.parseRequest();
				Response response = new Response(socket.getOutputStream());

				if (request.getMethod().equals("GET")) {
					File f = new File(server.getWEB_ROOT() + request.getUri());
					if (f.exists()) {
						response.setFile(f);
						response.setDate(new Date());
						response.renderResponse(socket.getOutputStream());
					}

					// response.setCode(200)
				}
			} catch (Exception e) {
			}

			//
		} catch (IOException e1) {

		}
	}
}
