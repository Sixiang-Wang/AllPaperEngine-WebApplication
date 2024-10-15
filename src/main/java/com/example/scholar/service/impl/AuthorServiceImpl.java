package com.example.scholar.service.impl;

import com.example.scholar.dao.AuthorMapper;
import com.example.scholar.domain.openalex.Author;
import com.example.scholar.domain.openalex.AuthorShips;
import com.example.scholar.dto.AuthorResultDto;
import com.example.scholar.dto.WorkAuthorResultDto;
import com.example.scholar.service.AuthorService;
import com.example.scholar.util.AuthorNameRestore;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("authorService")
public class AuthorServiceImpl implements AuthorService {
    @Resource
    private AuthorMapper authorMapper;
    @Override
    public ArrayList<WorkAuthorResultDto> getAuthorsByWorkId(String workId) {
        List<AuthorShips> authorships = authorMapper.selectAuthorsById(workId);
        System.out.print(authorships);
        ArrayList<WorkAuthorResultDto> authorResultDtos = new ArrayList<WorkAuthorResultDto>();
        for(AuthorShips authorShip:authorships){
            if(authorShip!=null){
                WorkAuthorResultDto workAuthorResultDto = new WorkAuthorResultDto();
                workAuthorResultDto.setPosition(authorShip.getAuthor_position());
                Author author = authorMapper.selectAuthorById(authorShip.getAuthor_id());
                AuthorResultDto authorResultDto = new AuthorResultDto();
                authorResultDto.setAuthorName(AuthorNameRestore.restoreAuthorName(author.getDisplay_name_alternatives()));
                authorResultDto.setAuthorId(author.getId());
                authorResultDto.setWorks_count(author.getWorks_count());
                authorResultDto.setWorks_api_url(author.getWorks_api_url());
                authorResultDto.setCited_by_count(author.getCited_by_count());
                workAuthorResultDto.setAuthorResultDto(authorResultDto);
                authorResultDtos.add(workAuthorResultDto);
            }
        }
        return authorResultDtos;
    }
}
