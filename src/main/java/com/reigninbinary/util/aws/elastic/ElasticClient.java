package com.reigninbinary.util.aws.elastic;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

public class ElasticClient {
	
	private RestHighLevelClient restHighLevelClient;

	public ElasticClient() {
		
		RestClientBuilder builder = RestClient.builder(new HttpHost(
				ElasticConfig.getHostname(), ElasticConfig.getPort(), ElasticConfig.getScheme()));
		builder.setMaxRetryTimeoutMillis(ElasticConfig.getMaxRetryTimeoutMillis());
		
		restHighLevelClient = new RestHighLevelClient(builder);
	}
	
	public RestHighLevelClient client() {
		
		return restHighLevelClient;
	}
}
