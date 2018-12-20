package com.reigninbinary.cloud.aws.ses;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.reigninbinary.cloud.aws.AwsCloudException;
import com.reigninbinary.cloud.aws.ses.SesEmailInfo;

public class SesRenderedTemplate implements SesEmailTemplate {

	private String renderedText;
	private String renderedHtml;

	private SesEmailTemplate emailTemplate;

	public SesRenderedTemplate(SesEmailTemplate emailTemplate, SesEmailInfo emailInfo) throws AwsCloudException {
		this(emailTemplate, emailInfo.getMergeFields());
	}

	public SesRenderedTemplate(SesEmailTemplate emailTemplate, Map<String, Object> mapTemplateData)
			throws AwsCloudException {

		Mustache m;
		String template;
		StringWriter writer;

		MustacheFactory mf = new DefaultMustacheFactory();

		template = emailTemplate.getText();
		m = mf.compile(new StringReader(template), "text");
		writer = new StringWriter();
		try {
			m.execute(writer, mapTemplateData).flush();
		} catch (IOException e) {
			throw new AwsCloudException(e);
		}
		this.renderedText = writer.toString();

		template = emailTemplate.getHtml();
		m = mf.compile(new StringReader(template), "html");
		writer = new StringWriter();
		try {
			m.execute(writer, mapTemplateData).flush();
		} catch (IOException e) {
			throw new AwsCloudException(e);
		}
		this.renderedHtml = writer.toString();

		this.emailTemplate = emailTemplate;
	}

	@Override
	public String getTemplateName() {

		return emailTemplate.getTemplateName();
	}

	@Override
	public String getSubject() {

		return emailTemplate.getSubject();

	}

	@Override
	public String getText() {

		return renderedText;
	}

	@Override
	public String getHtml() {

		return renderedHtml;
	}
}
