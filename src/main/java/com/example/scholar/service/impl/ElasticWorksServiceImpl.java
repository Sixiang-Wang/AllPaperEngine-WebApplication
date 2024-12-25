package com.example.scholar.service.impl;

import com.example.scholar.dao.SearchedWorkMapper;
import com.example.scholar.domain.openalexElasticsearch.Works;
import com.example.scholar.repository.ElasticSearchRepository;
import com.example.scholar.service.ElasticWorkService;
import com.example.scholar.util.SQLAuthorBuilder;
import com.example.scholar.util.SQLInstitutionBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.elasticsearch.search.suggest.completion.FuzzyOptions;
import org.elasticsearch.search.suggest.term.TermSuggestionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import springfox.documentation.spring.web.json.Json;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.cache.annotation.Cacheable;

@Service("ElasticWorkService")
public class ElasticWorksServiceImpl implements ElasticWorkService {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;


    @Resource
    private ElasticSearchRepository elasticSearchRepository;



    @Resource
    private SearchedWorkMapper elasticWorkMapper;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Resource
    private ElasticWorkService elasticWorkService;




    private RestHighLevelClient client;

    public ElasticWorksServiceImpl(RestHighLevelClient client) {
        this.client = client;
    }

    public SearchResponse advancedSearch(List<String> andTitles,List<Boolean> andTitlesFuzzy, List<String> orTitles,List<Boolean> orTitlesFuzzy, List<String> notTitles,List<Boolean> notTitlesFuzzy,
                                         List<String> andTopics,List<Boolean> andTopicsFuzzy, List<String> orTopics,List<Boolean> orTopicsFuzzy, List<String> notTopics,List<Boolean> notTopicsFuzzy,
                                         List<String> andAuthors,List<Boolean> andAuthorsFuzzy, List<String> orAuthors,List<Boolean> orAuthorsFuzzy, List<String> notAuthors,List<Boolean> notAuthorsFuzzy,//author名字
                                         List<String> andFirstAuthors,List<Boolean> andFirstAuthorsFuzzy, List<String> orFirstAuthors,List<Boolean> orFirstAuthorsFuzzy, List<String> notFirstAuthors,List<Boolean> notFirstAuthorsFuzzy,
                                         List<String> andInstitutions,List<Boolean> andInstitutionsFuzzy, List<String> orInstitutions, List<Boolean> orInstitutionsFuzzy,List<String> notInstitutions,List<Boolean> notInstitutionsFuzzy,
                                         List<String> andAbstracts,List<Boolean> andAbstractsFuzzy, List<String> orAbstracts, List<Boolean> orAbstractsFuzzy,List<String> notAbstracts,List<Boolean> notAbstractsFuzzy,
                                         List<String> andDOI,List<Boolean> andDOIFuzzy, List<String> orDOI,List<Boolean> orDOIFuzzy, List<String> notDOI,List<Boolean> notDOIFuzzy,
                                         java.sql.Date startDate, java.sql.Date endDate) throws IOException {
        SearchRequest searchRequestWorks = new SearchRequest("works_index");
        SearchRequest searchRequestAuthors = new SearchRequest("authors_index");
        SearchRequest searchRequestFirstAuthors = new SearchRequest("authors_index");
        SearchRequest searchRequestWorksAuthorShips = new SearchRequest("works_authorships_index");
        SearchRequest searchRequestTopics = new SearchRequest("topics_index");
        SearchRequest searchRequestInstitutions = new SearchRequest("institutions_index");
        SearchRequest searchRequestWorksTopics = new SearchRequest("works_topics_index");

        if(andTitles == null){
            andTitles = new ArrayList<String>();
        }
        if(andTitlesFuzzy == null){
            andTitlesFuzzy = new ArrayList<>();
        }
        if(orTitles == null){
            orTitles = new ArrayList<String>();
        }
        if(orTitlesFuzzy == null){
            orTitlesFuzzy = new ArrayList<>();
        }
        if(notTitles == null){
            notTitles = new ArrayList<>();
        }
        if(notTitlesFuzzy == null){
            notTitlesFuzzy = new ArrayList<>();
        }

        if(andTopics == null){
            andTopics = new ArrayList<String>();
        }
        if(andTopicsFuzzy == null){
            andTopicsFuzzy = new ArrayList<>();
        }
        if(orTopics == null){
            orTopics = new ArrayList<String>();
        }
        if(orTopicsFuzzy == null){
            orTopicsFuzzy = new ArrayList<>();
        }
        if(notTopics == null){
            notTopics = new ArrayList<>();
        }
        if(notTopicsFuzzy == null){
            notTopicsFuzzy = new ArrayList<>();
        }

        if(andAuthors == null){
            andAuthors = new ArrayList<String>();
        }
        if(andAuthorsFuzzy == null){
            andAuthorsFuzzy = new ArrayList<>();
        }
        if(orAuthors == null){
            orAuthors = new ArrayList<String>();
        }
        if(orAuthorsFuzzy == null){
            orAuthorsFuzzy = new ArrayList<>();
        }
        if(notAuthors == null){
            notAuthors = new ArrayList<>();
        }
        if(notAuthorsFuzzy == null){
            notAuthorsFuzzy = new ArrayList<>();
        }


        if(andInstitutions == null){
            andInstitutions = new ArrayList<String>();
        }
        if(andInstitutionsFuzzy == null){
            andInstitutionsFuzzy = new ArrayList<>();
        }
        if(orInstitutions == null){
            orInstitutions = new ArrayList<String>();
        }
        if(orInstitutionsFuzzy == null){
            orInstitutionsFuzzy = new ArrayList<>();
        }
        if(notInstitutions == null){
            notInstitutions = new ArrayList<>();
        }
        if(notInstitutionsFuzzy == null){
            notInstitutionsFuzzy = new ArrayList<>();
        }

        if(andFirstAuthors == null){
            andFirstAuthors = new ArrayList<String>();
        }
        if(andFirstAuthorsFuzzy == null){
            andFirstAuthorsFuzzy = new ArrayList<>();
        }
        if(orFirstAuthors == null){
            orFirstAuthors = new ArrayList<String>();
        }
        if(orFirstAuthorsFuzzy == null){
            orFirstAuthorsFuzzy = new ArrayList<>();
        }
        if(notFirstAuthors == null){
            notFirstAuthors = new ArrayList<>();
        }
        if(notFirstAuthorsFuzzy == null){
            notFirstAuthorsFuzzy = new ArrayList<>();
        }

        if(andAbstracts == null){
            andAbstracts = new ArrayList<String>();
        }
        if(andAbstractsFuzzy == null){
            andAbstractsFuzzy = new ArrayList<>();
        }
        if(orAbstracts == null){
            orAbstracts = new ArrayList<String>();
        }
        if(orAbstractsFuzzy == null){
            orAbstractsFuzzy = new ArrayList<>();
        }
        if(notAbstracts == null){
            notAbstracts = new ArrayList<>();
        }
        if(notAbstractsFuzzy == null){
            notAbstractsFuzzy = new ArrayList<>();
        }


        if(andDOI == null){
            andDOI = new ArrayList<String>();
        }
        if(andDOIFuzzy == null){
            andDOIFuzzy = new ArrayList<>();
        }
        if(orDOI == null){
            orDOI = new ArrayList<String>();
        }
        if(orDOIFuzzy == null){
            orDOIFuzzy = new ArrayList<>();
        }
        if(notDOI == null){
            notDOI = new ArrayList<>();
        }
        if(notDOIFuzzy == null){
            notDOIFuzzy = new ArrayList<>();
        }

        client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("116.204.112.5", 9200, "http")));


        //目标结果: works
        //首先查找es中存储的内容 比如:titles ,authors , firstauthors , doi ,abstract内容

        //searchSourceBuilder1 boolQuery1 for worksindex
        SearchSourceBuilder searchSourceBuilder1 = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery1 = QueryBuilders.boolQuery();
        searchSourceBuilder1.size(20);

        //searchSourceBuilder2 boolQuery2 for authorsindex
        SearchSourceBuilder searchSourceBuilder2 = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery2 = QueryBuilders.boolQuery();
        searchSourceBuilder2.size(20);

        //searchSourceBuilder3 boolQuery2 for worksauthorshipsindex
        SearchSourceBuilder searchSourceBuilder3 = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery3 = QueryBuilders.boolQuery();
        searchSourceBuilder2.size(20);


        //searchSourceBuilder4 boolQuery4 for authors_index for firstAuthor
        SearchSourceBuilder searchSourceBuilder4 = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery4 = QueryBuilders.boolQuery();
        searchSourceBuilder4.size(20);


        //searchSourceBuilder5 boolQuery5 for topics_index for topics
        SearchSourceBuilder searchSourceBuilder5 = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery5 = QueryBuilders.boolQuery();
        searchSourceBuilder5.size(20);


        //searchSourceBuilder6 boolQuery6 for institutions_index for institution
        SearchSourceBuilder searchSourceBuilder6 = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery6 = QueryBuilders.boolQuery();
        searchSourceBuilder6.size(20);

        //searchSourceBuilder7 boolQuery7 for works_topics_index for search worksId by topicsId
        SearchSourceBuilder searchSourceBuilder7 = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery7 = QueryBuilders.boolQuery();
        searchSourceBuilder7.size(20);


        String url = "jdbc:mysql://116.204.112.5:3306/openalex";
        String username = "root";
        String password = "BjMfWi6CFkrW3556";

        //TODO: ES做topic 搜索
        Set<String> topicIds = new HashSet<>();
        if((andTopics!= null && !andTopics.isEmpty()) ||(orTopics!=null&&!orTopics.isEmpty())){
            addMatchQueries(boolQuery5,andTopics,andTopicsFuzzy,orTopics,orTopicsFuzzy,notTopics,notTopicsFuzzy,"display_name");
            searchSourceBuilder5.query(boolQuery5);
            searchRequestTopics.source(searchSourceBuilder5);
            SearchResponse topics =  client.search(searchRequestTopics, RequestOptions.DEFAULT);
            SearchHits searchHits = topics.getHits();
            //获得了所有匹配的相关普通作者 下一步 取出所有topicid 拿到worksauthorships里面搜索出workid
            // 提取所有匹配的 topicid

            for (org.elasticsearch.search.SearchHit hit : searchHits.getHits()) {
                String topicId = (String) hit.getSourceAsMap().get("id");
                topicIds.add(topicId);
            }
        }

        //TODO: ES做topic相关works的检索
        Set<String> worksTopicIds = new HashSet<>();
        List<String> list = worksTopicIds.stream().collect(Collectors.toList());
        if(!topicIds.isEmpty()){
            addMatchQueries(boolQuery7,null,null,list,null,null,null,"topic_id");
            searchSourceBuilder7.query(boolQuery7);
            searchRequestWorksTopics.source(searchSourceBuilder7);
            SearchResponse worksTopics =  client.search(searchRequestWorksTopics, RequestOptions.DEFAULT);
            SearchHits searchHits = worksTopics.getHits();
            //获得了所有匹配的相关普通作者 下一步 取出所有topicid 拿到worksauthorships里面搜索出workid
            // 提取所有匹配的 topicid
            for (org.elasticsearch.search.SearchHit hit : searchHits.getHits()) {
                String workId = (String) hit.getSourceAsMap().get("work_id");
                worksTopicIds.add(workId);
            }
        }




        //做institutions Mysql =====> 改成es搜索
        //TODO: institution ES
        Set<String> institutionids = new HashSet<>();
        if((andInstitutions!= null && !andInstitutions.isEmpty()) ||(orInstitutions!=null&&!orInstitutions.isEmpty())){
            addMatchQueries(boolQuery6,andInstitutions,andInstitutionsFuzzy,orInstitutions,orInstitutionsFuzzy,notInstitutions,notInstitutionsFuzzy,"display_name");
            searchSourceBuilder6.query(boolQuery6);
            searchRequestInstitutions.source(searchSourceBuilder6);
            SearchResponse institutions =  client.search(searchRequestInstitutions, RequestOptions.DEFAULT);
            SearchHits searchHits = institutions.getHits();
            for (org.elasticsearch.search.SearchHit hit : searchHits.getHits()) {
                String institutionId = (String) hit.getSourceAsMap().get("id");
                institutionids.add(institutionId);
            }
        }



        //TODO:  find workids by institutionids
        String sql1 = SQLInstitutionBuilder.buildSQLQuery1(institutionids);
        Set<String>  workids = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql1);
             ResultSet rs = pstmt.executeQuery()) {
            // 处理查询结果
            while (rs.next()) {
                // 读取每一行数据
                workids.add(rs.getString("work_id"));
            }
            System.out.print(workids);
        } catch (SQLException e) {
            e.printStackTrace();
        }



        //TODO:做author 普通作者相关的搜索
        Set<String> authorIds = new HashSet<>();

        if((andAuthors!= null && !andAuthors.isEmpty()) ||(orAuthors!=null&&!orAuthors.isEmpty())){
            addMatchQueries(boolQuery2,andAuthors,andAuthorsFuzzy,orAuthors,orAuthorsFuzzy,notAuthors,notAuthorsFuzzy,"display_name");
            searchSourceBuilder2.query(boolQuery2);
            searchRequestAuthors.source(searchSourceBuilder2);
            SearchResponse authors =  client.search(searchRequestAuthors, RequestOptions.DEFAULT);
            SearchHits searchHits = authors.getHits();
            //获得了所有匹配的相关普通作者 下一步 取出所有authorid 拿到worksauthorships里面搜索出workid
            // 提取所有匹配的 authorid

            for (org.elasticsearch.search.SearchHit hit : searchHits.getHits()) {
                String authorId = (String) hit.getSourceAsMap().get("id");
                authorIds.add(authorId);
            }
        }

        String sql4 = SQLAuthorBuilder.buildSQLQuery(authorIds);
        Set<String> workauthorsids = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql4);
             ResultSet rs = pstmt.executeQuery()) {
            // 处理查询结果
            while (rs.next()) {
                // 读取每一行数据
                workauthorsids.add(rs.getString("work_id"));
            }
            System.out.print(workauthorsids);
        } catch (SQLException e) {
            e.printStackTrace();
        }



        //TODO:做firstAuthor相关的检索
        Set<String> firstAuthorIds = new HashSet<>();
        if((andFirstAuthors!=null && !andFirstAuthors.isEmpty()) ||(orFirstAuthors!=null && !orFirstAuthors.isEmpty())){
            addMatchQueries(boolQuery4,andFirstAuthors,andFirstAuthorsFuzzy,orFirstAuthors,orFirstAuthorsFuzzy,notFirstAuthors,notFirstAuthorsFuzzy,"display_name");
            searchSourceBuilder4.query(boolQuery4);
            searchRequestFirstAuthors.source(searchSourceBuilder4);
            SearchResponse authors =  client.search(searchRequestFirstAuthors, RequestOptions.DEFAULT);
            SearchHits searchHits = authors.getHits();
            //获得了所有匹配的相关第一作者 下一步取出所有authorid 拿到worksauthorships里面搜索出workid 记得加authorposition
            for (org.elasticsearch.search.SearchHit hit : searchHits.getHits()) {
                String authorId = (String) hit.getSourceAsMap().get("id");
                firstAuthorIds.add(authorId);
            }
        }


        String sql5 = SQLAuthorBuilder.buildSQLQuery1(firstAuthorIds);
        System.out.print(sql5);
        Set<String> workFirstAuthorsids = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql5);
             ResultSet rs = pstmt.executeQuery()) {
            // 处理查询结果
            while (rs.next()) {
                // 读取每一行数据
                workFirstAuthorsids.add(rs.getString("id"));
            }
            System.out.print(workFirstAuthorsids);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        Set<String> worksIds = new HashSet<>();
        if(!workids.isEmpty()){
            worksIds.addAll(workids);
            if(!workauthorsids.isEmpty()){
                worksIds.retainAll(workauthorsids);
            }
            if(!workFirstAuthorsids.isEmpty()){
                worksIds.retainAll(workFirstAuthorsids);
            }
        }else{
            if(!workauthorsids.isEmpty()){
                worksIds.addAll(workauthorsids);
                if(!workFirstAuthorsids.isEmpty()){
                    worksIds.retainAll(workFirstAuthorsids);
                }
            }else{
                if(!workFirstAuthorsids.isEmpty()){
                    worksIds.addAll(workFirstAuthorsids);
                }
            }
        }

        if(!worksTopicIds.isEmpty()){
        }else{
            if(worksIds.isEmpty()){
                worksIds.addAll(worksTopicIds);
            }else{
                worksIds.retainAll(worksTopicIds);
            }
        }


        SearchResponse works;
        SearchHits searchHits;
        //TODO:再做worksindex的搜索
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(andTitles.size()!=0 || andDOI.size()!=0 || andAbstracts.size()!=0 || orTitles.size()!=0 || orDOI.size()!=0 || orAbstracts.size()!=0){
            //先做worksindex的搜索

            if(worksIds.isEmpty()){
                //那么就只听worksindex的条件
            }else{
                //做一次交集运算 searchHits中id属于worksIds的项目
                list = worksIds.stream().collect(Collectors.toList());
                addMatchQueries(boolQuery1,null,null,list,null,null,null,"id");
            }

            addMatchQueries(boolQuery1,andTitles,andTitlesFuzzy,orTitles,orTitlesFuzzy,notTitles,notTitlesFuzzy,"title");
            addMatchQueries(boolQuery1,andDOI,andDOIFuzzy,orDOI,orDOIFuzzy,notDOI,notDOIFuzzy,"doi");
            addMatchQueries(boolQuery1,andAbstracts,andAbstractsFuzzy,orAbstracts,orAbstractsFuzzy,notAbstracts,notAbstractsFuzzy,"abstract_inverted_index");


            // 添加时间范围过滤
            if (startDate != null && endDate != null) {
                boolQuery1.filter(QueryBuilders.rangeQuery("publication_date").gte(sdf.format(startDate)).lte(sdf.format(endDate)).format("yyyy-MM-dd"));
            } else if (startDate != null) {
                boolQuery1.filter(QueryBuilders.rangeQuery("publication_date").gte(sdf.format(startDate)).format("yyyy-MM-dd"));
            } else if (endDate != null) {
                boolQuery1.filter(QueryBuilders.rangeQuery("publication_date").lte(sdf.format(endDate)).format("yyyy-MM-dd"));
            }
            searchSourceBuilder1.query(boolQuery1);
            searchRequestWorks.source(searchSourceBuilder1);

            works =  client.search(searchRequestWorks, RequestOptions.DEFAULT);
            searchHits = works.getHits();

        }else{
            if(worksIds.isEmpty()){
                //全空
                return null;
            }else{
                //按时间范围过滤搜索
                //es搜索
                //id属于worksIds的项目
                list = worksIds.stream().collect(Collectors.toList());
                addMatchQueries(boolQuery1,null,null,list,null,null,null,"id");
                // 添加时间范围过滤
                if (startDate != null && endDate != null) {
                    boolQuery1.filter(QueryBuilders.rangeQuery("publication_date").gte(sdf.format(startDate)).lte(sdf.format(endDate)).format("yyyy-MM-dd"));
                } else if (startDate != null) {
                    boolQuery1.filter(QueryBuilders.rangeQuery("publication_date").gte(sdf.format(startDate)).format("yyyy-MM-dd"));
                } else if (endDate != null) {
                    boolQuery1.filter(QueryBuilders.rangeQuery("publication_date").lte(sdf.format(endDate)).format("yyyy-MM-dd"));
                }

                searchSourceBuilder1.query(boolQuery1);
                searchRequestWorks.source(searchSourceBuilder1);

                works =  client.search(searchRequestWorks, RequestOptions.DEFAULT);
                searchHits = works.getHits();
            }
        }

        System.out.print(searchHits);
        return works;
    }

    public static void main(String[] args) {

        List<String> andTitles = new ArrayList<>();
        List<Boolean> andTitlesFuzzy = new ArrayList<>();
        List<String> orTitles = new ArrayList<>();
        List<Boolean> orTitlesFuzzy = new ArrayList<>();
        List<String> notTitles = new ArrayList<>();
        List<Boolean> notTitlesFuzzy = new ArrayList<>();
        andTitles.add("Transformer");
        andTitles.add("LLVM");
        andTitlesFuzzy.add(true);
        andTitlesFuzzy.add(true);
        orTitles.add("Attention");
        orTitlesFuzzy.add(true);
        notTitles.add("Physic");
        notTitlesFuzzy.add(false);



        //List<String> andTitles,List<Boolean> andTitlesFuzzy, List<String> orTitles,List<Boolean> orTitlesFuzzy, List<String> notTitles,List<Boolean> notTitlesFuzzy,
        //                                         List<String> andTopics,List<Boolean> andTopicsFuzzy, List<String> orTopics,List<Boolean> orTopicsFuzzy, List<String> notTopics,List<Boolean> notTopicsFuzzy,
        //                                         List<String> andAuthors,List<Boolean> andAuthorsFuzzy, List<String> orAuthors,List<Boolean> orAuthorsFuzzy, List<String> notAuthors,List<Boolean> notAuthorsFuzzy,//author名字
        //                                         List<String> andFirstAuthors,List<Boolean> andFirstAuthorsFuzzy, List<String> orFirstAuthors,List<Boolean> orFirstAuthorsFuzzy, List<String> notFirstAuthors,List<Boolean> notFirstAuthorsFuzzy,
        //                                         List<String> andInstitutions,List<Boolean> andInstitutionsFuzzy, List<String> orInstitutions, List<Boolean> orInstitutionsFuzzy,List<String> notInstitutions,List<Boolean> notInstitutionsFuzzy,
        //                                         List<String> andAbstracts,List<Boolean> andAbstractsFuzzy, List<String> orAbstracts, List<Boolean> orAbstractsFuzzy,List<String> notAbstracts,List<Boolean> notAbstractsFuzzy,
        //                                         List<String> andDOI,List<Boolean> andDOIFuzzy, List<String> orDOI,List<Boolean> orDOIFuzzy, List<String> notDOI,List<Boolean> notDOIFuzzy,
        //                                         Date startDate, Date endDate


    }


    private void addMatchQueries(BoolQueryBuilder boolQuery, List<String> andList,List<Boolean> andListFuzzy, List<String> orList, List<Boolean> orListFuzzy, List<String> notList,List<Boolean> notListFuzzy, String field) {
        if (andList != null && !andList.isEmpty()) {
            int lenth = andList.size();
            for (int i=0;i<lenth;i++) {
                if(andListFuzzy.get(i)){
                    boolQuery.must(QueryBuilders.fuzzyQuery(field, andList.get(i)).fuzziness(Fuzziness.TWO));
                }else{
                    boolQuery.must(QueryBuilders.matchQuery(field, andList.get(i)));
                }
            }
        }

        if (orList != null && !orList.isEmpty()) {
            int lenth = orList.size();
            for (int i=0;i<lenth;i++) {
                if(orListFuzzy!=null){
                    if(orListFuzzy.get(i)){
                        boolQuery.should(QueryBuilders.fuzzyQuery(field, orList.get(i)).fuzziness(Fuzziness.TWO));
                    }else{
                        boolQuery.should(QueryBuilders.matchQuery(field, orList.get(i)));
                    }
                }else{
                    boolQuery.should(QueryBuilders.matchQuery(field, orList.get(i)));
                }
            }
        }

        if (notList != null && !notList.isEmpty()) {
            int lenth = notList.size();
            for (int i=0;i<lenth;i++) {
                if(notListFuzzy.get(i)){
                    boolQuery.mustNot(QueryBuilders.fuzzyQuery(field, notList.get(i)).fuzziness(Fuzziness.TWO));
                }else{
                    boolQuery.mustNot(QueryBuilders.matchQuery(field, notList.get(i)));
                }
            }
        }

    }








    @Override
    public List<SearchHit<Works>> searchByTitleTest(String title) {
        List<SearchHit<Works>> searchHits = elasticSearchRepository.findByTitle(title);
        return searchHits;
    }

    @Override
    public void searchAssistant(String title) {
        Query bmpQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("title", title))
                .withHighlightFields(new HighlightBuilder.Field("title")
                        .numOfFragments(0))
                .withPageable(PageRequest.of(0, 400)) // 分页设置
                .build();
        org.springframework.data.elasticsearch.core.SearchHits<Works> searchHits = elasticsearchRestTemplate.search(bmpQuery, Works.class);
        // 下面是用于生成关键词表的
        List<Works> worksList = (searchHits.getSearchHits()).stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
        System.out.println(worksList.size());
        elasticWorkMapper.clearSearchWork();
        for(Works work: worksList)
        {
            String work_id = work.getId();
            String type = work.getType();
            Integer year = work.getPublication_year();
            String topic_id = elasticWorkMapper.getTopicIdByWorkId(work_id);
            String keyword = elasticWorkMapper.getKeywordsById(topic_id);
            String institution = elasticWorkMapper.getInstitutionNameByWorkId(work_id);
            elasticWorkMapper.insertSearchWork(work_id, null, type, institution, year);
            ObjectMapper mapper = new ObjectMapper();
            try {
                // 确保 keyword 是一个合法的 JSON 字符串，比如 ["key1", "key2"]
                if (keyword != null && !keyword.isEmpty()) {
                    // 反序列化 JSON 字符串为 List<String>
                    List<String> keywords = mapper.readValue(keyword, new TypeReference<List<String>>() {});
                    // 遍历关键字并逐个更新
                    for (String keywordText : keywords) {
                        if (keywordText != null && !keywordText.isEmpty()) {
                            elasticWorkMapper.updateKeywordText(work_id, keywordText.trim());
                        }
                    }
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                System.err.println("JSON 解析失败: " + e.getMessage());
            }
        }
    }
    public static long count = 0;


    @Override
    public List<SearchHit<Works>> searchByTitleByPage(String title, int page) {
        // 每页显示的记录数
        int pageSize = 20;
        int from = (page - 1) * pageSize; // 起始记录

        // 使用 Elasticsearch 的分页功能
        Query query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("title", title))
                .withHighlightFields(new HighlightBuilder.Field("title")
                        .preTags("<span style='color:red'>")
                        .postTags("</span>")
                        .numOfFragments(0))
                .withPageable(PageRequest.of(page - 1, pageSize)) // 分页设置
                .build();
        count = 0;
        try{
            Query tmpQuery = new NativeSearchQueryBuilder()
                    .withQuery(QueryBuilders.matchQuery("title", title))
                    .withHighlightFields(new HighlightBuilder.Field("title")
                            .preTags("<span style='color:red'>")
                            .postTags("</span>")
                            .numOfFragments(0))
                    .build();
            org.springframework.data.elasticsearch.core.SearchHits<Works> tmp = elasticsearchRestTemplate.search(query, Works.class);
            count = tmp.getTotalHits();
        }catch (Exception e){
            count = 10000;
        }
        org.springframework.data.elasticsearch.core.SearchHits<Works> searchHits = elasticsearchRestTemplate.search(query, Works.class);

        // 返回当前页的搜索结果
        return searchHits.getSearchHits();
    }





    @Override
    public List<SearchHit<Works>> searchByTitle(String title) {
        List<SearchHit<Works>> searchHits = elasticSearchRepository.findByTitle(title);

        return searchHits;
    }

    @Override
    public int getLenthOfFindTitleOrKeywordsTextOrAbstract(String searchTerm) {
        List<SearchHit<Works>> hits = elasticSearchRepository.findByTitleOrKeywordsTextOrAbstract(searchTerm);
        List<Works> worksList = hits.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
        elasticWorkMapper.clearSearchWork();
        int cnt = 0;
        for(Works work: worksList)
        {
            if(cnt <= 100000)
            {
                String publicationid = work.getId();
                String type = work.getType();
                String language = work.getLanguage();
                Integer year = work.getPublication_year();
                List<String> keywordsText = new ArrayList<>();
                String keyword = work.getKeywords();
                ObjectMapper mapper = new ObjectMapper();
                try {
                    List<Map<String, Object>> keywordMaps = mapper.readValue(keyword, List.class);
                    System.out.println(keywordMaps);
                    for (Map<String, Object> keywordMap : keywordMaps) {
                        elasticWorkMapper.insertSearchWork(publicationid, (String) keywordMap.get("display_name"), type, language, year);
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                cnt++;
            }
            else{
                break;
            }
        }
        return hits.size();
    }

    @Override
    public List<SearchHit<Works>> findByTitleOrKeywordsTextOrAbstract(String searchTerm,int pageIndex) {
        List<SearchHit<Works>> hits = elasticSearchRepository.findByTitleOrKeywordsTextOrAbstract(searchTerm);
        List<SearchHit<Works>> ans = new ArrayList<>();
        //从第一页开始
        for(int i=pageIndex*20-20;i<pageIndex*20&&i<hits.size();i++){
            ans.add(hits.get(i));
        }
        return ans;
    }




    @Override
    public Json AutoCompleteWithCompletionSuggester(String searchContent) throws IOException {


        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("116.204.112.5", 9200, "http")));

        try {
            // 创建SearchRequest
            SearchRequest searchRequest = new SearchRequest("works_index");
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

            // 创建SuggestBuilder
            SuggestBuilder suggestBuilder = new SuggestBuilder();

            // 添加title_suggest
            CompletionSuggestionBuilder titleSuggestion = SuggestBuilders.completionSuggestion("display_name.suggest_field")
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
    public Json AutoFixSuggester(String searchContent) throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http")));



        try {
            SearchRequest searchRequest = new SearchRequest("openalexworksindexaddingcompletion1");
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            //	建议模式（控制提供建议词的方式）：
        //1. missing：默认方式，仅在‘要搜索词项’不在索引中存在时，才提供建议词；
        //2. popular：仅提供频率比‘要搜索词项’高的建议词；
        //3. always：总是提供建议词；
            TermSuggestionBuilder titleSuggestion = SuggestBuilders.termSuggestion("title.keyword")
                    .suggestMode(TermSuggestionBuilder.SuggestMode.ALWAYS)
                    .text(searchContent)
//                    .analyzer( "iksmart")
                    .size(5); // 设定返回数量

            TermSuggestionBuilder abstractSuggestion = SuggestBuilders.termSuggestion("abstract.keyword")
                    .suggestMode(TermSuggestionBuilder.SuggestMode.ALWAYS)
                    .text(searchContent)
//                    .analyzer( "iksmart")
                    .size(5); // 设定返回数量

            TermSuggestionBuilder displaynameSuggestion = SuggestBuilders.termSuggestion("displayname.keyword")
                    .suggestMode(TermSuggestionBuilder.SuggestMode.ALWAYS)
                    .text(searchContent)
                    .size(5); // 设定返回数量
//                .analyzer( "iksmart")
            TermSuggestionBuilder keywordstextSuggestion = SuggestBuilders.termSuggestion("keywordstext.keyword")
                    .suggestMode(TermSuggestionBuilder.SuggestMode.ALWAYS)
                    .text(searchContent)
//                    .analyzer( "iksmart")
                    .size(5); // 设定返回数量

            SuggestBuilder suggestBuilder = new SuggestBuilder();
            suggestBuilder.addSuggestion("titleSuggestion", titleSuggestion)
                    .addSuggestion("displaynameSuggestion",displaynameSuggestion)
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
            SearchRequest searchRequest = new SearchRequest("openalexworksindexaddingcompletion1");
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

            // 创建SuggestBuilder
            SuggestBuilder suggestBuilder = new SuggestBuilder();

            // 添加titlesuggest
            CompletionSuggestionBuilder titleSuggestion = SuggestBuilders.completionSuggestion("title.suggestfield")
                    .prefix(searchContent,(new FuzzyOptions.Builder()
                            .setFuzziness(1) // 设置模糊度为1
                            .setFuzzyMinLength(3) // 设置最小长度为3
                            .setFuzzyPrefixLength(1) // 设置前缀长度为1
                            .setTranspositions(true)// 允许转置
                            .build()))
                    .size(10)
                    .skipDuplicates(true)
                    .analyzer("iksmart");


            suggestBuilder.addSuggestion("titlesuggest", titleSuggestion);


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
            SearchRequest searchRequest = new SearchRequest("openalexworksindexaddingcompletion1");
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

            // 创建SuggestBuilder
            SuggestBuilder suggestBuilder = new SuggestBuilder();


            // 添加abstractSuggest
            CompletionSuggestionBuilder abstractSuggestion = SuggestBuilders.completionSuggestion("abstract.suggestfield")
                    .prefix(searchContent,(new FuzzyOptions.Builder()
                            .setFuzziness(1) // 设置模糊度为1
                            .setFuzzyMinLength(3) // 设置最小长度为3
                            .setFuzzyPrefixLength(1) // 设置前缀长度为1
                            .setTranspositions(true)// 允许转置
                            .build()))
                    .size(10)
                    .skipDuplicates(true)
                    .analyzer("iksmart");
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
            SearchRequest searchRequest = new SearchRequest("openalexworksindexaddingcompletion1");
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

            // 创建SuggestBuilder
            SuggestBuilder suggestBuilder = new SuggestBuilder();

            // 添加keywordstextSuggest
            CompletionSuggestionBuilder keywordsTextSuggestion = SuggestBuilders.completionSuggestion("keywordstext.suggestfield")
                    .prefix(searchContent,(new FuzzyOptions.Builder()
                            .setFuzziness(1) // 设置模糊度为1
                            .setFuzzyMinLength(3) // 设置最小长度为3
                            .setFuzzyPrefixLength(1) // 设置前缀长度为1
                            .setTranspositions(true)// 允许转置
                            .build()))
                    .size(10)
                    .skipDuplicates(true)
                    .analyzer("iksmart");
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

    @Override
    public Json AutoCompleteAuthorWithCompletionSuggester(String searchContent) throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("116.204.112.5", 9200, "http")));

        try {
            // Create SearchRequest
            SearchRequest searchRequest = new SearchRequest("authors_index");
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

            // Create SuggestBuilder
            SuggestBuilder suggestBuilder = new SuggestBuilder();

            // Add author_suggest
            CompletionSuggestionBuilder authorSuggestion = SuggestBuilders.completionSuggestion("display_name.suggest_field")
                    .prefix(searchContent)
                    .size(10)
                    .skipDuplicates(true)
                    .analyzer("ik_smart");

            suggestBuilder.addSuggestion("author_suggest", authorSuggestion);

            // Set SuggestBuilder to SearchSourceBuilder
            sourceBuilder.suggest(suggestBuilder);

            // Set SearchSourceBuilder to SearchRequest
            searchRequest.source(sourceBuilder);

            // Execute search request
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            // Process search results
            Suggest suggest = searchResponse.getSuggest();

            return new Json(suggest.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close client
            try {
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }


    @Cacheable(value = "worksByTitleCache", key = "#title")
    @Override
    public List<SearchHit<Works>> searchByTitleAll(String title) {

        // 使用 Elasticsearch 的分页功能
        Query query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("title", title))
                .withHighlightFields(new HighlightBuilder.Field("title")
                        .preTags("<span style='color:red'>")
                        .postTags("</span>")
                        .numOfFragments(0))
                .build();
        count = 0;
        try{
            Query tmpQuery = new NativeSearchQueryBuilder()
                    .withQuery(QueryBuilders.matchQuery("title", title))
                    .withHighlightFields(new HighlightBuilder.Field("title")
                            .preTags("<span style='color:red'>")
                            .postTags("</span>")
                            .numOfFragments(0))
                    .build();
            org.springframework.data.elasticsearch.core.SearchHits<Works> tmp = elasticsearchRestTemplate.search(query, Works.class);
            count = tmp.getTotalHits();
        }catch (Exception e){
            count = 10000;
        }
        org.springframework.data.elasticsearch.core.SearchHits<Works> searchHits = elasticsearchRestTemplate.search(query, Works.class);

        // 返回当前页的搜索结果
        return searchHits.getSearchHits();
    }

    @Override
    public List<SearchHit<Works>> searchByYear(String title){
        return null;
    }


}
