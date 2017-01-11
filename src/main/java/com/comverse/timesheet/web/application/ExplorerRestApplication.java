package com.comverse.timesheet.web.application;

import org.activiti.rest.common.api.DefaultResource;
import org.activiti.rest.common.application.ActivitiRestApplication;
import org.activiti.rest.common.filter.JsonpFilter;
import org.apache.commons.collections.bag.SynchronizedBag;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class ExplorerRestApplication extends ActivitiRestApplication{
	public ExplorerRestApplication(){
		super();
	}
	/**
	 * Creates a root Restlet that will receive all incoming calls.
	 */
	@Override
	public synchronized Restlet createInboundRoot() {
		Router router = new Router(getContext());
		router.attachDefault(DefaultResource.class);
		ModelerServicesInit.attachResources(router);
		JsonpFilter jsonpFilter = new JsonpFilter(getContext());
		jsonpFilter.setNext(router);
		return jsonpFilter;
	}
}
