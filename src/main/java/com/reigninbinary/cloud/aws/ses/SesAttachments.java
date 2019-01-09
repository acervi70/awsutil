package com.reigninbinary.cloud.aws.ses;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.reigninbinary.cloud.aws.AwsCloudException;
import com.reigninbinary.cloud.aws.s3.S3Client;


public class SesAttachments {

	private static SesAttachments instance() {

		return SesAttachmentsInstance.INSTANCE;
	}
	
	private static class SesAttachmentsInstance {
		
		private static final SesAttachments INSTANCE = new SesAttachments();
	}

	private final Cache<String, SesAttachment> cache;

	private SesAttachments() {

		int timeout = SesEnv.getEmailAttachmentsCacheTimeoutMinutes();
		cache = CacheBuilder.newBuilder().expireAfterWrite(timeout, TimeUnit.MINUTES).build();
	}

	public static SesAttachment getAttachment(String directory, String filename) throws AwsCloudException {

		String filenameWithPath = constructFilenameWithPath(directory, filename);

		SesAttachment attachment = getAttachment(filenameWithPath);
		if (attachment == null) {
			InputStream inputStream = readFileStreamFromS3(filenameWithPath);
			attachment = new SesAttachment(inputStream, filename);
			putAttachment(filenameWithPath, attachment);
			try {
				inputStream.close();
			} 
			catch (IOException e) {
				final String ERRFMT = "failed to read file from S3: %s";
				throw new AwsCloudException(String.format(ERRFMT, filenameWithPath), e);
			}
		}
		return attachment;
	}

	public static void addFileToAttachments(File file, String directory, String filename) throws AwsCloudException {

		String filenameWithPath = constructFilenameWithPath(directory, filename);

		SesAttachment attachment = new SesAttachment(file, filename);
		putAttachment(filenameWithPath, attachment);
		
		try {
			writeFileToS3(attachment.getInputStream(), filenameWithPath);
		} 
		catch (IOException e) {
			
			final String ERRFMT = "failed to write file to S3: %s";
			throw new AwsCloudException(String.format(ERRFMT, filenameWithPath), e);
		}
	}
	
	private static SesAttachment getAttachment(String filenameWithPath) {
		
		return instance().cache.getIfPresent(filenameWithPath);
	}

	private static void putAttachment(String filenameWithPath, SesAttachment attachment) {

		instance().cache.put(filenameWithPath, attachment);
	}

	private static void writeFileToS3(InputStream inputStream, String filenameWithPath) throws AwsCloudException {

		String attBucket = SesEnv.getEmailAttachmentsS3Bucket();
		S3Client.uploadFile(attBucket, filenameWithPath, inputStream);
	}

	@SuppressWarnings("unused")
	private static void writeFileToS3(File file, String filenameWithPath) throws AwsCloudException {

		String attBucket = SesEnv.getEmailAttachmentsS3Bucket();
		S3Client.uploadFile(attBucket, filenameWithPath, file);
	}

	private static InputStream readFileStreamFromS3(String filenameWithPath) throws AwsCloudException {

		String attBucket = SesEnv.getEmailAttachmentsS3Bucket();
		return S3Client.getInputStreamForS3Object(attBucket, filenameWithPath);
	}

	private static String constructFilenameWithPath(String directory, String filename) {

		String filenameWithPath = filename;

		String attDirectory = SesEnv.getEmailAttachmentsS3Directory();
		
		if (StringUtils.isNotBlank(attDirectory) && StringUtils.isNotBlank(directory)) {
			String DIRFMT = "%s/%s/%s";
			filenameWithPath = String.format(DIRFMT, attDirectory, directory, filename);
		} 
		else if (StringUtils.isNotBlank(attDirectory)) {
			String DIRFMT = "%s/%s";
			filenameWithPath = String.format(DIRFMT, attDirectory, filename);
		} 
		else if (StringUtils.isNotBlank(directory)) {
			String DIRFMT = "%s/%s";
			filenameWithPath = String.format(DIRFMT, directory, filename);
		}

		return filenameWithPath;
	}
}
