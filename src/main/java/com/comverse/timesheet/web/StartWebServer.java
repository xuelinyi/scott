package com.comverse.timesheet.web;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.AprLifecycleListener;
import org.apache.catalina.core.StandardServer;
import org.apache.catalina.startup.Tomcat;

public class StartWebServer {
	/**
	 * 
	 * @param args
	 */
	@SuppressWarnings("serial")
	public static void main(String[] args) {
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(8080);
		tomcat.setBaseDir(".");
		tomcat.getHost().setAppBase(".");
		try {
			StandardServer server = (StandardServer) tomcat.getServer();
			AprLifecycleListener listener = new AprLifecycleListener();
			server.addLifecycleListener(listener);
			tomcat.getConnector().setURIEncoding("UTF-8");
			tomcat.getConnector().setProtocolHandlerClassName("org.apache.coyote.http11.Http11NioProtocol");
			System.out.println("AsyncTimeout = " + tomcat.getConnector().getAsyncTimeout());
			tomcat.getConnector().setAsyncTimeout(60000L);

			File base = new File(System.getProperty("java.io.tmpdir"));
			Context rootCtx = tomcat.addContext("/", base.getAbsolutePath());

			Tomcat.addServlet(rootCtx, "Redirect", new HttpServlet() {
				protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
						IOException {
					Writer w = resp.getWriter();
					w.write("<html><head><META http-equiv=\"refresh\" content=\"0;URL=/scott\"></head><body></body></html>");
					w.flush();
				}
			});

			rootCtx.addServletMapping("/*", "Redirect");

			tomcat.addWebapp("/scott", "./src/main/webapp");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			tomcat.start();
			tomcat.getServer().await();
		} catch (LifecycleException e) {
			e.printStackTrace();
		}
	}
}
