package com.comverse.timesheet.web.business.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comverse.timesheet.web.bean.Dept;
import com.comverse.timesheet.web.business.DeptServices;

public class DeptServicesImpl implements DeptServices{
	public List findDept() {
        throw new RuntimeException("查找失败!");
    }

    public void deleteDept(Long id) {
        System.out.println("Delete dept " + id);
    }

    public List getDeptsForPo() {
        List depts = new ArrayList();
        depts.add(new Dept(1l, "教质部"));
        depts.add(new Dept(2l, "学术部"));
        depts.add(new Dept(3l, "就业部"));
        depts.add(new Dept(4l, "咨询部"));
        return depts;
    }

    
    public void saveDept(List<Dept> depts) {
        // System.out.println(dept.getId() + ":" + dept.getName());
        System.out.println(depts);
    }

    public List getDepts() {
        List depts = new ArrayList();
        Map map = new HashMap();
        map.put("id", "01");
        map.put("name", "教质部");
        depts.add(map);

        map = new HashMap();
        map.put("id", "02");
        map.put("name", "学术部");
        depts.add(map);

        map = new HashMap();
        map.put("id", "03");
        map.put("name", "就业部");
        depts.add(map);

        map = new HashMap();
        map.put("id", "04");
        map.put("name", "咨询部");
        depts.add(map);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return depts;
    }
}
