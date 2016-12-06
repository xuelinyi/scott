package com.comverse.timesheet.web;

public enum OaLeaveEnum {
	ACTIVITY_USER(10000,"session user"){
		
	};
	
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

	private OaLeaveEnum(int sexFlag,String value) {
        this.sexFlag = sexFlag;
        this.value = value;
    }
	
	public static String getValue(int sexFlag) {
		for (OaLeaveEnum c : OaLeaveEnum.values()) {  
            if (c.getSexFlag() == sexFlag) {  
                return c.value;  
            }  
        }  
        return null;  
		
	}
	public static void main(String[] args) {
		System.out.println(OaLeaveEnum.ACTIVITY_USER);
	}
}
