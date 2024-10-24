package com.example.scholar.service.impl;


import com.example.scholar.dao.InstitutionsMapper;
import com.example.scholar.domain.openalex.Institutions;
import com.example.scholar.service.InstitutionsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("institutionsService")
public class InstitutionsServiceImpl implements InstitutionsService {
    @Resource
    private InstitutionsMapper institutionsMapper;
    @Override
    public Institutions getInstitutionById(String institutionId) {
        return institutionsMapper.selectInstitutionsById(institutionId);
    }
}
