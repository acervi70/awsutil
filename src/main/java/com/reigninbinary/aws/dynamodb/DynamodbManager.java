package com.reigninbinary.aws.dynamodb;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

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
	
    private static class DynamodbManagerInstance {
    	
        private static final DynamodbManager INSTANCE = new DynamodbManager();
    }
    
	private final AmazonDynamoDB client;
	
    private DynamodbManager() {
    	
		if (DynamodbConfig.getRunLocal()) {    			
			EndpointConfiguration endpoint = new AwsClientBuilder.EndpointConfiguration(
					DynamodbConfig.getRunLocalEndpoint(), DynamodbConfig.getRunLocalRegion());    			
			client = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(endpoint).build();    			    			
		}
		else {
			String region = DynamodbConfig.getRegion();
			if (StringUtils.isNotEmpty(region)) {
    			client = AmazonDynamoDBClientBuilder.standard().withRegion(region).build();
    		}
    		else {
    			client = AmazonDynamoDBClientBuilder.standard().build();
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

	// https://github.com/aws/aws-sdk-java/blob/master/src/samples/AmazonDynamoDBDocumentAPI/quick-start/com/amazonaws/services/dynamodbv2/document/quickstart/I_BatchWriteItemTest.java
	public static Map<String, List<WriteRequest>> handleUnprocessedItems(FailedBatch failedBatch) throws InterruptedException {
		
		Map<String, List<WriteRequest>> mapUnprocessed = failedBatch.getUnprocessedItems();
		
		for (int attempts = 0; 
				mapUnprocessed.size() > 0 && 
				attempts++ < DynamodbConfig.getMaxRetryUnprocessedItems();) {
			
            // exponential backoff per Amazon recommendation.
			Thread.sleep((1 << attempts) * 1000);
			
			BatchWriteItemOutcome outcome = dynamodb().batchWriteItemUnprocessed(mapUnprocessed);				
			mapUnprocessed = outcome.getUnprocessedItems();
		}
		
		return mapUnprocessed;
	}
	
    /*
	public static Map<String, List<WriteRequest>> handleUnprocessedItems(List<FailedBatch> listFailedBatch) throws Exception{
		
		Map<String, List<WriteRequest>> mapUnprocessed = new HashMap<>();
		
		for (FailedBatch failed : listFailedBatch) {
			// TODO: Make sure the key value doesn't overlap/collide across  
			// FailedBatch collections otherwise we might overwrite an item.
			// For now, we'll just let the clients figure out how to handle this.
			mapUnprocessed.putAll(handleUnprocessedItems(failed));
		}
		
		return mapUnprocessed;
	}
	*/
}
