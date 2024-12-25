package com.example.scholar.service.impl;

import com.example.scholar.dao.AuthorMapper;
import com.example.scholar.dao.InstitutionMapper;
import com.example.scholar.dao.WorkMapper;
import com.example.scholar.domain.openalex.Author;
import com.example.scholar.domain.openalex.AuthorShips;
import com.example.scholar.domain.openalex.Institutions;
import com.example.scholar.domain.openalex.Work;
import com.example.scholar.dto.AuthorResultDto;
import com.example.scholar.dto.AuthorSpecificResultDto;
import com.example.scholar.dto.InstitutionsResultDto;
import com.example.scholar.dto.WorkAuthorResultDto;
import com.example.scholar.service.AdminService;
import com.example.scholar.service.AuthorService;
import com.example.scholar.util.AuthorNameRestore;
import com.example.scholar.util.JsonDisposer;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@Service("authorService")
public class AuthorServiceImpl implements AuthorService {
    @Resource
    private AuthorMapper authorMapper;
    @Resource
    private AuthorService authorService;
    @Resource
    private WorkMapper workMapper;
    @Resource
    private InstitutionMapper institutionMapper;

    @Override
    public Author getAuthorById(String id) {
        return authorMapper.selectAuthorById(id);
    }

    @Override
    public AuthorSpecificResultDto getSpecificAuthorById(String id) {
        Author author = authorMapper.selectAuthorById(id);
        AuthorSpecificResultDto authorSpecificResultDto = new AuthorSpecificResultDto();

        if(author!=null){
            authorSpecificResultDto.setAuthor(author);

            String institutionId = authorMapper.getInstitutionIdByAuthorId(author.getId());

            Institutions institutions = institutionMapper.getInstitutionById(institutionId);
            if(institutions!=null){
                authorSpecificResultDto.setInstitutions(institutions);
            }

            return authorSpecificResultDto;
        }else{
            return null;
        }
    }


    @Override
    @Cacheable(value = "getAuthorsByWorkIdCache", key = "#workId")
    public ArrayList<WorkAuthorResultDto> getAuthorsByWorkId(String workId) {
        List<AuthorShips> authorships = authorMapper.selectAuthorsById(workId);
//        System.out.print(authorships);
        ArrayList<WorkAuthorResultDto> authorResultDtos = new ArrayList<WorkAuthorResultDto>();
        if (authorships != null) {
            for (AuthorShips authorShip : authorships) {
                if (authorShip != null) {
                    WorkAuthorResultDto workAuthorResultDto = new WorkAuthorResultDto();
                    workAuthorResultDto.setPosition(authorShip.getAuthorPosition());
//                    workAuthorResultDto.setInstitutions(JsonDisposer.disposeInstitutions(authorShip.getInstitutions()));
                    workAuthorResultDto.setInstitution(authorShip.getInstitution().toDto());
                    Author author = authorMapper.selectAuthorById(authorShip.getAuthorId());
                    if (author != null) {
                        AuthorResultDto authorResultDto = new AuthorResultDto();
                        if (author.getDisplayNameAlternatives() != null) {
                            authorResultDto.setAuthorName(author.getDisplayName());
                        }
                        if (author.getId() != null) {
                            authorResultDto.setAuthorId(author.getId());
                        }
                        authorResultDto.setWorksCount(author.getWorksCount());

                        if (author.getWorksApiUrl() != null) {
                            authorResultDto.setWorksApiUrl(author.getWorksApiUrl());
                        }
                        authorResultDto.setCitedByCount(author.getCitedByCount());

                        workAuthorResultDto.setAuthorResultDto(authorResultDto);
                    }
                    authorResultDtos.add(workAuthorResultDto);
                }
            }
        }
        return authorResultDtos;
    }




    @Override
    public List<String> getAuthorIdByAuthorName(String authorName) {
        return authorMapper.getAuthorIdByAuthorName(authorName);
    }


    @Override
    public List<Work> getWorksByAuthorId(String authorId) {
        List<String> workIds = authorMapper.getWorkIdsByAuthorId(authorId);
        List<Work> works = new ArrayList<>();

        works = workMapper.getWorksByWorkIds(workIds);
        works.sort(Comparator.comparingInt(Work::getCitedByCount).reversed());
        return works;
    }

    @Override
    public int getWorksCountByAuthorId(String authorId) {
        Author author = authorMapper.selectAuthorById(authorId);
        return author.getWorksCount();
    }

