package com.reigninbinary.cloud.aws.lambda;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.reigninbinary.core.CoreLogging;

public class LambdaProxyHandlerImpl implements RequestStreamHandler {
	
	private LambdaProxyHandlerFactory lambdaProxyHandlerFactory;
	
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
			CoreLogging.logInfo(input.getLogMessage());
			
			LambdaProxyHandler handler = lambdaProxyHandlerFactory.getHandler(input);
			output = handler.handleRequest(input, context);
		} 
		catch (Exception e) {
			output = new LambdaProxyOutput(e);
		}
		
		CoreLogging.logInfo(output.getLogMessage());
		
		output.writeToOutputStream(outputStream);
    }

}
