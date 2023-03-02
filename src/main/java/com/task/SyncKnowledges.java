package com.task;

import com.alibaba.fastjson.JSON;
import com.entity.User;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: ohhhh
 * @description: 1
 * @author: wangnie
 * @create: 2022-10-29 02:40
 **/

@Component
public class SyncKnowledges {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @PostConstruct
    @Scheduled(
            cron = "${service.kbp.cron.task5}"
    )
    public void syncKnowledges() throws IOException {

        List<User> esKnowledgeArticles = new ArrayList<>();
        try {

        }
                GetIndexRequest getIndexRequest = new GetIndexRequest(new String[]{"kbp_knowledge"});
                boolean exists = this.restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
                if (exists) {
                    DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("kbp_knowledge");
                    AcknowledgedResponse acknowledgedResponse = this.restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
                    
                }


                CreateIndexRequest createIndexRequest = new CreateIndexRequest("kbp_knowledge");
                XContentBuilder builder = XContentFactory.jsonBuilder();
                builder.startObject();
                builder.startObject("properties");
                builder.startObject("id");
                builder.field("type", "long");
                builder.endObject();
                builder.startObject("content");
                builder.field("type", "text").field("analyzer", "ik_max_word").field("search_analyzer", "ik_smart");
                builder.endObject();
                builder.startObject("knowledgeCategoryId");
                builder.field("type", "long");
                builder.endObject();
                builder.startObject("summary");
                builder.field("type", "text").field("analyzer", "ik_max_word").field("search_analyzer", "ik_smart");
                builder.endObject();
                builder.startObject("title");
                builder.field("type", "text").field("analyzer", "ik_max_word").field("search_analyzer", "ik_smart");
                builder.endObject();
                builder.startObject("fileTitle");
                builder.field("type", "text").field("analyzer", "ik_max_word").field("search_analyzer", "ik_smart");
                builder.endObject();
                builder.startObject("keyWord");
                builder.field("type", "text").field("analyzer", "ik_max_word").field("search_analyzer", "ik_smart");
                builder.endObject();
                builder.endObject();
                builder.endObject();
                createIndexRequest.mapping(builder);
                CreateIndexResponse createIndexResponse = this.restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);


                    this.addKonwledges(esKnowledgeArticles);



    }

    public void addKonwledges(List<ESKnowledgeArticle> esKnowledgeArticles) {
        esKnowledgeArticles.forEach((knowledge) -> {
            try {
                BulkRequest bulkRequest = new BulkRequest();
                IndexRequest indexRequest = new IndexRequest("kbp_knowledge");
                indexRequest.source(JSON.toJSONString(knowledge), XContentType.JSON);
                indexRequest.id(knowledge.getId().toString());
                bulkRequest.add(indexRequest);
                this.restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
                log.info("数据同步中...");
            } catch (Exception var4) {
                log.error("同步数据异常：{}，{}", var4, var4.getMessage());
            }

        });
    }
}
