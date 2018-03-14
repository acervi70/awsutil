package com.reigninbinary.util.aws.lambda;

public interface LambdaProxyHandlerFactory {

	public LambdaProxyHandler getHandler(LambdaProxyInput input) throws Exception;
}
