package com.reigninbinary.aws.ses;

import org.apache.commons.io.FilenameUtils;


public class SesMimeTypes {

	public static final String FILETYPE_PDF = "pdf";
	
	public static final String MIMETYPE_PDF = "application/pdf";
	
	public static String getMimeTypeFromFilename(String filename) throws IllegalArgumentException {

		String extension = FilenameUtils.getExtension(filename);

		if (0 == FILETYPE_PDF.compareToIgnoreCase(extension)) {
			return MIMETYPE_PDF;
		}
		
		final String ERRFMT = "unsupoorted mime type: %s";
		throw new IllegalArgumentException(String.format(ERRFMT, extension));
	}

}
