package com.reigninbinary.util.aws.dynamodb;

import com.reigninbinary.util.aws.Config;


public class DynamodbConfig {

	public static final String REGION_PARAM = "DYNAMODB_REGION";
	public static final String REGION_DEFAULT = null;

	public static final String MAX_BATCHWRITEITEM_PARAM = "DYNAMODB_MAX_BATCHWRITEITEM";
	public static final int MAX_BATCHWRITEITEM_DEFAULT = 25;

	public static final String MAX_RETRYUNPROCESSEDITEMS_PARAM = "DYNAMODB_MAX_RETRYUNPROCESSEDITEMS";
	public static final int MAX_RETRYUNPROCESSEDITEMS_DEFAULT = 5;


	public static String getDynamodbRegion() {
	
		String region = getSystemEnvParam(REGION_PARAM);
		if (region == null || region.isEmpty()) {
			region = getProperty(REGION_PARAM, REGION_DEFAULT);	
		}
		return region;
	}
	
	public static int getMaxBatchWriteItem() {
		
		String maxBatch = getSystemEnvParam(MAX_BATCHWRITEITEM_PARAM);
		if (maxBatch == null || maxBatch.isEmpty()) {
			return getProperty(MAX_BATCHWRITEITEM_PARAM, MAX_BATCHWRITEITEM_DEFAULT);	
		}
		return Integer.parseInt(maxBatch);
	}
	
	public static int getMaxRetryUnprocessedItems() {
		
		String maxBatch = getSystemEnvParam(MAX_RETRYUNPROCESSEDITEMS_PARAM);
		if (maxBatch == null || maxBatch.isEmpty()) {
			return getProperty(MAX_RETRYUNPROCESSEDITEMS_PARAM, MAX_RETRYUNPROCESSEDITEMS_DEFAULT);	
		}
		return Integer.parseInt(maxBatch);
	}
	
	private static String getProperty(String propertyName, String defaultValue) {
		
		String propertyValue = Config.properties().getProperty(propertyName);
		if (propertyValue != null) {
			return propertyValue;
		}		
		return defaultValue;
	}
		
	private static int getProperty(String propertyName, int defaultValue) {
		
		String propertyValue = Config.properties().getProperty(propertyName);
		if (propertyValue != null) {
			return Integer.parseInt(propertyValue);
		}		
		return defaultValue;
	}
		
	private static String getSystemEnvParam(String paramName) {
		
		return System.getenv(paramName);
	}	
}
