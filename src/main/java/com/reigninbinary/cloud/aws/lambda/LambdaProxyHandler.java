package com.reigninbinary.cloud.aws.lambda;

import com.amazonaws.services.lambda.runtime.Context;


public interface LambdaProxyHandler {

	public LambdaProxyOutput handleRequest(LambdaProxyInput lambdaProxyInput, Context context) throws Exception;
}
