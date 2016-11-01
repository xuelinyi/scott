package com.comverse.timesheet.web;

public enum AuthorEnum {
	MAN(10000){
		
	},WOMAN(10001);
	
	private int value;
	private AuthorEnum(int value) {
        this.value = value;
    }
	
	public String getValue() {
		if(value==10000) {
			return "男";
		}else{
			return "女";
		}
		
	}
	public static void main(String[] args) {
		System.out.println(AuthorEnum.MAN.getValue());
	}
}
