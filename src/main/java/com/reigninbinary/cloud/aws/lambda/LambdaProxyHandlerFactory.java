package com.reigninbinary.cloud.aws.lambda;

public interface LambdaProxyHandlerFactory {

	public LambdaProxyHandler getHandler(LambdaProxyInput input) throws Exception;
}
