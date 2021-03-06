package com.comverse.timesheet.web;

public enum AuthorEnum {
	MAN(10000,"男"){
		
	},WOMAN(10001,"女");
	
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

	private AuthorEnum(int sexFlag,String value) {
        this.sexFlag = sexFlag;
        this.value = value;
    }
	
	public static String getValue(int sexFlag) {
		for (AuthorEnum c : AuthorEnum.values()) {  
            if (c.getSexFlag() == sexFlag) {  
                return c.value;  
            }  
        }  
        return null;  
		
	}
	public static void main(String[] args) {
		System.out.println(AuthorEnum.getValue(10000));
	}
}
