package com.comverse.timesheet.web.server.impl;

import java.util.HashSet;



import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

import com.comverse.timesheet.web.server.Constants;
import com.comverse.timesheet.web.server.ISessionCache;
import com.comverse.timesheet.web.server.ISessionSaver;
import com.comverse.timesheet.web.server.dto.CacheKey;
import com.comverse.timesheet.web.server.dto.CacheSession;
import com.comverse.timesheet.web.server.message.SessionSegmentMessage;


public class SessionCache implements ISessionCache {
	private static Logger log = Logger.getLogger(SessionCache.class);

	private ConcurrentHashMap<CacheKey,CacheSession> cache = new ConcurrentHashMap<CacheKey,CacheSession>();
	private Set<CacheKey> lastKeySet=new HashSet<CacheKey>();
	private AtomicLong sessionCount = new AtomicLong(0L);
	
	private boolean isStoping=false;
	public void addSessionSegmentMessage(SessionSegmentMessage message){
		if(message==null){
			return;
		}
		if(isStoping){
			log.debug("isStoping message "+message);
			return;
		}
		boolean complete=message.getTlvList().size()==0;
		boolean addOnThisTime=false;
		
		CacheKey key=new CacheKey(message);
		CacheSession cacheSession=cache.get(key);
		if(cacheSession==null){
			cacheSession=new CacheSession(message);
			cache.put(key,cacheSession);
		}
		lastKeySet.add(key);//update lastKeySet
		
		log.debug(cacheSession.getSessionId()+" getTlvList().size() = "+message.getTlvList().size());
		
		cacheSession.merge(message);
		
		

		if(!cacheSession.isAdded() && cacheSession.allowAdded()){
			log.debug(cacheSession.getSessionId()+" added");
			sessionCount.addAndGet(1);
			cacheSession.setAdded(true);
			sessionSaver.add(cacheSession,Constants.SESSION_LOG_SESSION_KILLED_NO);	
			addOnThisTime=true;
		}
		
		if(complete){
			log.debug(cacheSession.getSessionId()+" complete");
			sessionSaver.update(cacheSession,Constants.SESSION_LOG_SESSION_KILLED_NORMAL);
			cache.remove(key);
		}
		
	}
	
	public synchronized void timeout(){
		log.info("timeout...");		
		log.info("all sessions = "+sessionCount.get());
		long current=System.currentTimeMillis();
		lastKeySet.clear();
		
		for(CacheKey key:cache.keySet()){
			CacheSession cacheSession=cache.get(key);
			if(cacheSession!=null && current>cacheSession.getLast()+Constants.SESSION_LOG_TIMEOUT){
				log.info("session = "+cacheSession.getSessionId()+" timeout.");
				if(!cacheSession.isAdded()){
					sessionSaver.add(cacheSession,Constants.SESSION_LOG_SESSION_KILLED_TIMEOUT);	
				}else{
					sessionSaver.update(cacheSession,Constants.SESSION_LOG_SESSION_KILLED_TIMEOUT);	
				}
				
				cache.remove(key);
			}
		}
		
	}
	public void start(){
		log.debug("start...");
	}
	public void stop(){
		log.debug("stop...");
		isStoping=true;
		for(CacheKey key:cache.keySet()){
			CacheSession cacheSession=cache.get(key);
			if(cacheSession!=null){
				if(!cacheSession.isAdded()){
					sessionSaver.add(cacheSession,Constants.SESSION_LOG_SESSION_KILLED_TIMEOUT);	
				}else{
					sessionSaver.update(cacheSession,Constants.SESSION_LOG_SESSION_KILLED_TIMEOUT);	
				}			
			}
		}
	}
	
	private ISessionSaver sessionSaver;

	public void setSessionSaver(ISessionSaver sessionSaver) {
		this.sessionSaver = sessionSaver;
	}

	

}


