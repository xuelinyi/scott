package com.comverse.timesheet.web.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.comverse.timesheet.web.bean.User;

public interface IUserDao<T> {
	void update(T entity) throws Exception;
	void save(T entity) throws Exception;
	void insert(T entity) throws Exception;
	User getByTel(String tel) throws Exception;
	void delete(T user) throws Exception;
}
