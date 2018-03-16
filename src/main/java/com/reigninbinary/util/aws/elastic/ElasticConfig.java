package com.reigninbinary.util.aws.elastic;

import com.reigninbinary.core.Config;


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
	
		return Config.getConfigParam(HOSTNAME_PARAM, HOSTNAME_DEFAULT);
	}
	
	public static int getPort() {
		
		return Config.getConfigParam(PORT_PARAM, PORT_DEFAULT);
	}
	
	public static String getScheme() {
		
		return Config.getConfigParam(SCHEME_PARAM, SCHEME_DEFAULT);
	}
	
	public static int getMaxRetryTimeoutMillis() {
		
		return Config.getConfigParam(MAX_RETRYTIMEOUTMILLS_PARAM, MAX_RETRYTIMEOUTMILLS_DEFAULT);
	}
}
