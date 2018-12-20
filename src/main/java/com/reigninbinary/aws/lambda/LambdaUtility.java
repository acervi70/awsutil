package com.reigninbinary.aws.lambda;

import com.amazonaws.HttpMethod;
import com.reigninbinary.core.CoreConfig;

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
		
		// allows us to use config file for params as well.
		return CoreConfig.getConfigParam(paramName, null);
	}	
}
