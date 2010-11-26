package com.jettytest;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

import com.vaadin.terminal.gwt.server.ApplicationServlet;

public class Main {

	private static final int PORT = 8082;

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Server server = new Server();

		SocketConnector connector = new SocketConnector();
		connector.setPort(PORT);
		connector.setReuseAddress(true);
		server.setConnectors(new Connector[] { connector });

		Context root = new Context(server, "/", Context.SESSIONS);
		ServletHolder appHolder = new ServletHolder(new ApplicationServlet());
		appHolder.setInitParameter("application",
				"com.jettytest.WebInterface");
		root.addServlet(appHolder, "/*");

		server.start();
	}

}

