package com.example.scholar.service;


import com.example.scholar.domain.openalex.Institutions;
import com.example.scholar.domain.openalex.Work;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface InstitutionService {



    List<Institutions> getTopNMostCitedInstitution(int number);
    Institutions getInstitutionById(String id);
    List<Institutions> getInstitutionByName(String name);
    List<String> getInstitutionIdByName(String name);
    List<Work> getWorksByInstitutions(String id);


}
