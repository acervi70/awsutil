package com.reigninbinary.util.aws.dynamodb;

public class DynamodbEnv {

	private static final String REGION_ENVPARAM = "DYNAMODB_REGION";
	
	public static final String MAX_BATCHWRITEITEM_ENVPARAM = "DYNAMODB_MAX_BATCHWRITEITEM";
	public static final int MAX_BATCHWRITEITEM_DEFAULT = 25;

	public static final String MAX_RETRYUNPROCESSEDITEMS_ENVPARAM = "DYNAMODB_MAX_RETRYUNPROCESSEDITEMS";
	public static final int MAX_RETRYUNPROCESSEDITEMS_DEFAULT = 5;


	public static String getDynamodbRegion() {
		
		// Leaving this envparam undefined will result in:
		// AmazonDynamoDBClientBuilder.standard().build();
		
		return getSystemEnvParam(REGION_ENVPARAM);		
	}
	
	public static int getMaxBatchWriteItem() {
		
		String batchSize = getSystemEnvParam(MAX_BATCHWRITEITEM_ENVPARAM);		
		if (batchSize == null || batchSize.isEmpty()) {
			return MAX_BATCHWRITEITEM_DEFAULT;
		}
		return Integer.parseInt(batchSize);
	}
	
	public static int getMaxRetryUnprocessedItems() {
		
		String maxRetries = getSystemEnvParam(MAX_RETRYUNPROCESSEDITEMS_ENVPARAM);		
		if (maxRetries == null || maxRetries.isEmpty()) {
			return MAX_RETRYUNPROCESSEDITEMS_DEFAULT;
		}
		return Integer.parseInt(maxRetries);
	}
		
	private static String getSystemEnvParam(String envParamName) {
		
		return System.getenv(envParamName);
	}
}
