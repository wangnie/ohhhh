package com.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: ohhhh
 * @description: 1
 * @author: wangnie
 * @create: 2022-10-29 02:34
 **/


@Configuration
public class ElasticSearchConfig {

    @Value("${params.es.host:127.0.0.1}")
    private String esUrl;
    @Value("${params.es.username:}")
    private String username;
    @Value("${params.es.password:}")
    private String password;

    public ElasticSearchConfig() {
    }

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        HttpHost host = new HttpHost("127.0.0.1", 9200, "http");

        RestClientBuilder builder = RestClient.builder(new HttpHost[]{host});
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(this.username, this.password));
        builder.setHttpClientConfigCallback((f) -> {
            return f.setDefaultCredentialsProvider(credentialsProvider);
        });

        RestHighLevelClient client = new RestHighLevelClient(builder);
        return client;
    }
}
