package com.example.scholar.service;

import com.example.scholar.domain.openalex.Institutions;
import com.example.scholar.dto.WorkSpecificResultDto;

public interface InstitutionsService {


    Institutions getInstitutionById(String institutionId);


}
