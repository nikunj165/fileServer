package org.adobe.initiation;

public enum ContentType {
	TEXT("text/plain"), HTML("text/html");

	ContentType(String type) {
		this.contentType = type;
	}
	private String contentType;
	public String getContentType() {
		return contentType;
	}
}
