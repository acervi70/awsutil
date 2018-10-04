package com.reigninbinary.aws.ses;

public interface SesEmailTemplate {

	public String getTemplateName();
	public String getSubject();
	public String getText();
	public String getHtml();
}