    @Override
    @Cacheable(value = "getHighQualityWorksByAuthorIdCache", key = "#authorId")
    public List<Work> getHighQualityWorksByAuthorId(String authorId) {
        List<String> workIds = authorMapper.getWorkIdsByAuthorId(authorId);
        List<Work> works = new ArrayList<>();
        for (String workId : workIds) {
            Work work = workMapper.getWorkById(workId);
            if (work != null) {
                if(work.getCitedByCount()>1000){
                    works.add(work);
                }
            }
        }
        works.sort(Comparator.comparingInt(Work::getCitedByCount).reversed());
        return works;
    }

    @Override
    public int getHighQualityWorksCountByAuthorId(String authorId,boolean track) {
        Author author = authorMapper.selectAuthorById(authorId);
        List<String> workIds = authorMapper.getWorkIdsByAuthorId(authorId);
        List<Work> works = new ArrayList<>();
        for (String workId : workIds) {
            Work work = workMapper.getWorkById(workId);
            if (work != null) {
                if(work.getCitedByCount()>1000){
                    works.add(work);
                }
            }
        }
        return works.size();
    }




    @Override
    public int getCitedCountByAuthorId(String authorId) {
        //先判断init列是不是false，如果是true那就继续，如果不是就跳过直接搜
        Author author = authorMapper.selectAuthorById(authorId);
        return author.getCitedByCount();
    }

    @Override
    public int getHNumberByAuthorId(String authorId,boolean track) {
        Author author = authorMapper.selectAuthorById(authorId);

        List<String> workIds = authorMapper.getWorkIdsByAuthorId(authorId);
        List<Work> works = new ArrayList<>();
        works.sort(Comparator.comparingInt(Work::getCitedByCount).reversed());
        int i=0;
        for(i=0;i<works.size();i++) {
            if (works.get(i).getCitedByCount() >= (i + 1)) {
                continue;
            }
        }
        return i;
    }



    @Override
    public List<Work> getFirstPublishWorkByAuthorId(String authorId) {
        List<String> workIds = authorMapper.getWorkIdsByFirstAuthorId(authorId);
        List<Work> works = new ArrayList<>();
        for (String workId : workIds) {
            Work work = workMapper.getWorkById(workId);
            if (work != null) {
                works.add(work);
            }
        }
        works.sort(Comparator.comparingInt(Work::getCitedByCount).reversed());
        return works;
    }

    @Override
    public int getFirstPublishWorkCountByAuthorId(String authorId,boolean track) {
        Author author = authorMapper.selectAuthorById(authorId);

        List<String> workIds = authorMapper.getWorkIdsByFirstAuthorId(authorId);
//        List<Work> works = new ArrayList<>();
//        for (String workId : workIds) {
//            Work work = workMapper.getWorkById(workId);
//            if (work != null) {
//                works.add(work);
//            }
//        }
//        works.sort(Comparator.comparingInt(Work::getCitedByCount).reversed());

        return workIds.size();

    }

    @Override
    public List<Work> getWorksByAuthorName(String authorName) {
        return null;
    }

    @Override
    public List<AuthorResultDto> getAuthorsByName(String name) {
        List<Author> list = authorMapper.getAuthorsByName(name);
        List<AuthorResultDto> res = new ArrayList<>();
        for (Author author : list) {
            AuthorResultDto authorResultDto = new AuthorResultDto();
            authorResultDto.setAuthorId(author.getId());
            authorResultDto.setAuthorName(author.getDisplayName());
            authorResultDto.setWorksCount(author.getWorksCount());
            authorResultDto.setCitedByCount(author.getCitedByCount());
            authorResultDto.setWorksApiUrl(author.getWorksApiUrl());
            res.add(authorResultDto);
        }
        return res;
    }

    @Override
    public List<AuthorResultDto> get100AuthorsByName(String name) {
        List<Author> list = authorMapper.get100AuthorsByName(name);
        List<AuthorResultDto> res = new ArrayList<>();

        for (Author author : list) {
            AuthorResultDto authorResultDto = new AuthorResultDto();
            authorResultDto.setAuthorId(author.getId());
            authorResultDto.setAuthorName(author.getDisplayName());
            authorResultDto.setWorksCount(author.getWorksCount());
            authorResultDto.setCitedByCount(author.getCitedByCount());
            authorResultDto.setWorksApiUrl(author.getWorksApiUrl());
            res.add(authorResultDto);
        }
        return res;
    }
}
