package com.reigninbinary.aws.dynamodb;

import java.util.List;
import java.util.Map;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper.FailedBatch;
import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;


public class DynamodbManager {
	
	private final AmazonDynamoDB client;
	
    private static class DynamodbManagerInstance {
    	
        private static final DynamodbManager INSTANCE = new DynamodbManager();
    }
    
    private DynamodbManager() {
    	
		if (DynamodbConfig.getRunLocal()) {    			
			EndpointConfiguration endpoint = new AwsClientBuilder.EndpointConfiguration(
					DynamodbConfig.getRunLocalEndpoint(), DynamodbConfig.getRunLocalRegion());    			
			client = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(endpoint).build();    			    			
		}
		else {
			String region = DynamodbConfig.getRegion();
	    		if (region == null || region.isEmpty()) {
	    			client = AmazonDynamoDBClientBuilder.standard().build();
	    		}
	    		else {
	    			client = AmazonDynamoDBClientBuilder.standard().withRegion(region).build();
	    		}
		}
    }
    
    private static DynamodbManager getInstance() {
    	
        return DynamodbManagerInstance.INSTANCE;
    }
 
    public static DynamoDB dynamodb() {

		return new DynamoDB(getInstance().client);
    }

    public static DynamoDBMapper mapper() {

		return new DynamoDBMapper(getInstance().client);
    }

	public static void handleUnprocessedItems(List<FailedBatch> listFailedBatch) throws Exception{
		
		for (FailedBatch failed : listFailedBatch) {
			handleUnprocessedItems(failed);
		}
	}

	public static void handleUnprocessedItems(FailedBatch failedBatch) throws Exception{
		
		Map<String, List<WriteRequest>> mapUnprocessed = failedBatch.getUnprocessedItems();
		
		for (int attempts = 0; mapUnprocessed.size() > 0 
				&& attempts++ < DynamodbConfig.getMaxRetryUnprocessedItems();) {
			
			// https://github.com/aws/aws-sdk-java/blob/master/src/samples/AmazonDynamoDBDocumentAPI/quick-start/com/amazonaws/services/dynamodbv2/document/quickstart/I_BatchWriteItemTest.java
            // exponential backoff per DynamoDB recommendation.
            Thread.sleep((1 << attempts) * 1000);
			
			BatchWriteItemOutcome outcome = dynamodb().batchWriteItemUnprocessed(mapUnprocessed);				
			mapUnprocessed = outcome.getUnprocessedItems();
		}
		
		if (mapUnprocessed.size() > 0 ) {
			final String RETRY_ERROR = "unable to update/retry the entire list of failed batches";
			throw new Exception(RETRY_ERROR);
		}
	}
}
