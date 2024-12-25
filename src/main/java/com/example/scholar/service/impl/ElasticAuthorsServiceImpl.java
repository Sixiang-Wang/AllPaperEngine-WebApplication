package com.example.scholar.service.impl;

import com.example.scholar.service.ElasticAuthorService;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service("ElasticAuthorService")
public class ElasticAuthorsServiceImpl implements ElasticAuthorService {

    private RestHighLevelClient client;

    @Override
    public List<SearchHit> searchByDisplayNameByPage(String displayName, int page) {
        SearchRequest searchRequestWorks = new SearchRequest("authors_index");
        client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("116.204.112.5", 9200, "http")));

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        searchSourceBuilder.size(20);
        boolQuery.must(QueryBuilders.matchQuery("display_name", displayName));
        searchSourceBuilder.query(boolQuery);

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("display_name");
        highlightBuilder.preTags("<span style=\"color:red;\">"); // 使用红色样式标签
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);

        searchRequestWorks.source(searchSourceBuilder);

        try {
            SearchResponse works =  client.search(searchRequestWorks, RequestOptions.DEFAULT);
            SearchHits searchHits = works.getHits();

            int max = searchHits.getHits().length;
            int from = (page-1)*20;
            int to = page*20;

            List<org.elasticsearch.search.SearchHit> newList = new ArrayList<>();
            if(to>max){
                to = max;
            }
            for(int i=from;i<to;i++){
                newList.add(searchHits.getAt(i));
            }
            return newList;


        } catch (IOException e) {
            throw new RuntimeException(e);
        }




    }


    //这个搜institution
    @Override
    public List<SearchHit> searchByDisplayName(String displayName, int page) {
        SearchRequest searchRequestWorks = new SearchRequest("institutions_index");
        client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("116.204.112.5", 9200, "http")));

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        searchSourceBuilder.size(20);
        boolQuery.must(QueryBuilders.matchQuery("display_name", displayName));
        searchSourceBuilder.query(boolQuery);
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("display_name");
        highlightBuilder.preTags("<span style=\"color:red;\">"); // 使用红色样式标签
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);
        searchRequestWorks.source(searchSourceBuilder);



        try {
            SearchResponse works =  client.search(searchRequestWorks, RequestOptions.DEFAULT);
            SearchHits searchHits = works.getHits();


            int max = searchHits.getHits().length;
            int from = (page-1)*20;
            int to = page*20;

            List<org.elasticsearch.search.SearchHit> newList = new ArrayList<>();
            if(to>max){
                to = max;
            }
            for(int i=from;i<to;i++){
                newList.add(searchHits.getAt(i));
            }
            return newList;


        } catch (IOException e) {
            throw new RuntimeException(e);
        }




    }


}
