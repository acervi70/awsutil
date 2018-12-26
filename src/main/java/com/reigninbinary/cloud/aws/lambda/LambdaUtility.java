package com.reigninbinary.cloud.aws.lambda;

import org.apache.commons.lang3.StringUtils;

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
		
		return CoreConfig.getConfigParam(paramName, StringUtils.EMPTY);
	}	
}
