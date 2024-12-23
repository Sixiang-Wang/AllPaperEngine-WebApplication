package com.example.scholar.service.impl;

import com.example.scholar.dao.SuggestMapper;
import com.example.scholar.domain.WorkConcept;
import com.example.scholar.dto.SuggestDto;
import com.example.scholar.service.SuggestService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("suggestService")
public class SuggestServiceImpl implements SuggestService {
    @Resource
    private SuggestMapper suggestMapper;

    @Override
    @Cacheable(value = "suggestWorksCache", key = "#workId")
    public List<SuggestDto> getSuggestWorks(String workId) {
        List<String> conceptIds = suggestMapper.getConceptIdByWorkId(workId);
        List<WorkConcept> list = new ArrayList<>();
        for (String str : conceptIds) {
            List<WorkConcept> tmpList = suggestMapper.getWorkConceptById(str);
            list.addAll(tmpList);
        }
        List<SuggestDto> res = new ArrayList<>();
        for (WorkConcept workConcept : list) {
            SuggestDto suggestDto = new SuggestDto();
            suggestDto.setDate(workConcept.getWork().getPublicationDate());
            suggestDto.setTitle(workConcept.getWork().getTitle());
            res.add(suggestDto);
        }
        return res.subList(0, 5);
    }
}

