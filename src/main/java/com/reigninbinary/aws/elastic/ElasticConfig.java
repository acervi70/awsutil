package com.reigninbinary.aws.elastic;

import com.reigninbinary.core.util.CoreConfig;


public class ElasticConfig {

	public static final String HOSTNAME_PARAM = "ELASTIC_HOSTNAME";
	public static final String HOSTNAME_DEFAULT = "localhost";

	public static final String PORT_PARAM = "ELASTIC_PORT";
	public static final int PORT_DEFAULT = 9200;

	public static final String SCHEME_PARAM = "ELASTIC_SCHEME";
	public static final String SCHEME_DEFAULT = "http";
	
	public static final String MAX_RETRYTIMEOUTMILLS_PARAM = "ELASTIC_MAX_RETRYTIMEOUTMILLS";
	public static final int MAX_RETRYTIMEOUTMILLS_DEFAULT = 10000;


	public static String getHostname() {
	
		return CoreConfig.getConfigParam(HOSTNAME_PARAM, HOSTNAME_DEFAULT);
	}
	
	public static int getPort() {
		
		return CoreConfig.getConfigParam(PORT_PARAM, PORT_DEFAULT);
	}
	
	public static String getScheme() {
		
		return CoreConfig.getConfigParam(SCHEME_PARAM, SCHEME_DEFAULT);
	}
	
	public static int getMaxRetryTimeoutMillis() {
		
		return CoreConfig.getConfigParam(MAX_RETRYTIMEOUTMILLS_PARAM, MAX_RETRYTIMEOUTMILLS_DEFAULT);
	}
}
