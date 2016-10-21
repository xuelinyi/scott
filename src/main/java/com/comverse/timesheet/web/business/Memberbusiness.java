package com.comverse.timesheet.web.business;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.comverse.timesheet.web.bean.Member;
import com.comverse.timesheet.web.bean.TestTable;
import com.comverse.timesheet.web.bean.User;
import com.comverse.timesheet.web.dao.ITestTableDAO;
import com.comverse.timesheet.web.dao.MemberDao;

@Component
public class Memberbusiness {
	
	@Resource
	private MemberDao mDAO;
//	public MemberDao getmDAO() {
//		return mDAO;
//	}
//
//	public void setmDAO(MemberDao mDAO) {
//		this.mDAO = mDAO;
//	} 
	public void add() throws Exception{ 
        Member test=new Member(); 
        test.setId("yuanmomo6"); 
        test.setNickname((new Date()).toString()); 
        try { 
        	mDAO.add(test); 
        } catch (Exception e) { 
            throw e; 
        } 
    } 
	
	public void addAnfFind() {
		User user = new User(); 
		user.setId(1);
		user.setName("yixuelin");
		user.setAge("23");
		user.setTelephone("13521190257");
		mDAO.addMongo(user);
		User u = mDAO.findUserWithMongo(user.getName());
		System.out.println(" find  user : " + u);
	}
}
