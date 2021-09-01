package com.itcast.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix="elasticsearch")
public class ESConfig {
    private String host;

    private int port;


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Bean
    public RestHighLevelClient restHighLevelClient(){
        RestClientBuilder builder = RestClient.builder(new HttpHost(host, port, "http"));
        builder.setRequestConfigCallback(requestConfigBuilder ->{
            requestConfigBuilder.setConnectionRequestTimeout(500000);
            requestConfigBuilder.setSocketTimeout(500000);
            requestConfigBuilder.setConnectTimeout(500000);
            return requestConfigBuilder;
        });
        return new RestHighLevelClient(builder);
    }
}
