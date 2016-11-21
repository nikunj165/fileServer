/**
 * 
 */
package org.adobe.initiation;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author njain
 *
 */
public class FileServer implements Runnable {

	private ServerSocket server;
	private final String webRoot;
	private ExecutorService threadsPool;
	private final int port;
	private final int threadsLimit;

	public FileServer(int port, int numThreads, String webRoot) {
		this.webRoot = webRoot;
		this.port = port;
		threadsLimit = numThreads;
	}

	public static void main(String[] args) {
		int port = 8086;
		int numThreads = 10;
		String webroot = "wwwroot";
		if (args.length == 0) {
			System.out.println(
					"Usage: java -jar fileserver.jar [port] [numThreads] [webroot]");
		} else {
			port = Integer.parseInt(args[0]);
			numThreads = Integer.parseInt(args[1]);
			webroot = args[2];
		}
		// Construct a server object
		FileServer server = new FileServer(port, numThreads, webroot);
		// Start the server thread
		new Thread(server).start();
	}

	@Override
	public void run() {
		try {
			this.server = new ServerSocket(port);
		} catch (IOException e1) {
			System.err.println(
					"Cannot listen on port:" + port + " " + e1.getMessage());
			System.exit(1);
		}
		threadsPool = Executors.newFixedThreadPool(threadsLimit);
		System.out.println("Server running on the port " + port
				+ " with web root folder \"" + webRoot + "\" and "
				+ threadsLimit + " threads limit.");

		while (!Thread.interrupted()) {
			try {
				threadsPool.execute(
						new Thread(new TaskExecutor(server.accept(), this)));
			} catch (Exception e) {
				System.err.println("Exception occurred while executing request "
						+ e.getMessage());
			}
		}
		// close the server
		close();
	}

	private void close() {
		try {
			server.close();
		} catch (IOException e) {
			System.err.println(
					"Error while closing server socket." + e.getMessage());
		}
		shutdownAndAwaitTermination(threadsPool);
	}
	void shutdownAndAwaitTermination(ExecutorService pool) {
		pool.shutdown(); // Disable new tasks from being submitted
		try {
			// Wait a while for existing tasks to terminate
			if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
				pool.shutdownNow(); // Cancel currently executing tasks
				// Wait a while for tasks to respond to being cancelled
				if (!pool.awaitTermination(60, TimeUnit.SECONDS))
					System.err.println("Pool did not terminate");
			}
		} catch (InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted
			pool.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
	}
	public String getWEB_ROOT() {
		return webRoot;
	}
}
