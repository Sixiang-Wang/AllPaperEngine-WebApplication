package com.example.scholar.service.impl;

import com.example.scholar.dao.InstitutionMapper;
import com.example.scholar.dao.InstitutionsMapper;
import com.example.scholar.domain.openalex.Institutions;
import com.example.scholar.domain.openalex.Work;
import com.example.scholar.service.InstitutionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("institutionService")
public class InstitutionServiceImpl implements InstitutionService {

    @Resource
    private InstitutionMapper institutionMapper;

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
}
