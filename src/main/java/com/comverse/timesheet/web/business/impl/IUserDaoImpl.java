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
     * �޸Ĳ���
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
     * save�������ݲ�������,������insert��update����:�����֮,�����֮
     *
     * @param entity
     * @throws Exception
     * @author yadong.zhang
     */
    public void save(User entity) throws Exception {
        this.mongoTemplate.save(entity);
    }
    /**
     * insert�Ķ�����������򲻻��޸�֮ǰ��ֵ��Ҳ������������
     *
     * @param entity
     * @throws Exception
     * @author yadong.zhang
     */
    public void insert(User entity) throws Exception {
        //save�������ݲ�������,������insert��update����:�����֮,�����֮
        this.mongoTemplate.insert(entity);
    }
    /**
     * �����ֻ���ȡ����
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
     * ɾ��
     *
     * @param user
     * @throws Exception
     * @author yadong.zhang
     */
    public void delete(User user) throws Exception {
        this.mongoTemplate.remove(user);
    }
}
