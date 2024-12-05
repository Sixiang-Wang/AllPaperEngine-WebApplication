package com.example.scholar.service.impl;

import com.example.scholar.dao.KeywordMapper;
import com.example.scholar.dto.HottestKeywordsResult;
import com.example.scholar.service.HotSpotService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
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
}
