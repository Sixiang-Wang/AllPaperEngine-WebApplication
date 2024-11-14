package com.example.scholar.repository;

import com.example.scholar.domain.openalexElasticsearch.Works;
import com.example.scholar.domain.openalexElasticsearch.WorksCompletion;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElasticSearchCompletionRepository extends ElasticsearchRepository<WorksCompletion, String> {
}
