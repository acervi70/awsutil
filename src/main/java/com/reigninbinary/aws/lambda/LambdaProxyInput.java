package com.reigninbinary.aws.lambda;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/* Input Format of a Lambda Function for Proxy Integration
 * https://docs.aws.amazon.com/apigateway/latest/developerguide/set-up-lambda-proxy-integrations.html
 * 
	{
	    "path": "/",
	    "headers": null,
	    "pathParameters": null,
	    "isBase64Encoded": false,
	    "requestContext": {
	        "path": "/",
	        "accountId": "985731327228",
	        "resourceId": "dm2acuhtp2",
	        "stage": "test-invoke-stage",
	        "requestId": "test-invoke-request",
	        "identity": {
	            "cognitoIdentityPoolId": null,
	            "cognitoIdentityId": null,
	            "apiKey": "test-invoke-api-key",
	            "cognitoAuthenticationType": null,
	            "userArn": "arn:aws:iam::985731327228:user/acervi",
	            "apiKeyId": "test-invoke-api-key-id",
	            "userAgent": "Apache-HttpClient/4.5.x (Java/1.8.0_144)",
	            "accountId": "985731327228",
	            "caller": "AIDAIKV4XWCQAIR6TMUFQ",
	            "sourceIp": "test-invoke-source-ip",
	            "accessKey": "ASIAJ5CZV2BRR265VWSA",
	            "cognitoAuthenticationProvider": null,
	            "user": "AIDAIKV4XWCQAIR6TMUFQ"
	        },
	        "resourcePath": "/",
	        "httpMethod": "POST",
	        "apiId": "ml3clq0c5f"
	    },
	    "resource": "/",
	    "httpMethod": "POST",
	    "queryStringParameters": {
	        "vin": "1C3CDFBB1GD593541"
	    },
	    "stageVariables": null,
	    "body": null
	}
*/

public class LambdaProxyInput {
	
	private static final String BODY = "body";
	private static final String RESOURCE = "resource";
	private static final String HTTP_METHOD = "httpMethod";
	private static final String PATH_PARAMS = "pathParameters";
	private static final String QUERYSTRING_PARAMS = "queryStringParameters";		
	
	private static String LOG_MSG = "LambdaProxyInput = %s";

	// Object representation of JSON string input.
	private JSONObject jsonLambdaEvent;
	
	
	public LambdaProxyInput(InputStream inputStream) throws IOException, ParseException {
		
		getInputStreamAsJsonObject(inputStream);
	}
	
	public JSONObject getLambdaEvent() {
		
		return jsonLambdaEvent;
	}
	
	public String getLambdaEventAsJsonString() {
		
		return jsonLambdaEvent.toJSONString();
	}
	
	public String getLogMessage() {
		
		return String.format(LOG_MSG, getLambdaEventAsJsonString());
	}
	
	public String getResource() {
		
		String resource = (String) jsonLambdaEvent.get(RESOURCE);
		return resource;
	}
	
	public String getHttpMethod() {
		
		String httpMethod = (String) jsonLambdaEvent.get(HTTP_METHOD);
		if (httpMethod == null || httpMethod.isEmpty()) {
			return httpMethod;
		}		
		return httpMethod.toUpperCase();
	}
	
	public String getBody() throws Exception {
		
		return (String) jsonLambdaEvent.get(BODY);
	}
			
	public String getQueryParam(String paramName) {
		
	    JSONObject qsp = (JSONObject) jsonLambdaEvent.get(QUERYSTRING_PARAMS);
	    if (qsp == null) {
        		return null;
        }
        return (String) qsp.get(paramName);        
	}
	
	public String getPathParam(String paramName) {
		
	    JSONObject pp = (JSONObject) jsonLambdaEvent.get(PATH_PARAMS);
	    if (pp == null) {
        		return null;
        }
        return (String) pp.get(paramName);        
	}
	
	private void getInputStreamAsJsonObject(InputStream inputStream) throws IOException, ParseException {
    		
    		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    		try {
    			jsonLambdaEvent = (JSONObject) new JSONParser().parse(reader);
    		} 
    		finally {
    			if (reader != null) {
    				reader.close();
    			}
    		}
    	}
}
