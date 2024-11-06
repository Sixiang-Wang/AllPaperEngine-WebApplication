package com.example.scholar.service.impl;

import com.example.scholar.domain.openalexElasticsearch.Works;
import com.example.scholar.repository.ElasticSearchRepository;
import com.example.scholar.service.ElasticWorkService;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service("ElasticWorkService")
public class ElasticWorksServiceImpl implements ElasticWorkService {


    @Resource
    private ElasticSearchRepository elasticSearchRepository;

    @Override
    public List<SearchHit<Works>> searchByTitle(String title) {
        return elasticSearchRepository.findByTitle(title);
    }
}
