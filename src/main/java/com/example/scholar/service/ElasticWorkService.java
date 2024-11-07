package com.example.scholar.service;


import com.example.scholar.domain.openalexElasticsearch.Works;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;


@Component
public interface ElasticWorkService {

    /**
     * 根据标题查询并高亮显示
     * @param title 标题
     * @return 返回高亮显示的结果集
     */
    List<SearchHit<Works>> searchByTitle(String title);



    List<SearchHit<Works>> findByTitleOrKeywordsTextOrAbstract(String searchTerm);

    List<SearchHit<Works>> fuzzyAutocomplete(String value, String fuzziness, boolean transpositions, int prefixLength);
}
