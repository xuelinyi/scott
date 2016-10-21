package com.comverse.timesheet.web.dao;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.comverse.timesheet.web.bean.Member;

public abstract class RedisGeneratorDao <K extends Serializable, V extends Serializable>  {
	  
	  protected RedisTemplate<K,V> redisTemplate ;

	  /** 
	   * …Ë÷√redisTemplate 
	   * @param redisTemplate the redisTemplate to set 
	   */  
	  public void setRedisTemplate(RedisTemplate<K, V> redisTemplate) {  
	    this.redisTemplate = redisTemplate;  
	  }  
	    
	  /** 
	   * ªÒ»° RedisSerializer 
	   * <br>------------------------------<br> 
	   */  
	  protected RedisSerializer<String> getRedisSerializer() {  
	    return redisTemplate.getStringSerializer();  
	  }
	  
}
