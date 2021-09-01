package com.itcast;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.cluster.metadata.MappingMetadata;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ESTest {
    
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    public static final String INDEX = "hotel";


    @Test
    public void findIndex() throws IOException {

        IndicesClient indicesClient = restHighLevelClient.indices();

        GetIndexRequest getIndexRequest = new GetIndexRequest(INDEX);

        GetIndexResponse response = indicesClient.get(getIndexRequest, RequestOptions.DEFAULT);

        Map<String, MappingMetadata> mappings = response.getMappings();
        for (String key : mappings.keySet()) {
            System.out.println(key+"==="+mappings.get(key).getSourceAsMap());
        }
    }



}