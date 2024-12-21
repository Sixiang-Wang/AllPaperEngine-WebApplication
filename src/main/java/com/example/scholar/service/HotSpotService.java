package com.example.scholar.service;

import com.example.scholar.dto.HottestKeywordsResult;
import com.example.scholar.dto.TopicResultDto;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public interface HotSpotService {

    //关键词keywords分析
    /**
     * Name:GetHottestKeywords
     * Description:根据关键词对应文章总数排名
     * Params: Integer number Integer publicationYear
     */
    List<HottestKeywordsResult> GetHottestKeywords(int number,int publicationYearFrom,int publicationYearTo);


    //热点领域分析
    /**
     * Name:getTopicsBySubfield
     * Description:根据领域、学科、子学科获取热点领域topics及其works_count
     * Params: String domainDisplayName String fieldDisplayName String subfieldDisplayName
     */
    List<TopicResultDto> getTopicsBySubfield(String domainDisplayName, String fieldDisplayName, String subfieldDisplayName);





}
