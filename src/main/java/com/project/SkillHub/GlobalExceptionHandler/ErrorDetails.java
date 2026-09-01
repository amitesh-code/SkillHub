package com.project.SkillHub.GlobalExceptionHandler;

import java.time.LocalDateTime;

public class ErrorDetails {

	private String statuscode;
	private String msg;
	private LocalDateTime local;
	public String getStatuscode() {
		return statuscode;
	}
	public void setStatuscode(String statuscode) {
		this.statuscode = statuscode;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public ErrorDetails(String statuscode, String msg, LocalDateTime local) {
		super();
		this.statuscode = statuscode;
		this.msg = msg;
		this.local = local;
	}
	public LocalDateTime getLocal() {
		return local;
	}
	public void setLocal(LocalDateTime local) {
		this.local = local;
	}
	public ErrorDetails() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
