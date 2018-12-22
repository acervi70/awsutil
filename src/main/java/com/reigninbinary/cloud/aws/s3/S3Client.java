package com.reigninbinary.cloud.aws.s3;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.reigninbinary.cloud.aws.AwsCloudException;

public class S3Client {
	
	private static final String ERRFMT = "S3 operation failure; bucket: %s, file: %s ";		

	private static final AmazonS3 s3client;
	static {
		String region = S3Env.getRegion();
		if (StringUtils.isNotBlank(region)) {
			s3client = AmazonS3ClientBuilder.standard().withRegion(region).build();
		} else {
			s3client = AmazonS3ClientBuilder.standard().build();
		}
	}

	public static void uploadFile(
			String bucketName, 
			String filenameWithPath, 
			InputStream inputStream) throws AwsCloudException {

		try {
			byte[] bytes = IOUtils.toByteArray(inputStream);
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
	
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(bytes.length);
			
			s3client.putObject(bucketName, filenameWithPath, byteArrayInputStream, metadata);
		} 
		catch (AmazonClientException | IOException e) {
			
			throw new AwsCloudException(String.format(ERRFMT, bucketName, filenameWithPath), e);
		}
	}

	public static void uploadFile(
			String bucketName, 
			String filenameWithPath, 
			File file) throws AwsCloudException {

		try {
			uploadFile(bucketName, filenameWithPath, new FileInputStream(file));
		} 
		catch (FileNotFoundException e) {
			
			throw new AwsCloudException(String.format(ERRFMT, bucketName, filenameWithPath), e);
		}
		
		s3client.putObject(bucketName, filenameWithPath, file);
	}

	public static S3ObjectInputStream getInputStreamForS3Object(
			String bucketName, 
			String filenameWithPath) throws AwsCloudException {

		try {
			S3Object s3object = s3client.getObject(bucketName, filenameWithPath);
			S3ObjectInputStream inputStream = s3object.getObjectContent();
			return inputStream;
		}
		catch (AmazonClientException e) {
			
			throw new AwsCloudException(String.format(ERRFMT, bucketName, filenameWithPath), e);
		}
		
	}
}
