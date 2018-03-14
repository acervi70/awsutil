package com.reigninbinary.util.aws.lambda;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.json.simple.JSONObject;


/* Output Format of a Lambda Function for Proxy Integration
 * https://docs.aws.amazon.com/apigateway/latest/developerguide/set-up-lambda-proxy-integrations.html
 * Headers are optional.
 * 
	{
	    "isBase64Encoded": true|false,
	    "statusCode": httpStatusCode,
	    "headers": { "headerName": "headerValue", ... },
	    "body": "..."
	}
*/

public class LambdaProxyOutput {

	protected static final String CHARSET_ENCODING = "UTF-8";

	protected static final String STATUS_CODE = "statusCode";
	protected static final String BASE64ENCODED = "isBase64Encoded";
	protected static final String EXCEPTION = "exception";
	
	protected static final int STATUS_CODE_OK = 200;
	protected static final int STATUS_CODE_ERROR = 500;
	
	public static String LOG_MSG = "LambdaProxyOutput = %s";
	
	protected JSONObject jsonResponse;
			
	protected LambdaProxyOutput() {
		
	}
	
	public LambdaProxyOutput(Exception e) {
		
		createResponse(getBody(e));		
	}
	
	public String getAsJsonString() {
		
		return jsonResponse.toJSONString();
	}
	
	public String getLogMessage() {
		
		return String.format(LOG_MSG, getAsJsonString());
	}
	
	public void writeToOutputStream(OutputStream outputStream) throws IOException {
		
        // An exception here would indicate a failure to write to the output stream
        // which is a very bad thing ;) The client won't get an indication of the result
        // irregardless how hard we try so let exception bubble up to the infrastructure.
        
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, CHARSET_ENCODING);
        writer.write(jsonResponse.toJSONString());  
        writer.close();
	}
	
	// unchecked due to warnings on jsonObject.put()
	@SuppressWarnings("unchecked")
	private JSONObject getBody(Exception e) {
				
		JSONObject jsonBody = new JSONObject();
		jsonBody.put(EXCEPTION, e.getMessage());
		jsonBody.put(STATUS_CODE, STATUS_CODE_ERROR);
		return jsonBody;
	}
	
	// unchecked due to warnings on jsonObject.put()
	@SuppressWarnings("unchecked")
	protected void createResponse(JSONObject jsonBody) {
		
		// we want the whole json object in the response.
		// lambda proxy only returns the body text.
		jsonResponse = new JSONObject();
		jsonResponse.put(BASE64ENCODED, false);
		jsonResponse.put(LambdaProxyInput.BODY, jsonBody.toJSONString());
		jsonResponse.put(STATUS_CODE, jsonBody.get(STATUS_CODE));
	}
}
