package com.comverse.timesheet.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource; 
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller; 
import org.springframework.ui.ModelMap; 
import org.springframework.web.bind.annotation.RequestMapping;

import com.comverse.timesheet.web.bean.TestTable;
import com.comverse.timesheet.web.business.TestBusiness;
import com.comverse.timesheet.web.util.PageList;
@Controller 
public class ResultController { 
    //��spring �����ļ��е�bean ͨ��setterע����� 
    @Autowired
    private TestBusiness testBusiness; 
    
    @RequestMapping("/result") 
    public String viewUser(HttpServletRequest request, ModelMap modelMap) 
            throws Exception { 
        System.out.println("$$$$$$$$$$$$$$$$$$$$$you want to check the result.jsp+++++++++++++"); 
        System.out.println("$$$$$$$$$$$$$$$$$$$$$ ready to insert   +++++++++++++"); 
        testBusiness.add(); 
        System.out.println("$$$$$$$$$$$$$$$$$$$$$ insert completed +++++++++++++"); 
        return "result"; 
    } 
    @RequestMapping("/testDisplaytag") 
	 public String testDisplaytag(HttpServletRequest request, ModelMap modelMap) throws Exception{
    	List<TestTable> list=testBusiness.find();
		 System.out.println(":::=============================>>"+list);
		 String pageNumber = request.getParameter("page");  
	        if(pageNumber == null){  
	            pageNumber = "1";  
	        }  
	          
	        PageList pageList = new PageList();  
	          
	        pageList.setPageNumber(Integer.parseInt(pageNumber));  
	        pageList.setObjectsPerPage(4);  
	        pageList.setFullListSize(11);  
	        pageList.setList(list); 
	        modelMap.addAttribute("resultList", pageList);
	        System.out.println("----------->total:" + list.size());  
		 return "testpage";
	 }
}