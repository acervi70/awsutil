package com.reigninbinary.aws.ses;

import com.reigninbinary.core.util.CoreConfig;

public class SesEnv {

	private static final String REGION = "SES_REGION";
	private static final String REGION_DEFAULT = null;

	private static final String EMAILADDRESS_TO_DEBUG = "SES_EMAILADDRESS_TO_DEBUG";
	private static final String EMAILADDRESS_TO_DEBUG_DEFAULT = null;

	private static final String EMAILADDRESS_FROM = "SES_EMAILADDRESS_FROM";
	private static final String EMAILADDRESS_FROM_DEFAULT = "";

	private static final String EMAILADDRESS_FROM_NAME = "SES_EMAILADDRESS_FROM_NAME";
	private static final String EMAILADDRESS_FROM_NAME_DEFAULT = "";

	private static final String CONFIGSET = "SES_CONFIGSET";
	private static final String CONFIGSET_DEFAULT = "";

	private static final String EMAILATTACHMENTS_ENABLED = "SES_EMAILATTACHMENTS_ENABLED";
	private static final boolean EMAILATTACHMENTS_ENABLED_DEFAULT = true;

	private static final String EMAILATTACHMENTS_S3BUCKET = "SES_EMAILATTACHMENTS_S3BUCKET";
	private static final String EMAILATTACHMENTS_S3BUCKET_DEFAULT = "";

	private static final String EMAILATTACHMENTS_S3DIRECTORY = "SES_EMAILATTACHMENTS_S3DIRECTORY";
	private static final String EMAILATTACHMENTS_S3DIRECTORY_DEFAULT = "";

	private static final String EMAILATTACMENTS_CACHETIMEOUT_MINUTES = "SES_EMAILATTACHMENTS_CACHETIMEOUT_MINUTES";
	private static final int EMAILATTACMENTS_CACHETIMEOUT_MINUTES_DEFAULT = 60;

	public static String getSesRegion() {

		String region = CoreConfig.getConfigParam(REGION, REGION_DEFAULT);
		return region;
	}

	public static String getFromEmailAddress() {

		String fromAddress = CoreConfig.getConfigParam(EMAILADDRESS_FROM, EMAILADDRESS_FROM_DEFAULT);
		return fromAddress;
	}

	public static String getFromName() {

		String fromAddress = CoreConfig.getConfigParam(EMAILADDRESS_FROM_NAME, EMAILADDRESS_FROM_NAME_DEFAULT);
		return fromAddress;
	}

	public static String getToEmailAddressDebug() {

		String toAddress = CoreConfig.getConfigParam(EMAILADDRESS_TO_DEBUG, EMAILADDRESS_TO_DEBUG_DEFAULT);
		return toAddress;
	}

	public static String getConfigSet() {

		String configset = CoreConfig.getConfigParam(CONFIGSET, CONFIGSET_DEFAULT);
		return configset;
	}

	public static boolean isEmailAttachmentsEnabled() {

		return CoreConfig.getConfigParam(EMAILATTACHMENTS_ENABLED, EMAILATTACHMENTS_ENABLED_DEFAULT);
	}

	public static String getEmailAttachmentsS3Bucket() {

		String bucket = CoreConfig.getConfigParam(EMAILATTACHMENTS_S3BUCKET, EMAILATTACHMENTS_S3BUCKET_DEFAULT);
		return bucket;
	}

	public static String getEmailAttachmentsS3Directory() {

		String directory = CoreConfig.getConfigParam(EMAILATTACHMENTS_S3DIRECTORY, EMAILATTACHMENTS_S3DIRECTORY_DEFAULT);
		return directory;
	}

	public static int getEmailAttachmentsCacheTimeoutMinutes() {

		return CoreConfig.getConfigParam(EMAILATTACMENTS_CACHETIMEOUT_MINUTES, EMAILATTACMENTS_CACHETIMEOUT_MINUTES_DEFAULT);
	}
}
