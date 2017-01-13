package com.comverse.timesheet.web.server;

import org.apache.log4j.Logger;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
	private static final Logger log = Logger.getLogger(Main.class);
	public static void main(String[] args) {
		log.debug("start..");
		final AbstractApplicationContext ctx = new ClassPathXmlApplicationContext("com/comverse/timesheet/web/server/serverContext.xml");
		ctx.start();
		log.debug("Server is started!");
		Runtime.getRuntime().addShutdownHook(new Thread(){
				public void run() {
					try {
						ctx.close();
					}catch(Exception e) {
						log.error(e,e);
					}
				}
		});
	}
}

