package com.example.scholar.service.impl;

import com.example.scholar.dao.AuthorMapper;
import com.example.scholar.dao.InstitutionMapper;
import com.example.scholar.dao.InstitutionsMapper;
import com.example.scholar.domain.openalex.Author;
import com.example.scholar.domain.openalex.Institutions;
import com.example.scholar.domain.openalex.Work;
import com.example.scholar.dto.AuthorSpecificResultDto;
import com.example.scholar.service.InstitutionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("institutionService")
public class InstitutionServiceImpl implements InstitutionService {

    @Resource
    private InstitutionMapper institutionMapper;

    @Resource
    private AuthorMapper authorMapper;

    @Override
    public List<Institutions> getTopNMostCitedInstitution(int number) {
        return List.of();
    }

    @Override
    public Institutions getInstitutionById(String id) {
        return institutionMapper.getInstitutionById(id);
    }

    @Override
    public List<Institutions> getInstitutionByName(String name) {
        return institutionMapper.getInstitutionByName(name);
    }

    @Override
    public List<String> getInstitutionIdByName(String name) {
        return institutionMapper.getInstitutionIdByName(name);
    }

    @Override
    public List<Work> getWorksByInstitutions(String id) {
        return List.of();
    }

    @Override
    public List<Author> getAuthorByInstitutionId(String institutionId) {
        return institutionMapper.getAuthorByInstitutionId(institutionId);
    }

    @Override
    public List<String> getAuthorIdByInstitutionId(String institutionId) {
        return institutionMapper.getAuthorIdByInstitutionId(institutionId);
    }

    @Override
    public List<AuthorSpecificResultDto> getdtoList(String institutionId, List<String> authorIdList) {
        List<AuthorSpecificResultDto> resultDtoList = new ArrayList<>();
        Institutions institutionById = institutionMapper.getInstitutionById(institutionId);
//        for (String authorId : authorIdList) {
//            Author author = authorMapper.selectAuthorById(authorId);
//            resultDtoList.add(new AuthorSpecificResultDto(author, institutionById));
//        }
        List <Author> authorList = authorMapper.selectAuthorByIdList(authorIdList);
        for (Author author : authorList) {
            resultDtoList.add(new AuthorSpecificResultDto(author, institutionById));
        }
        return resultDtoList;
    }
}
