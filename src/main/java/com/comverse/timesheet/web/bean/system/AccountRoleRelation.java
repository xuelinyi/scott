package com.comverse.timesheet.web.bean.system;

public class AccountRoleRelation {
	private int id;
	private Account account;
	private Role role;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AccountRoleRelation [id=");
		builder.append(id);
		builder.append(", account=");
		builder.append(account);
		builder.append(", role=");
		builder.append(role);
		builder.append("]");
		return builder.toString();
	}
	public AccountRoleRelation(){
		
	}
	public AccountRoleRelation(Account account,Role role) {
		this.account = account;
		this.role = role;
	}
}
