package com.reigninbinary.aws.s3;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import com.reigninbinary.aws.util.AwsUtilException;

public class S3Client {

	static AmazonS3 s3client;
	static {
		String region = S3Env.getRegion();
		if (StringUtils.isNotBlank(region)) {
			s3client = AmazonS3ClientBuilder.standard().withRegion(region).build();
		} else {
			s3client = AmazonS3ClientBuilder.standard().build();
		}
	}

	public static void uploadFile(String bucketName, String filenameWithPath, InputStream inputStream)
			throws AwsUtilException {

		final String ERRFMT = "failed to upload file to S3; bucket: %s, file: %s ";
		try {
			ObjectMetadata metadata = new ObjectMetadata();
			byte[] bytes = IOUtils.toByteArray(inputStream);
			metadata.setContentLength(bytes.length);
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
			s3client.putObject(bucketName, filenameWithPath, byteArrayInputStream, metadata);
		} catch (AmazonServiceException e) {
			throw new AwsUtilException(String.format(ERRFMT, bucketName, filenameWithPath), e);
		} catch (SdkClientException e) {
			throw new AwsUtilException(String.format(ERRFMT, bucketName, filenameWithPath), e);
		} catch (IOException e) {
			throw new AwsUtilException(String.format(ERRFMT, bucketName, filenameWithPath), e);
		}
	}

	public static void uploadFile(String bucketName, String filenameWithPath, File file) throws AwsUtilException {

		final String ERRFMT = "failed to upload file to S3; bucket: %s, file: %s ";

		try {
			s3client.putObject(bucketName, filenameWithPath, file);
		} catch (AmazonServiceException e) {
			throw new AwsUtilException(String.format(ERRFMT, bucketName, filenameWithPath), e);
		} catch (SdkClientException e) {
			throw new AwsUtilException(String.format(ERRFMT, bucketName, filenameWithPath), e);
		}
	}

	public static S3ObjectInputStream getFileStream(String bucketName, String filenameWithPath)
			throws AwsUtilException {

		final String ERRFMT = "failed to download file from S3; bucket: %s, file: %s ";

		try {
			S3Object s3object = s3client.getObject(bucketName, filenameWithPath);
			S3ObjectInputStream inputStream = s3object.getObjectContent();
			return inputStream;
		} catch (AmazonServiceException e) {
			throw new AwsUtilException(String.format(ERRFMT, bucketName, filenameWithPath), e);
		} catch (SdkClientException e) {
			throw new AwsUtilException(String.format(ERRFMT, bucketName, filenameWithPath), e);
		}
	}
}
