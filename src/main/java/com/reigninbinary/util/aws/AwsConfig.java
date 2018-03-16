package com.reigninbinary.util.aws;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AwsConfig {

	private static final String CONFIG_FILE_ENVPARAM = "AWS_CONFIG_FILE";
	private static final String CONFIG_FILE_DEFAULT = "awsconfig.properties";
	
	private static final String NOFILE_ERROR = "Unable to load properties file: %s";
	
	private static Properties properties = new Properties();
		
	static {
	
		String configFile = getConfigFile();

		InputStream inputStream = null;
		try {

			inputStream = AwsConfig.class.getClassLoader().getResourceAsStream(configFile);
			if (inputStream == null) {
		        throw new Exception(String.format(NOFILE_ERROR, configFile));
			}
			properties.load(inputStream);
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					System.err.println(e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}
	
	public static String getConfigParam(String paramName, String defaultValue) {
		
		String paramValue = getSystemEnvParam(paramName);
		if (paramValue == null || paramValue.isEmpty()) {
			paramValue = getProperty(paramName, defaultValue);
		}
		return paramValue;
	}
	
	public static int getConfigParam(String paramName, int defaultValue) {
		
		String paramValue = getSystemEnvParam(paramName);
		if (paramValue == null || paramValue.isEmpty()) {
			return getProperty(paramName, defaultValue);
		}
		return Integer.parseInt(paramValue);
	}
	
	public static boolean getConfigParam(String paramName, boolean defaultValue) {
		
		String paramValue = getSystemEnvParam(paramName);
		if (paramValue == null || paramValue.isEmpty()) {
			return getProperty(paramName, defaultValue);
		}
		return Boolean.parseBoolean(paramValue);
	}
	
	private static String getProperty(String propertyName, String defaultValue) {
		
		String propertyValue = properties.getProperty(propertyName);
		if (propertyValue != null) {
			return propertyValue;
		}		
		return defaultValue;
	}
		
	private static int getProperty(String propertyName, int defaultValue) {
		
		String propertyValue = properties.getProperty(propertyName);
		if (propertyValue != null) {
			return Integer.parseInt(propertyValue);
		}		
		return defaultValue;
	}
		
	private static boolean getProperty(String propertyName, boolean defaultValue) {
		
		String propertyValue = properties.getProperty(propertyName);
		if (propertyValue != null) {
			return Boolean.parseBoolean(propertyValue);
		}		
		return defaultValue;
	}
		
	private static String getSystemEnvParam(String paramName) {
		
		return System.getenv(paramName);
	}	
		
	private static String getConfigFile() {
		
		String configFile = System.getenv(CONFIG_FILE_ENVPARAM);
		if (configFile == null || configFile.isEmpty()) {
			configFile = CONFIG_FILE_DEFAULT;
		}
		return configFile;
	}
}

