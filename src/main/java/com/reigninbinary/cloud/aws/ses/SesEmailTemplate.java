package com.reigninbinary.cloud.aws.ses;

public interface SesEmailTemplate {

	public String getTemplateName();
	public String getSubject();
	public String getText();
	public String getHtml();
}
