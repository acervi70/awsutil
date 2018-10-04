package com.reigninbinary.aws.dynamodb;

import com.reigninbinary.core.Config;


public class DynamodbConfig {
	
	private static final String RUNLOCAL_ENDPOINT = "RUNLOCAL_ENDPOINT";
	private static final String RUNLOCAL_ENDPOINT_DEFAULT = "http://localhost:8000";

	private static final String RUNLOCAL_REGION = "RUNLOCAL_REGION";
	private static final String RUNLOCAL_REGION_DEFAULT ="runlocal-region-1";

	private static final String RUNLOCAL_PARAM = "DYNAMODB_RUNLOCAL";
	private static final boolean RUNLOCAL_DEFAULT = false;

	private static final String REGION_PARAM = "DYNAMODB_REGION";
	private static final String REGION_DEFAULT = null;

	private static final String MAX_BATCHWRITEITEM_PARAM = "DYNAMODB_MAX_BATCHWRITEITEM";
	private static final int MAX_BATCHWRITEITEM_DEFAULT = 25; // from AWS code samples

	private static final String MAX_RETRYUNPROCESSEDITEMS_PARAM = "DYNAMODB_MAX_RETRYUNPROCESSEDITEMS";
	private static final int MAX_RETRYUNPROCESSEDITEMS_DEFAULT = 3; // arbitrary default


	public static boolean getRunLocal() {
		
		return Config.getConfigParam(RUNLOCAL_PARAM, RUNLOCAL_DEFAULT);
	}
	
	public static String getRunLocalRegion() {
		
		return Config.getConfigParam(RUNLOCAL_REGION, RUNLOCAL_REGION_DEFAULT);
	}
	
	public static String getRunLocalEndpoint() {
		
		return Config.getConfigParam(RUNLOCAL_ENDPOINT, RUNLOCAL_ENDPOINT_DEFAULT);
	}
	
	public static String getRegion() {
		
		return Config.getConfigParam(REGION_PARAM, REGION_DEFAULT);
	}
	
	public static int getMaxBatchWriteItem() {
		
		return Config.getConfigParam(MAX_BATCHWRITEITEM_PARAM, MAX_BATCHWRITEITEM_DEFAULT);
	}
	
	public static int getMaxRetryUnprocessedItems() {
		
		return Config.getConfigParam(MAX_RETRYUNPROCESSEDITEMS_PARAM, MAX_RETRYUNPROCESSEDITEMS_DEFAULT);
	}
}
