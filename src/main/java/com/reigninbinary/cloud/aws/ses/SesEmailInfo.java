package com.reigninbinary.cloud.aws.ses;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

// see mailchimp project readme.txt and MergeFields class.

public class SesEmailInfo {

	private String emailAddress;
	private String[] ccAddresses = null;
	private String[] bccAddresses = null;
	private String attachmentDirectory = null;
	private String attachmentFilename = null;
	private Map<String, Object> mapMergeFields = new HashMap<>();
	
	public SesEmailInfo(String emailAddress) {
		
		this.emailAddress = emailAddress;
	}

	public String getEmailAddress() {
		
		return emailAddress;
	}
	
	public String[] getCcAddresses() {
		return ccAddresses;
	}

	public void setCcAddresses(String[] ccAddresses) {
		this.ccAddresses = ccAddresses;
	}

	public String[] getBccAddresses() {
		return bccAddresses;
	}

	public void setBccAddresses(String[] bccAddresses) {
		this.bccAddresses = bccAddresses;
	}

	public Map<String, Object> getMergeFields() {
		
		return mapMergeFields;
	}

	public String getAttachmentDirectory() {
		return attachmentDirectory;
	}

	public void setAttachmentDirectory(String attachmentDirectory) {
		this.attachmentDirectory = attachmentDirectory;
	}

	public String getAttachmentFilename() {
		return attachmentFilename;
	}

	public void setAttachmentFilename(String attachmentFilename) {
		this.attachmentFilename = attachmentFilename;
	}

	public void addMergeField(String key, Object value) {
		
		mapMergeFields.put(key, value);
	}
	
	public void addMergeField(String key, String value) {
		
		mapMergeFields.put(key, value);
	}
	
	public void addMergeField(String key, JSONObject value) {
		
		mapMergeFields.put(key, value);
	}
	
	public String toJsonString() {
		
		return JSONObject.toJSONString(getMergeFields());
	}
}