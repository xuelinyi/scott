package com.comverse.timesheet.web;

public enum SystemEnum {
	DEBUG(0x10,"调试"),INFO (0x20,"信息"),WARN (0x30,"告警"),
	ERROR (0x40,"错误"),SUCCESS (0x50,"成功"),FAIL (0x60,"失败");
	
	private int logFlag;
	private String value;
	public int getLogFlag() {
		return logFlag;
	}

	public void setLogFlag(int logFlag) {
		this.logFlag = logFlag;
	}

	public void setValue(String value) {
		this.value = value;
	}

	private SystemEnum(int logFlag,String value) {
        this.logFlag = logFlag;
        this.value = value;
    }
	
	public static String getValue(int logFlag) {
		for (SystemEnum c : SystemEnum.values()) {  
            if (c.getLogFlag() == logFlag) {  
                return c.value;  
            }  
        }  
        return null;  
		
	}
	public static void main(String[] args) {
		System.out.println(SystemEnum.DEBUG.logFlag);
	}
}
