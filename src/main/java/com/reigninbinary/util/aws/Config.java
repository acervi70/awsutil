package com.reigninbinary.util.aws;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

	private static final String CONFIG_FILE_ENVPARAM = "AWS_CONFIG_FILE";
	private static final String CONFIG_FILE_DEFAULT = "awsconfig.properties";
	
	private static final String NOFILE_ERROR = "Unable to load properties file: %s";
	
	private static Properties properties = new Properties();
	
	public static Properties properties() {
		
		return properties;
	}
	
	static {
	
		String configFile = getConfigFile();

		InputStream inputStream = null;
		try {

			inputStream = Config.class.getClassLoader().getResourceAsStream(configFile);
			if (inputStream == null) {
		        System.err.println(String.format(NOFILE_ERROR, configFile));
			}
			else {
				properties.load(inputStream);
			}
		}
		catch (IOException e) {
	        System.err.println(new Exception(String.format(NOFILE_ERROR, configFile), e).getMessage());
		}
		finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
		
	private static String getConfigFile() {
		
		String configFile = System.getenv(CONFIG_FILE_ENVPARAM);
		if (configFile == null || configFile.isEmpty()) {
			configFile = CONFIG_FILE_DEFAULT;
		}
		return configFile;
	}
}

