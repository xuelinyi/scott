package com.comverse.timesheet.web.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.comverse.timesheet.web.bean.User;
import com.comverse.timesheet.web.dao.IUserDao;

public class IUserDaoImpl implements IUserDao<User> {
	@Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 修改操作
     *
     * @param entity
     * @throws Exception
     * @author yadong.zhang
     */
    public void update(User entity) throws Exception {
        Query query = new Query();
        query.addCriteria(new Criteria("telephone").is(entity.getTelephone()));
        Update update = new Update();
        update.set("telephone", entity.getTelephone());
        update.set("name", entity.getName());
        update.set("age", entity.getAge());
        update.set("time", entity.getTime());
        this.mongoTemplate.updateFirst(query, update, User.class);
    }
    /**
     * save函数根据参数条件,调用了insert或update函数:有则改之,无则加之
     *
     * @param entity
     * @throws Exception
     * @author yadong.zhang
     */
    public void save(User entity) throws Exception {
        this.mongoTemplate.save(entity);
    }
    /**
     * insert的对象如果存在则不会修改之前的值，也不会重新增加
     *
     * @param entity
     * @throws Exception
     * @author yadong.zhang
     */
    public void insert(User entity) throws Exception {
        //save函数根据参数条件,调用了insert或update函数:有则改之,无则加之
        this.mongoTemplate.insert(entity);
    }
    /**
     * 根据手机获取单个
     *
     * @param tel
     * @return
     * @throws Exception
     * @author yadong.zhang
     */
    public User getByTel(String tel) throws Exception {
        Query query = new Query();
        query.addCriteria(new Criteria("telephone").is(tel));
        return this.mongoTemplate.findOne(query, User.class);
    }
    /**
     * 删除
     *
     * @param user
     * @throws Exception
     * @author yadong.zhang
     */
    public void delete(User user) throws Exception {
        this.mongoTemplate.remove(user);
    }
}
