package com.comverse.timesheet.web.dao.impl;

import javax.annotation.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Repository;

import com.comverse.timesheet.web.bean.Member;
import com.comverse.timesheet.web.bean.User;
import com.comverse.timesheet.web.dao.MemberDao;
import com.comverse.timesheet.web.dao.RedisGeneratorDao;
import com.mongodb.BasicDBObject;

@Repository
public class MemberDaoImpl extends RedisGeneratorDao<String,Member> implements MemberDao{
	
		@Resource
		private MongoTemplate mongoTemplate;
		
//		public MongoTemplate getMongoTemplate() {
//			return mongoTemplate;
//		}
//	
		public void setMongoTemplate(MongoTemplate mongoTemplate) {
			this.mongoTemplate = mongoTemplate;
		}
		private static final String USER_COLLECT = "yixuelin";
	  /**
	   * ��Ӷ���
	   */
	  public boolean add(final Member member) {  
		  System.out.println("zengjia============================member : >>"+member);
	    boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {  
	      public Boolean doInRedis(RedisConnection connection)  
	          throws DataAccessException {  
	        RedisSerializer<String> serializer = getRedisSerializer();  
	        byte[] key  = serializer.serialize(member.getId());  
	        byte[] name = serializer.serialize(member.getNickname());  
	        return connection.setNX(key, name);  
	      }  
	    });  
	    return result;  
	  }

	public void addMongo(User user) {
		mongoTemplate.save(user);
	}

	public User findUserWithMongo(String name) {
		// TODO Auto-generated method stub
		  Query query = new Query();  
        query.addCriteria(new Criteria("name").is(USER_COLLECT));  
        return this.mongoTemplate.findOne(query, User.class);  
		//return mongoTemplate.findOne(new BasicDBObject("age", 26));
	}  

	  /**
//	   * ��Ӽ���
//	   */
//	  @Override
//	  public boolean add(final List<Member> list) {
//	    Assert.notEmpty(list);  
//	    boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {  
//	      public Boolean doInRedis(RedisConnection connection)  
//	          throws DataAccessException {  
//	        RedisSerializer<String> serializer = getRedisSerializer();  
//	        for (Member member : list) {  
//	          byte[] key  = serializer.serialize(member.getId());  
//	          byte[] name = serializer.serialize(member.getNickname());  
//	          connection.setNX(key, name);  
//	        }  
//	        return true;  
//	      }  
//	    }, false, true);  
//	    return result; 
//	  }  
//	  
//	  /**
//	   * ɾ����� ,����key
//	   */
//	  public void delete(String key) {  
//	    List<String> list = new ArrayList<String>();  
//	    list.add(key);  
//	    delete(list);  
//	  }  
//	  
//	  /**
//	   * ɾ��� ,����key����
//	   */
//	  public void delete(List<String> keys) {  
//	    redisTemplate.delete(keys);  
//	  }  
//	  
//	  /**
//	   * �޸Ķ��� 
//	   */
//	  public boolean update(final Member member) {  
//	    String key = member.getId();  
//	    if (get(key) == null) {  
//	      throw new NullPointerException("����в�����, key = " + key);  
//	    }  
//	    boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {  
//	      public Boolean doInRedis(RedisConnection connection)  
//	          throws DataAccessException {  
//	        RedisSerializer<String> serializer = getRedisSerializer();  
//	        byte[] key  = serializer.serialize(member.getId());  
//	        byte[] name = serializer.serialize(member.getNickname());  
//	        connection.set(key, name);  
//	        return true;  
//	      }  
//	    });  
//	    return result;  
//	  }  
//	  
//	  /**
//	   * ���key��ȡ����
//	   */
//	  public Member get(final String keyId) {  
//	    Member result = redisTemplate.execute(new RedisCallback<Member>() {  
//	      public Member doInRedis(RedisConnection connection)  
//	          throws DataAccessException {  
//	        RedisSerializer<String> serializer = getRedisSerializer();  
//	        byte[] key = serializer.serialize(keyId);  
//	        byte[] value = connection.get(key);  
//	        if (value == null) {  
//	          return null;  
//	        }  
//	        String nickname = serializer.deserialize(value);  
//	        return new Member(keyId, nickname);  
//	      }  
//	    });  
//	    return result;  
//	  }  

}
