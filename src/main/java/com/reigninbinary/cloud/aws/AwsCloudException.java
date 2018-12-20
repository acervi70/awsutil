package com.reigninbinary.cloud.aws;

public class AwsCloudException extends Exception {

	private static final long serialVersionUID = 8929047622965920227L;

	public AwsCloudException() {
	}

	public AwsCloudException(String arg0) {
		super(arg0);
	}

	public AwsCloudException(Throwable arg0) {
		super(arg0);
	}

	public AwsCloudException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public AwsCloudException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}
}
