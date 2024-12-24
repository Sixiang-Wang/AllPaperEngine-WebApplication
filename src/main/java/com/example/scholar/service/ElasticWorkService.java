package com.example.scholar.service;


import com.example.scholar.domain.openalexElasticsearch.Works;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Response;
import org.elasticsearch.search.suggest.Suggest;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import springfox.documentation.spring.web.json.Json;

import java.io.IOException;
import java.util.List;


@Component
public interface ElasticWorkService {

    /**
     * 根据标题查询并高亮显示
     * @param title 标题
     * @return 返回高亮显示的结果集
     */
    void searchByTitleTest(String title);
    List<SearchHit<Works>> searchByTitleByPage(String title, int page);

    List<SearchHit<Works>> searchByTitle(String title);

    int getLenthOfFindTitleOrKeywordsTextOrAbstract(String searchTerm);

    List<SearchHit<Works>> findByTitleOrKeywordsTextOrAbstract(String searchTerm,int pageIndex);

    Json AutoCompleteWithCompletionSuggester(String searchContent) throws IOException;
    Json AutoFixSuggester(String searchContent) throws IOException;


    Json AutoCompleteTitleWithCompletionSuggester(String searchContent) throws IOException;
    Json AutoFixTitleSuggester(String searchContent) throws IOException;


    Json AutoCompleteAbstractWithCompletionSuggester(String searchContent) throws IOException;
    Json AutoFixAbstractSuggester(String searchContent) throws IOException;


    Json AutoCompleteKeywordsWithCompletionSuggester(String searchContent) throws IOException;
    Json AutoFixKeywordsSuggester(String searchContent) throws IOException;


    /**
     * AdvancedSearch param:字符串 分别是 
     */








}
