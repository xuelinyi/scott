package com.comverse.timesheet.web.util;

import java.util.List;

import org.displaytag.pagination.PaginatedList;
import org.displaytag.properties.SortOrderEnum;

public class PageList implements PaginatedList {  
	    /** 
	     * 每页列表，即每页显示的记录数 
	     */  
	    private List list;  
	    /** 
	     * 当前页码，即当前第几页 
	     */  
	    private int pageNumber = 1;  
	    /** 
	     * 每页记录数 pagesize 
	     */  
	    private int objectsPerPage = 10;  
	    /** 
	     * 总记录数 
	     */  
	    private int fullListSize = 0;  
	      
	    /** 
	     *从第几条记录开始，用来定位offset  
	     */  
	    private int offset = 0;  
	    private String sortCriterion;  
	    private SortOrderEnum sortDirection;  
	    private String searchId;  
	    public List getList() {  
	        return list;  
	    }  
	    public void setList(List list) {  
	        this.list = list;  
	    }  
	    public int getPageNumber() {  
	        return pageNumber;  
	    }  
	    public void setPageNumber(int pageNumber) {  
	        this.pageNumber = pageNumber;  
	    }  
	    public int getObjectsPerPage() {  
	        return objectsPerPage;  
	    }  
	    public void setObjectsPerPage(int objectsPerPage) {  
	        this.objectsPerPage = objectsPerPage;  
	    }  
	    public int getFullListSize() {  
	        return fullListSize;  
	    }  
	    public void setFullListSize(int fullListSize) {  
	        this.fullListSize = fullListSize;  
	    }  
	    public String getSortCriterion() {  
	        return sortCriterion;  
	    }  
	    public void setSortCriterion(String sortCriterion) {  
	        this.sortCriterion = sortCriterion;  
	    }  
	    public SortOrderEnum getSortDirection() {  
	        return sortDirection;  
	    }  
	    public void setSortDirection(SortOrderEnum sortDirection) {  
	        this.sortDirection = sortDirection;  
	    }  
	    public String getSearchId() {  
	        return searchId;  
	    }
	    public void setSearchId(String searchId) {  
	        this.searchId = searchId;  
	    }  
	    public int getOffset() {  
	        return (this.getPageNumber() - 1) * this.getObjectsPerPage();  
	    }  
}
