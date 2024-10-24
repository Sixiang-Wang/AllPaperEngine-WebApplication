package com.example.scholar.service.impl;

import com.example.scholar.dao.ConceptsMapper;
import com.example.scholar.domain.openalex.Concepts;
import com.example.scholar.domain.openalex.WorksConcepts;
import com.example.scholar.dto.ConceptsResultDto;
import com.example.scholar.service.ConceptsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
@Service("conceptsService")
public class ConceptsServiceImpl implements ConceptsService {
    @Resource
    private ConceptsMapper conceptsMapper;
    @Override
    public List<ConceptsResultDto> getConceptsByWorkId(String workId) {
        List<ConceptsResultDto> conceptsResultDtoList = new ArrayList<ConceptsResultDto>();
        List<WorksConcepts> conceptsList = conceptsMapper.getWorksConceptsListById(workId);
        for(WorksConcepts worksConcepts:conceptsList){
            Concepts concepts = conceptsMapper.selectConceptsById(worksConcepts.getConceptId());
            ConceptsResultDto conceptsResultDto = new ConceptsResultDto();
            conceptsResultDto.setConceptsId(concepts.getId());
            conceptsResultDto.setDescription(concepts.getDescription());
            conceptsResultDto.setLevel(concepts.getLevel());
            conceptsResultDto.setWikiurl(concepts.getWikidata());
            conceptsResultDto.setDisplayName(concepts.getDisplayName());
            conceptsResultDto.setImgUrl(concepts.getImageUrl());
            conceptsResultDto.setSmallImgUrl(concepts.getImageThumbnailUrl());
            conceptsResultDto.setCitedByCount(concepts.getCitedbyCount());
            conceptsResultDto.setWorksCount(concepts.getWorksCount());
            conceptsResultDtoList.add(conceptsResultDto);
        }
        return conceptsResultDtoList;
    }
}
