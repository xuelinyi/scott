package com.comverse.timesheet.web.bean;

public class Rule {
	private int montch;
	private int money;
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Rule [montch=");
		builder.append(montch);
		builder.append(", money=");
		builder.append(money);
		builder.append("]");
		return builder.toString();
	}
	public int getMontch() {
		return montch;
	}
	public void setMontch(int montch) {
		this.montch = montch;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	
}
