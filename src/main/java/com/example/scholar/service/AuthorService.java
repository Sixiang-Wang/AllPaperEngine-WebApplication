package com.example.scholar.service;

import com.example.scholar.domain.openalex.AuthorShips;
import com.example.scholar.dto.AuthorResultDto;
import com.example.scholar.dto.WorkAuthorResultDto;

import java.util.ArrayList;
import java.util.List;

public interface AuthorService {
    ArrayList<WorkAuthorResultDto> getAuthorsByWorkId(String workId);
}
