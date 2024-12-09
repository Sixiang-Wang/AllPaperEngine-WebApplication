package com.example.scholar.service.impl;

import com.example.scholar.dao.SearchedWorkMapper;
import com.example.scholar.domain.openalexElasticsearch.Works;
import com.example.scholar.service.ConnectiveService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("connectiveService")
public class ConnectiveServiceImpl implements ConnectiveService {
    @Resource
    private SearchedWorkMapper searchedWorkMapper;

    @Override
    public List<String> sortKeywords(){
        List<String> results = searchedWorkMapper.getCollectiveNum();
        return results;
    }

    @Override
    public List<Works> getWorksByKeyword(String keyword){
        List<String> workIds = searchedWorkMapper.getWorksByKeyword(keyword);
        List<Works> works = searchedWorkMapper.getWorksByIds(workIds);
        return works;
    }


}
