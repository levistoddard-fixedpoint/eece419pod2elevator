package com.pod2.elevator.web;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

import com.pod2.elevator.main.CentralController;

/**
 * OVERVIEW: Manages the starting and stopping of a web server which hosts the
 * web control interface.
 * 
 */
public class ControlServer {

	private final CentralController controller;
	private Server server;

	public ControlServer(CentralController controller) {
		assert (controller != null);
		this.controller = controller;
	}

	/**
	 * MODIFIES: server
	 * 
	 * EFFECTS: Initializes the web server to listen for requests on specified
	 * port. Throws a ControlServerException if the server is already running,
	 * or it cannot be started for any reason.
	 * 
	 */
	public void start(int port) throws ControlServerException {
		if (server != null) {
			throw new ControlServerException("server already running.");
		}
		try {
			server = new Server();

			SocketConnector connector = new SocketConnector();
			connector.setPort(port);
			connector.setReuseAddress(true);
			server.setConnectors(new Connector[] { connector });

			Context root = new Context(server, "/", Context.SESSIONS);
			ControlServlet appServlet = new ControlServlet(controller);
			ServletHolder appHolder = new ServletHolder(appServlet);
			root.addServlet(appHolder, "/*");

			server.start();
		} catch (Exception e) {
			throw new ControlServerException(e);
		}
	}

	/**
	 * MODIFIES: server
	 * 
	 * EFFECTS: Stops the running web server. Throws ControlServerException if
	 * no server was running, or if the server could not be stopped for any
	 * reason.
	 * 
	 */
	public void stop() throws ControlServerException {
		if (server == null) {
			throw new ControlServerException("no server running.");
		}
		try {
			server.stop();
			server = null;
		} catch (Exception e) {
			throw new ControlServerException(e);
		}
	}

}
