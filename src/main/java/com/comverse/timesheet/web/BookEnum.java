package com.comverse.timesheet.web;

public enum BookEnum {
	ROMANCE(10000,"言情"){
		
	},MOTION (10001,"动作");
	
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
