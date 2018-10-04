package com.reigninbinary.aws.s3;

import com.reigninbinary.core.util.CoreConfig;

public class S3Env {

	private static final String S3_REGION = "S3_REGION";
	private static final String S3_REGION_DEFAULT = null;
	
	public static String getRegion() {
		
		return CoreConfig.getConfigParam(S3_REGION, S3_REGION_DEFAULT);
	}
}
