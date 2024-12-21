package com.example.scholar.service.impl;

import com.example.scholar.dao.KeywordMapper;
import com.example.scholar.dto.HottestKeywordsResult;
import com.example.scholar.dto.TopicResultDto;
import com.example.scholar.service.HotSpotService;
import com.example.scholar.util.TopicAnalyzer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("HotSpotService")
public class HotSpotServiceImpl implements HotSpotService {


    @Resource
    private KeywordMapper keywordMapper;
    @Override
    public List<HottestKeywordsResult> GetHottestKeywords(int number,int publicationYearFrom,int publicationYearTo) {
        List<HottestKeywordsResult> hottestKeywordsResults = keywordMapper.getTopNKeywordsByScore(publicationYearFrom,publicationYearTo,number);
        System.out.print(hottestKeywordsResults);
        return hottestKeywordsResults;
    }
    @Override
    public List<TopicResultDto> getTopicsBySubfield(String domainDisplayName, String fieldDisplayName, String subfieldDisplayName) {
        TopicAnalyzer analyzer = TopicAnalyzer.getInstance();
        Map<String, TopicAnalyzer.Domain> analysisResult = analyzer.getAnalysisResult();

        List<TopicResultDto> topics = new ArrayList<>();
        TopicAnalyzer.Domain domain = analysisResult.get(domainDisplayName);
        if (domain != null) {
            TopicAnalyzer.Field field = domain.fields.get(fieldDisplayName);
            if (field != null) {
                TopicAnalyzer.Subfield subfield = field.subfields.get(subfieldDisplayName);
                if (subfield != null) {
                    for (TopicAnalyzer.Topic topic : subfield.topics.values()) {
                        TopicResultDto dto = new TopicResultDto();
                        dto.setTopicName(topic.name);
                        dto.setWorksCount(topic.worksCount);
                        topics.add(dto);
                    }
                }
            }
        }
        return topics;
    }
}
