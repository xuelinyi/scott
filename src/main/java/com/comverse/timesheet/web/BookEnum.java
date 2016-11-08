package com.comverse.timesheet.web;

import java.util.EnumMap;

public enum BookEnum {
	ROMANCE(10000,"言情"),MOTION (10001,"动作"),IS_CHECK_NO (10002,"未审核")
	,IS_CHECK_ONGOING (10003,"审核中"),IS_CHECK_FAIL (10004,"审核未通过"),
	IS_CHECK_SUCCESS (10005,"审核通过"),ILLEGAL_CHARACTER (10005,"做爱,暴力,杀戮,毁灭");
	
	private int sexFlag;
	private String value;
	public int getSexFlag() {
		return sexFlag;
	}

	public void setSexFlag(int sexFlag) {
		this.sexFlag = sexFlag;
	}

	public void setValue(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	}
	
	private BookEnum(int sexFlag,String value) {
        this.sexFlag = sexFlag;
        this.value = value;
    }
	
	public static String getValue(int sexFlag) {
		for (BookEnum c : BookEnum.values()) {  
            if (c.getSexFlag() == sexFlag) {  
                return c.value;  
            }  
        }  
        return null;  
		
	}
	public static void main(String[] args) {
		System.out.println(BookEnum.getValue(10000));
	}
}
