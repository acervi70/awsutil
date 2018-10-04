package com.reigninbinary.aws.util;

public class AwsUtilException extends Exception {

	private static final long serialVersionUID = 8929047622965920227L;

	public AwsUtilException() {
	}

	public AwsUtilException(String arg0) {
		super(arg0);
	}

	public AwsUtilException(Throwable arg0) {
		super(arg0);
	}

	public AwsUtilException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public AwsUtilException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}
}
