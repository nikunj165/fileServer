package org.adobe.initiation;

public enum StatusCode {
	OK("200 OK"), NOT_FOUND("404 NOT FOUND"), SERVER_ERROR(
			"500 INTERNAL SERVER ERROR");

	StatusCode(String code) {
		this.code = code;
	}
	private String code;
	public String getCode() {
		return this.code;
	}
}
