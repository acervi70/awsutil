package com.reigninbinary.aws.lambda;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.json.simple.JSONObject;


/* Output Format of a Lambda Function for Proxy Integration
 * https://docs.aws.amazon.com/apigateway/latest/developerguide/set-up-lambda-proxy-integrations.html
 * headers are optional.
 * body is user supplied return data.
 * status code is normally http status code
 * isBase64Encoded is assumed false for the time being ***
 * 
	{
	    "isBase64Encoded": true|false,
	    "statusCode": httpStatusCode,
	    "headers": { "headerName": "headerValue", ... },
	    "body": "..."
	}
*/

public class LambdaProxyOutput {

	private static final String CHARSET_ENCODING = "UTF-8";

	private static final String BODY = "body";
	private static final String STATUS_CODE = "statusCode";
	private static final String EXCEPTION = "exception";
	
	private static final String ISBASE64ENCODED = "isBase64Encoded";
	private boolean base64Encoded = false;

	private static final int STATUS_CODE_OK = 200;
	private static final int STATUS_CODE_ERROR = 500;
	
	private static String LOG_MSG = "LambdaProxyOutput = %s";
	
	private JSONObject jsonResponse;
				
	public LambdaProxyOutput(JSONObject jsonResponseBody) {

		this(jsonResponseBody, STATUS_CODE_OK);		
	}
	
	public LambdaProxyOutput(JSONObject jsonResponseBody, int statusCode) {
		
		createResponse(jsonResponseBody, statusCode);		
	}
	
	public LambdaProxyOutput(String jsonResponseBody) {

		this(jsonResponseBody, STATUS_CODE_OK);		
	}
	
	public LambdaProxyOutput(String jsonResponseBody, int statusCode) {
		
		createResponse(jsonResponseBody, statusCode);		
	}
	
	public LambdaProxyOutput(Exception e)  {
		this(e, STATUS_CODE_ERROR);		
	}
	
	public LambdaProxyOutput(Exception e, int statusCode) {
		
		createResponse(getErrorBody(e, statusCode), statusCode);		
	}
	
	public boolean isBase64Encoded() {
		return base64Encoded;
	}

	public void setBase64Encoded(boolean base64Encoded) {
		this.base64Encoded = base64Encoded;
	}

	public String getAsJsonString() {
		
		return jsonResponse.toJSONString();
	}
	
	public String getLogMessage() {
		
		return String.format(LOG_MSG, getAsJsonString());
	}
	
	public void writeToOutputStream(OutputStream outputStream) throws IOException {
		
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, CHARSET_ENCODING);
        writer.write(jsonResponse.toJSONString());  
        writer.close();
	}
	
	// unchecked due to warnings on jsonObject.put()
	@SuppressWarnings("unchecked")
	private JSONObject getErrorBody(Exception e, int statusCode) {
		
		JSONObject jsonBody = new JSONObject();
		jsonBody.put(EXCEPTION, e.getMessage());
		jsonBody.put(STATUS_CODE, statusCode);
		return jsonBody;
	}
	
	private void createResponse(JSONObject jsonBody, int statusCode) {
		
		createResponse(jsonBody.toJSONString(), statusCode);
	}
	
	// unchecked due to warnings on jsonObject.put()
	@SuppressWarnings("unchecked")
	private void createResponse(String jsonBody, int statusCode) {
		
		// we want the whole json object in the response.
		// lambda proxy only returns the body text.
		
		jsonResponse = new JSONObject();
		jsonResponse.put(ISBASE64ENCODED, base64Encoded);
		jsonResponse.put(BODY, jsonBody);
		jsonResponse.put(STATUS_CODE, statusCode);
	}
}
