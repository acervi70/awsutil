package com.reigninbinary.cloud.aws.ses;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.util.ByteArrayDataSource;
import javax.mail.util.SharedByteArrayInputStream;

import com.reigninbinary.cloud.aws.AwsCloudException;

public class SesAttachment implements DataSource {

	private ByteArrayDataSource dataSource;

	public SesAttachment(File file, String filename) throws AwsCloudException {

		try {
			InputStream inputStream = getInputStreamFromFile(file, filename);
			createDataSource(inputStream, filename);
		} 
		catch (IOException e) {
			
			final String ERRFMT = "faled to create attachment from File; filename: %s";
			throw new AwsCloudException(String.format(ERRFMT, filename));
		}
	}

	public SesAttachment(InputStream inputStream, String filename) throws AwsCloudException {

		try {
			createDataSource(inputStream, filename);
		} 
		catch (IOException e) {
			
			final String ERRFMT = "faled to create attachment from InputStream; filename: %s";
			throw new AwsCloudException(String.format(ERRFMT, filename));
		}
	}

	@Override
	public String getContentType() {

		return dataSource.getContentType();
	}

	@Override
	public InputStream getInputStream() throws IOException {

		return dataSource.getInputStream();
	}

	@Override
	public String getName() {

		return dataSource.getName();
	}

	@Override
	public OutputStream getOutputStream() throws IOException {

		return dataSource.getOutputStream();
	}
	
	private void createDataSource(InputStream inputStream, String filename) throws IOException {
		
		byte[] bytes = getBytesFromInputStream(inputStream);
		SharedByteArrayInputStream sharedStream = new SharedByteArrayInputStream(bytes);
		dataSource = new ByteArrayDataSource(sharedStream, SesMimeTypes.getMimeTypeFromFilename(filename));
		dataSource.setName(filename);
	}
	
	private InputStream getInputStreamFromFile(File file, String filename) throws IOException {
		
		FileDataSource fds = new FileDataSource(file);
		return fds.getInputStream();
	}

	private byte[] getBytesFromInputStream(InputStream inputStream) throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		int nRead;
		byte[] data = new byte[1024];
		while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
			baos.write(data, 0, nRead);
		}
		baos.flush();

		return baos.toByteArray();
	}
}
