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
    public List<String> sortTypes(){
        List<String> results = searchedWorkMapper.getTypeNum();
        return results;
    }

    @Override
    public List<String> sortInstitutions()
    {
        List<String> results = searchedWorkMapper.getInstitutionNum();
        return results;
    }

    @Override
    public List<Integer> sortPublictionYears()
    {
        List<Integer> results = searchedWorkMapper.getPublictionYearsNum();
        return results;
    }

    @Override
    public List<Works> getWorksByKeyword(String keyword){
        List<String> workIds = searchedWorkMapper.getWorksByKeyword(keyword);
        List<Works> works = searchedWorkMapper.getWorksByIds(workIds);
        return works;
    }


    @Override
    public List<Works> getWorksByType(String type){
        List<String> workIds = searchedWorkMapper.getWorksByType(type);
        List<Works> works = searchedWorkMapper.getWorksByIds(workIds);
        return works;
    }

    @Override
    public List<Works> getWorksByInstitution(String institution){
        List<String> workIds = searchedWorkMapper.getWorksByInstitution(institution);
        List<Works> works = searchedWorkMapper.getWorksByIds(workIds);
        return works;
    }

    @Override
    public List<Works> getWorksByPublicationYear(int year){
        List<String> workIds = searchedWorkMapper.getWorksByPublictionYears(year);
        List<Works> works = searchedWorkMapper.getWorksByIds(workIds);
        return works;
    }

    @Override
    public List<Works> getWorksByConditions(String keyword, String type, String institution, Integer year) {
        List<String> workIds = searchedWorkMapper.getWorksByConditions(keyword, type, institution, year);

        if (workIds != null && !workIds.isEmpty()) {
            return searchedWorkMapper.getWorksByIds(workIds);
        }
        return new ArrayList<>(); // 没有结果返回空列表
    }

}
