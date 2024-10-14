package com.example.scholar.service.impl;

import com.example.scholar.dao.WorkMapper;
import com.example.scholar.domain.openalex.Work;
import com.example.scholar.dto.WorkResultDto;
import com.example.scholar.service.WorkService;
import com.example.scholar.util.AbstractRestore;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("workService")
public class WorkServiceImpl implements WorkService {
    @Resource
    private WorkMapper workMapper;
    @Override
    public List<WorkResultDto> getWorks() {
        List<Work> works = workMapper.selectAllWorks();
        List<WorkResultDto> workResultDtos = new ArrayList<>();
        for(Work work: works){
            WorkResultDto workResultDto = new WorkResultDto();
            workResultDto.setAbstractText(AbstractRestore.restoreAbstract(work.getAbstractInvertedIndex()));
            workResultDto.setTitle(work.getTitle());
            workResultDto.setPaperInformation("A Vignes - Industrial & Engineering Chemistry Fundamentals, 1966 - ACS Publications");
            //这里后续需要修改
            workResultDtos.add(workResultDto);
        }
        return workResultDtos;
    }

    @Override
    public List<WorkResultDto> getWorksByPage(int page) {
        int from = page*20-20;//设置前端每页最多20条
        int to = page*20;
        List<Work> works = workMapper.selectAllWorksByPage(from,to);
        List<WorkResultDto> workResultDtos = new ArrayList<>();
        for(Work work: works){
            WorkResultDto workResultDto = new WorkResultDto();
            workResultDto.setAbstractText(AbstractRestore.restoreAbstract(work.getAbstractInvertedIndex()));
            workResultDto.setTitle(work.getTitle());
            workResultDto.setCited(work.getCitedByCount());
            workResultDto.setPaperInformation("A Vignes - Industrial & Engineering Chemistry Fundamentals, 1966 - ACS Publications");
            //这里后续需要修改
            workResultDtos.add(workResultDto);
        }
        return workResultDtos;    }
}
