package com.reigninbinary.util.aws.dynamodb;

import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper.FailedBatch;
import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;

public class DynamodbManager {

	private static final String RETRY_ERROR = "unable to update/retry the entire list of failed batches";

	private static volatile DynamodbManager instance;

    private DynamoDB dynamodb;
    private DynamoDBMapper dynamodbMapper;

    private DynamodbManager() {
    	
    		AmazonDynamoDB client;
    		
    		String region = DynamodbConfig.getDynamodbRegion();
    		if (region == null || region.isEmpty()) {
    			client = AmazonDynamoDBClientBuilder.standard().build();
    		}
    		else {
    			client = AmazonDynamoDBClientBuilder.standard().withRegion(region).build();
    		}
    		
    		dynamodb = new DynamoDB(client);
    		dynamodbMapper = new DynamoDBMapper(client);
    }

    private static DynamodbManager instance() {

        if (instance == null) {
            synchronized(DynamodbManager.class) {
                if (instance == null)
                    instance = new DynamodbManager();
            }
        }
        return instance;
    }

    public static DynamoDB dynamodb() {

        DynamodbManager manager = instance();
        return manager.dynamodb;
    }   

    public static DynamoDBMapper mapper() {

        DynamodbManager manager = instance();
        return manager.dynamodbMapper;
    }

	public static void handleUnprocessedItems(List<FailedBatch> listFailedBatch) throws Exception{
		
		for (FailedBatch failed : listFailedBatch) {
			handleUnprocessedItems(failed);
		}
	}

	public static void handleUnprocessedItems(FailedBatch failedBatch) throws Exception{
		
		int maxRetries = DynamodbConfig.getMaxRetryUnprocessedItems();
		
		int attempts = 0;
		
		Map<String, List<WriteRequest>> mapUnprocessed = failedBatch.getUnprocessedItems();	
		
		while (mapUnprocessed.size() > 0 && attempts++ < maxRetries) {
			
			// https://github.com/aws/aws-sdk-java/blob/master/src/samples/AmazonDynamoDBDocumentAPI/quick-start/com/amazonaws/services/dynamodbv2/document/quickstart/I_BatchWriteItemTest.java
            // exponential backoff per DynamoDB recommendation.
            Thread.sleep((1 << attempts) * 1000);
			
			BatchWriteItemOutcome outcome = 
					DynamodbManager.dynamodb().batchWriteItemUnprocessed(mapUnprocessed);	
			
			mapUnprocessed = outcome.getUnprocessedItems();
		}
		
		if (mapUnprocessed.size() > 0 ) {
			throw new Exception(RETRY_ERROR);
		}
	}
}
