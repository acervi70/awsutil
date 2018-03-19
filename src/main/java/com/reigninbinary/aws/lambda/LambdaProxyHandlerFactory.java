package com.reigninbinary.aws.lambda;

public interface LambdaProxyHandlerFactory {

	public LambdaProxyHandler getHandler(LambdaProxyInput input) throws Exception;
}
