package com.reigninbinary.aws.lambda;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.json.simple.JSONObject;


/* Output Format of a Lambda Function for Proxy Integration
 * https://docs.aws.amazon.com/apigateway/latest/developerguide/set-up-lambda-proxy-integrations.html
 * Headers are optional.
 * Body is user supplied return data.
 * 
	{
	    "isBase64Encoded": true|false,
	    "statusCode": httpStatusCode,
	    "headers": { "headerName": "headerValue", ... },
	    "body": "..."
	}
*/

public class LambdaProxyOutput {

	public static final String BODY = "body";

	protected static final String CHARSET_ENCODING = "UTF-8";

	protected static final String STATUS_CODE = "statusCode";
	protected static final String BASE64ENCODED = "isBase64Encoded";
	protected static final String EXCEPTION = "exception";
	
	protected static final int STATUS_CODE_OK = 200;
	protected static final int STATUS_CODE_ERROR = 500;
	
	public static String LOG_MSG = "LambdaProxyOutput = %s";
	
	protected JSONObject jsonResponse;
				
	public LambdaProxyOutput(JSONObject jsonBody) {

		this(jsonBody, STATUS_CODE_OK);		
	}
	
	public LambdaProxyOutput(JSONObject jsonBody, int statusCode) {
		
		createResponse(jsonBody, statusCode);		
	}
	
	public LambdaProxyOutput(Exception e)  {
		this(e, STATUS_CODE_ERROR);		
	}
	
	public LambdaProxyOutput(Exception e, int statusCode) {
		
		createResponse(getErrorBody(e, statusCode), statusCode);		
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
	private JSONObject getErrorBody(Exception e, int statusCode) {
		
		JSONObject jsonBody = new JSONObject();
		jsonBody.put(EXCEPTION, e.getMessage());
		jsonBody.put(STATUS_CODE, statusCode);
		return jsonBody;
	}
	
	// unchecked due to warnings on jsonObject.put()
	@SuppressWarnings("unchecked")
	private void createResponse(JSONObject jsonBody, int statusCode) {
		
		// we want the whole json object in the response.
		// lambda proxy only returns the body text.
		jsonResponse = new JSONObject();
		jsonResponse.put(BASE64ENCODED, false);
		jsonResponse.put(BODY, jsonBody.toJSONString());
		jsonResponse.put(STATUS_CODE, statusCode);
	}
}
