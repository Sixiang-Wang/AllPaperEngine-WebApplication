package com.example.scholar.service.impl;

import com.example.scholar.dao.AuthorMapper;
import com.example.scholar.dao.InstitutionMapper;
import com.example.scholar.dao.WorkMapper;
import com.example.scholar.domain.openalex.Author;
import com.example.scholar.domain.openalex.AuthorShips;
import com.example.scholar.domain.openalex.Work;
import com.example.scholar.dto.AuthorResultDto;
import com.example.scholar.dto.InstitutionsResultDto;
import com.example.scholar.dto.WorkAuthorResultDto;
import com.example.scholar.service.AuthorService;
import com.example.scholar.util.AuthorNameRestore;
import com.example.scholar.util.JsonDisposer;
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
    public ArrayList<WorkAuthorResultDto> getAuthorsByWorkId(String workId) {
        List<AuthorShips> authorships = authorMapper.selectAuthorsById(workId);
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
                            authorResultDto.setAuthorName(AuthorNameRestore.restoreAuthorName(author.getDisplayNameAlternatives()));
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
    public int getWorksCountByAuthorId(String authorId) {
        Author author = authorMapper.selectAuthorById(authorId);
        return author.getWorksCount();
    }

    @Override
    public List<Work> getHighQualityWorksByAuthorId(String authorId) {
        List<String> workIds = authorMapper.getWorkIdsByAuthorId(authorId);
        List<Work> works = new ArrayList<>();
        for (String workId : workIds) {
            Work work = workMapper.getWorkById(workId);
            if (work != null) {
                if (work.getCitedByCount() > 1000) {
                    works.add(work);
                }
            }
        }
        works.sort(Comparator.comparingInt(Work::getCitedByCount).reversed());
        return works;
    }

    @Override
    public int getHighQualityWorksCountByAuthorId(String authorId, boolean track) {
        Author author = authorMapper.selectAuthorById(authorId);
        if (author.isIsInitialized()) {
            return author.getHighQualityWorkCount();
        } else {
            List<String> workIds = authorMapper.getWorkIdsByAuthorId(authorId);
            List<Work> works = new ArrayList<>();
            for (String workId : workIds) {
                Work work = workMapper.getWorkById(workId);
                if (work != null) {
                    if (work.getCitedByCount() > 1000) {
                        works.add(work);
                    }
                }
            }
            if (track != false) {
                int hNumber = authorService.getHNumberByAuthorId(authorId, false);
                int firstCount = authorService.getFirstPublishWorkCountByAuthorId(authorId, false);
                authorMapper.insertIntoHighQualityWorksCount(authorId, works.size(), hNumber, firstCount);
            }
            return works.size();
        }


    }

    @Override
    public int getCitedCountByAuthorId(String authorId) {
        //先判断init列是不是false，如果是true那就继续，如果不是就跳过直接搜
        Author author = authorMapper.selectAuthorById(authorId);
        return author.getCitedByCount();
    }

    @Override
    public int getHNumberByAuthorId(String authorId, boolean track) {
        Author author = authorMapper.selectAuthorById(authorId);
        if (author.isIsInitialized()) {
            return author.getHnumber();
        } else {
            List<String> workIds = authorMapper.getWorkIdsByAuthorId(authorId);
            List<Work> works = new ArrayList<>();
            for (String workId : workIds) {
                Work work = workMapper.getWorkById(workId);
                if (work != null) {
                    works.add(work);
                }
            }
            works.sort(Comparator.comparingInt(Work::getCitedByCount).reversed());
            int i = 0;
            for (i = 0; i < works.size(); i++) {
                if (works.get(i).getCitedByCount() >= (i + 1)) {
                    continue;
                }
            }
            if (track != false) {
                int size = authorService.getHighQualityWorksCountByAuthorId(authorId, false);
                int firstCount = authorService.getFirstPublishWorkCountByAuthorId(authorId, false);
                authorMapper.insertIntoHighQualityWorksCount(authorId, size, i, firstCount);
            }
            return i;
        }

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
    public int getFirstPublishWorkCountByAuthorId(String authorId, boolean track) {
        Author author = authorMapper.selectAuthorById(authorId);
        if (author.isIsInitialized()) {
            return author.getFirstPublishCount();
        } else {
            List<String> workIds = authorMapper.getWorkIdsByFirstAuthorId(authorId);
            List<Work> works = new ArrayList<>();
            for (String workId : workIds) {
                Work work = workMapper.getWorkById(workId);
                if (work != null) {
                    works.add(work);
                }
            }
            works.sort(Comparator.comparingInt(Work::getCitedByCount).reversed());
            if (track != false) {
                int size = authorService.getHighQualityWorksCountByAuthorId(authorId, false);
                int hNumber = authorService.getHNumberByAuthorId(authorId, false);
                authorMapper.insertIntoHighQualityWorksCount(authorId, size, hNumber, works.size());
            }
            return works.size();
        }
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
            authorResultDto.setAuthorName(AuthorNameRestore.restoreAuthorName(author.getDisplayNameAlternatives()));
            authorResultDto.setWorksCount(author.getWorksCount());
            authorResultDto.setCitedByCount(author.getCitedByCount());
            authorResultDto.setWorksApiUrl(author.getWorksApiUrl());
            res.add(authorResultDto);
        }
        return res;
    }
}
