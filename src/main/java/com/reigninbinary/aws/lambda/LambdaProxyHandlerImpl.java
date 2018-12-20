package com.reigninbinary.aws.lambda;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

public class LambdaProxyHandlerImpl implements RequestStreamHandler {
	
	LambdaProxyHandlerFactory lambdaProxyHandlerFactory;
	
	public LambdaProxyHandlerImpl() {}
	
	public LambdaProxyHandlerImpl(LambdaProxyHandlerFactory lamdaProxyHandlerFactory) {
		
		setHandlerFactory(lamdaProxyHandlerFactory);
	}
	
	public void setHandlerFactory(LambdaProxyHandlerFactory lamdaProxyHandlerFactory) {
		
		this.lambdaProxyHandlerFactory = lamdaProxyHandlerFactory;
	}

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {

		LambdaProxyOutput output;
		try {
			LambdaProxyInput input = new LambdaProxyInput(inputStream);
			context.getLogger().log(input.getLogMessage());
			
			LambdaProxyHandler handler = lambdaProxyHandlerFactory.getHandler(input);
			output = handler.handleRequest(input, context);
		} 
		catch (Exception e) {
			output = new LambdaProxyOutput(e);
		}
    		
		context.getLogger().log(output.getLogMessage());
		output.writeToOutputStream(outputStream);
    }

}
