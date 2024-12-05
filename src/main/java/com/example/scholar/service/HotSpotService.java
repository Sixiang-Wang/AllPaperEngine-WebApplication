package com.example.scholar.service;

import com.example.scholar.dto.HottestKeywordsResult;

import java.util.List;

public interface HotSpotService {

    //关键词keywords分析
    /**
     * Name:GetHottestKeywords
     * Description:根据关键词对应文章总数排名
     * Params: Integer number Integer publicationYear
     */
    List<HottestKeywordsResult> GetHottestKeywords(int number,int publicationYearFrom,int publicationYearTo);







}
