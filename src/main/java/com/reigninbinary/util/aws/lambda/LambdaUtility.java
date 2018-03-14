package com.reigninbinary.util.aws.lambda;

import com.amazonaws.HttpMethod;

public class LambdaUtility {
	
    public static HttpMethod getHttpMethod(String httpMethod) {
    	
    		if (httpMethod != null) {
	    		for (HttpMethod method : HttpMethod.values()) {
	    			if (method.toString().equals(httpMethod.toUpperCase())) {
	    				return method;
	    			}
	    		}
    		}
    		return null;
    	}

	public static String getLambdaEnvParam(String paramName) {
		
		return System.getenv(paramName);
	}	
}