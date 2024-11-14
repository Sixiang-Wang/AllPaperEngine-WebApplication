package com.example.scholar.service.impl;

import com.example.scholar.domain.openalexElasticsearch.Works;
import com.example.scholar.domain.openalexElasticsearch.WorksCompletion;
import com.example.scholar.repository.ElasticSearchRepository;
import com.example.scholar.service.ElasticWorkService;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.elasticsearch.search.suggest.term.TermSuggestionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import springfox.documentation.spring.web.json.Json;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.search.suggest.SuggestBuilders.completionSuggestion;


@Service("ElasticWorkService")
public class ElasticWorksServiceImpl implements ElasticWorkService {



    @Autowired
    private ElasticsearchOperations elasticsearchOperations;
    @Resource
    private ElasticSearchRepository elasticSearchRepository;

    @Override
    public List<SearchHit<Works>> searchByTitle(String title) {
        return elasticSearchRepository.findByTitle(title);
    }

    @Override
    public List<SearchHit<Works>> findByTitleOrKeywordsTextOrAbstract(String searchTerm) {
        return elasticSearchRepository.findByTitleOrKeywordsTextOrAbstract(searchTerm);
    }




    @Override
    public Json AutoCompleteWithCompletionSuggester(String searchContent) throws IOException {


        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http")));

        try {
            // 创建SearchRequest
            SearchRequest searchRequest = new SearchRequest("openalex_works_index_addingcompletion1");
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

            // 创建SuggestBuilder
            SuggestBuilder suggestBuilder = new SuggestBuilder();

            // 添加title_suggest
            CompletionSuggestionBuilder titleSuggestion = SuggestBuilders.completionSuggestion("title.suggest_field")
                    .prefix(searchContent)
                    .size(10);
            suggestBuilder.addSuggestion("title_suggest", titleSuggestion);

            // 添加abstractSuggest
            CompletionSuggestionBuilder abstractSuggestion = SuggestBuilders.completionSuggestion("abstract.suggest_field")
                    .prefix(searchContent)
                    .size(10);
            suggestBuilder.addSuggestion("abstractSuggest", abstractSuggestion);

            // 添加display_name_suggest
            CompletionSuggestionBuilder displayNameSuggestion = SuggestBuilders.completionSuggestion("display_name.suggest_field")
                    .prefix(searchContent)
                    .size(10);
            suggestBuilder.addSuggestion("display_name_suggest", displayNameSuggestion);

            // 添加keywordstextSuggest
            CompletionSuggestionBuilder keywordsTextSuggestion = SuggestBuilders.completionSuggestion("keywordstext.suggest_field")
                    .prefix(searchContent)
                    .size(10);
            suggestBuilder.addSuggestion("keywordstextSuggest", keywordsTextSuggestion);

            // 设置SuggestBuilder到SearchSourceBuilder
            sourceBuilder.suggest(suggestBuilder);

            // 设置SearchSourceBuilder到SearchRequest
            searchRequest.source(sourceBuilder);

            // 执行搜索请求
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);


            // 处理搜索结果
            Suggest suggest = searchResponse.getSuggest();

//            if (suggest != null) {
//                suggest.forEach(suggestion -> {
//                    System.out.println("Suggestion Name: " + suggestion.getName());
//                    suggestion.getEntries().forEach(entry -> {
//                        System.out.println("  Text: " + entry.getText());
//                        entry.getOptions().forEach(option -> {
//                            System.out.println("    -- Score: " + option.getScore() + ", Text: " + option.getText());
//                        });
//                    });
//                });
//            }
            return new Json(suggest.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭客户端
            try {
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return null;
    }

    @Override
    public Json AutoFixSuggester(String searchContent) throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http")));



        try {
            SearchRequest searchRequest = new SearchRequest("openalex_works_index_addingcompletion1");
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            //	建议模式（控制提供建议词的方式）：
        //1. missing：默认方式，仅在‘要搜索词项’不在索引中存在时，才提供建议词；
        //2. popular：仅提供频率比‘要搜索词项’高的建议词；
        //3. always：总是提供建议词；
            TermSuggestionBuilder titleSuggestion = SuggestBuilders.termSuggestion("title.keyword")
                    .suggestMode(TermSuggestionBuilder.SuggestMode.ALWAYS)
                    .text(searchContent)
//                    .analyzer( "ik_smart")
                    .size(5); // 设定返回数量

            TermSuggestionBuilder abstractSuggestion = SuggestBuilders.termSuggestion("abstract.keyword")
                    .suggestMode(TermSuggestionBuilder.SuggestMode.ALWAYS)
                    .text(searchContent)
//                    .analyzer( "ik_smart")
                    .size(5); // 设定返回数量

            TermSuggestionBuilder display_nameSuggestion = SuggestBuilders.termSuggestion("display_name.keyword")
                    .suggestMode(TermSuggestionBuilder.SuggestMode.ALWAYS)
                    .text(searchContent)
                    .size(5); // 设定返回数量
//                .analyzer( "ik_smart")
            TermSuggestionBuilder keywordstextSuggestion = SuggestBuilders.termSuggestion("keywordstext.keyword")
                    .suggestMode(TermSuggestionBuilder.SuggestMode.ALWAYS)
                    .text(searchContent)
//                    .analyzer( "ik_smart")
                    .size(5); // 设定返回数量

            SuggestBuilder suggestBuilder = new SuggestBuilder();
            suggestBuilder.addSuggestion("titleSuggestion", titleSuggestion)
                    .addSuggestion("display_nameSuggestion",display_nameSuggestion)
                    .addSuggestion("keywordstextSuggestion",keywordstextSuggestion)
                    .addSuggestion("abstractSuggestion",abstractSuggestion);
            // 设置SuggestBuilder到SearchSourceBuilder
            sourceBuilder.suggest(suggestBuilder);

            // 设置SearchSourceBuilder到SearchRequest
            searchRequest.source(sourceBuilder);

            // 执行搜索请求
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            // 处理搜索结果
            Suggest suggest = searchResponse.getSuggest();
            return new Json(suggest.toString());


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭客户端
            try {
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Json AutoCompleteTitleWithCompletionSuggester(String searchContent) throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http")));

        try {
            // 创建SearchRequest
            SearchRequest searchRequest = new SearchRequest("openalex_works_index_addingcompletion1");
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

            // 创建SuggestBuilder
            SuggestBuilder suggestBuilder = new SuggestBuilder();

            // 添加title_suggest
            CompletionSuggestionBuilder titleSuggestion = SuggestBuilders.completionSuggestion("title.suggest_field")
                    .prefix(searchContent)
                    .size(10);
            suggestBuilder.addSuggestion("title_suggest", titleSuggestion);


            // 设置SuggestBuilder到SearchSourceBuilder
            sourceBuilder.suggest(suggestBuilder);

            // 设置SearchSourceBuilder到SearchRequest
            searchRequest.source(sourceBuilder);

            // 执行搜索请求
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);


            // 处理搜索结果
            Suggest suggest = searchResponse.getSuggest();

            return new Json(suggest.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭客户端
            try {
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return null;
    }

    @Override
    public Json AutoFixTitleSuggester(String searchContent) throws IOException {
        return null;
    }

    @Override
    public Json AutoCompleteAbstractWithCompletionSuggester(String searchContent) throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http")));

        try {
            // 创建SearchRequest
            SearchRequest searchRequest = new SearchRequest("openalex_works_index_addingcompletion1");
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

            // 创建SuggestBuilder
            SuggestBuilder suggestBuilder = new SuggestBuilder();


            // 添加abstractSuggest
            CompletionSuggestionBuilder abstractSuggestion = SuggestBuilders.completionSuggestion("abstract.suggest_field")
                    .prefix(searchContent)
                    .size(10);
            suggestBuilder.addSuggestion("abstractSuggest", abstractSuggestion);


            // 设置SuggestBuilder到SearchSourceBuilder
            sourceBuilder.suggest(suggestBuilder);

            // 设置SearchSourceBuilder到SearchRequest
            searchRequest.source(sourceBuilder);

            // 执行搜索请求
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            // 处理搜索结果
            Suggest suggest = searchResponse.getSuggest();

            return new Json(suggest.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭客户端
            try {
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return null;
    }

    @Override
    public Json AutoFixAbstractSuggester(String searchContent) throws IOException {
        return null;
    }

    @Override
    public Json AutoCompleteKeywordsWithCompletionSuggester(String searchContent) throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http")));

        try {
            // 创建SearchRequest
            SearchRequest searchRequest = new SearchRequest("openalex_works_index_addingcompletion1");
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

            // 创建SuggestBuilder
            SuggestBuilder suggestBuilder = new SuggestBuilder();

            // 添加keywordstextSuggest
            CompletionSuggestionBuilder keywordsTextSuggestion = SuggestBuilders.completionSuggestion("keywordstext.suggest_field")
                    .prefix(searchContent)
                    .size(10);
            suggestBuilder.addSuggestion("keywordstextSuggest", keywordsTextSuggestion);

            // 设置SuggestBuilder到SearchSourceBuilder
            sourceBuilder.suggest(suggestBuilder);

            // 设置SearchSourceBuilder到SearchRequest
            searchRequest.source(sourceBuilder);

            // 执行搜索请求
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);


            // 处理搜索结果
            Suggest suggest = searchResponse.getSuggest();


            return new Json(suggest.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭客户端
            try {
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return null;
    }

    @Override
    public Json AutoFixKeywordsSuggester(String searchContent) throws IOException {
        return null;
    }


}
