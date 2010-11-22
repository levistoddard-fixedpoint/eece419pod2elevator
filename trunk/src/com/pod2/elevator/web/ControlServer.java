package com.pod2.elevator.web;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

import com.pod2.elevator.main.CentralController;

public class ControlServer {

	private final CentralController controller;

	Server server;

	public ControlServer(CentralController controller) {
		assert (controller != null);
		this.controller = controller;
	}

	public void start(int port) throws ControlServerException {
		if (server != null) {
			throw new ControlServerException("server already running!");
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

	public void stop() throws ControlServerException {
		if (server == null) {
			throw new ControlServerException("no server running!");
		}
		try {
			server.stop();
			server = null;
		} catch (Exception e) {
			throw new ControlServerException(e);
		}
	}

}
