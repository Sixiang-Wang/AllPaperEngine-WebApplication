package com.example.scholar.service;


import com.example.scholar.domain.openalexElasticsearch.Authors;
import com.example.scholar.domain.openalexElasticsearch.Works;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Response;
import org.elasticsearch.search.suggest.Suggest;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import springfox.documentation.spring.web.json.Json;

import java.io.IOException;
import java.util.Date;
import java.util.List;


@Component
public interface ElasticWorkService {


    List<SearchHit<Works>> searchByTitleTest(String title);
    List<SearchHit<Works>> searchByTitleByPage(String title, int page);

    List<SearchHit<Works>> searchByTitle(String title);

    void searchAssistant(String title);

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




    Json AutoCompleteAuthorWithCompletionSuggester(String searchContent) throws IOException;

    /**
     * AdvancedSearch param:每个类型三个列表 分别是 AndTopic OrTopic NotTopic
     * 如果有条件title就先过一遍title
     *
     *
     *
     *
     *
     *
     */

    SearchResponse advancedSearch(
            List<String> andTitles,List<Boolean> andTitlesFuzzy, List<String> orTitles,List<Boolean> orTitlesFuzzy, List<String> notTitles,List<Boolean> notTitlesFuzzy,
            List<String> andTopics,List<Boolean> andTopicsFuzzy, List<String> orTopics,List<Boolean> orTopicsFuzzy, List<String> notTopics,List<Boolean> notTopicsFuzzy,
            List<String> andAuthors,List<Boolean> andAuthorsFuzzy, List<String> orAuthors,List<Boolean> orAuthorsFuzzy, List<String> notAuthors,List<Boolean> notAuthorsFuzzy,//author名字
            List<String> andFirstAuthors,List<Boolean> andFirstAuthorsFuzzy, List<String> orFirstAuthors,List<Boolean> orFirstAuthorsFuzzy, List<String> notFirstAuthors,List<Boolean> notFirstAuthorsFuzzy,
            List<String> andInstitutions,List<Boolean> andInstitutionsFuzzy, List<String> orInstitutions, List<Boolean> orInstitutionsFuzzy,List<String> notInstitutions,List<Boolean> notInstitutionsFuzzy,
            List<String> andAbstracts,List<Boolean> andAbstractsFuzzy, List<String> orAbstracts, List<Boolean> orAbstractsFuzzy,List<String> notAbstracts,List<Boolean> notAbstractsFuzzy,
            List<String> andDOI,List<Boolean> andDOIFuzzy, List<String> orDOI,List<Boolean> orDOIFuzzy, List<String> notDOI,List<Boolean> notDOIFuzzy,
            java.sql.Date startDate, java.sql.Date endDate) throws IOException;





    public List<SearchHit<Works>> searchByTitleAll(String title);

    public List<SearchHit<Works>> searchByYear(String title);

}
