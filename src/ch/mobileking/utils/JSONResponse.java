package ch.mobileking.utils;

import com.google.gson.annotations.SerializedName;

public class JSONResponse {
	
	private static final long serialVersionUID = 1L;
	
	@SerializedName("status")
	private String status;
	
	@SerializedName("exception")
	private String exception;
	
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}
	

}
