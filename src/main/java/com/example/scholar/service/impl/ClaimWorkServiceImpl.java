package com.example.scholar.service.impl;

import com.example.scholar.dao.ScholarClaimMapper;
import com.example.scholar.dao.WorkMapper;
import com.example.scholar.domain.UserClaimedWork;
import com.example.scholar.domain.openalex.Work;
import com.example.scholar.dto.WorkResultDto;
import com.example.scholar.service.ClaimWorkService;
import com.example.scholar.service.WorkService;
import com.example.scholar.util.AbstractRestore;
import com.example.scholar.util.JsonDisposer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("claimWorkService")
public class ClaimWorkServiceImpl implements ClaimWorkService {
    @Resource
    private ScholarClaimMapper claimMapper;
    @Resource
    private WorkMapper workMapper;
    @Resource
    private WorkService workService;
    @Override
    public int claimWork(int scholarId, String workId) {
            //check if correct.
            int check = claimMapper.checkIfScholar(scholarId);
            if(check == 0){//如果用户不存在或者不是学者
                return 0;//用户有问题
            }
            int ifClaimed = claimMapper.checkIfClaimed(workId);
            if(ifClaimed!=0){
                return 1;//work已认证
            }
            int ifExist = claimMapper.checkIfWorkExist(workId);
            if(ifExist==0){
                return -1;//work 无效
            }
            claimMapper.claimWork(scholarId,workId);
            return 2;//成功claim
    }

    @Override
    public int deleteClaimedWork(int scholarId, String workId) {
        claimMapper.deleteClaimedWorks(scholarId, workId);
        return 1;
    }

    @Override
    public List<WorkResultDto> selectClaimedWorks(int scholarId) {
        int check = claimMapper.checkIfScholar(scholarId);
        if(check==0){
            return null;
        }
        List<UserClaimedWork> list = claimMapper.selectWorksById(scholarId);
        List<Work> works = new ArrayList<>();
        List<WorkResultDto> workResultDtos = new ArrayList<>();
        for(UserClaimedWork userClaimedWork: list){
            Work work = workMapper.getWorkById(userClaimedWork.getWorkId());
            WorkResultDto workResultDto = new WorkResultDto();
            workResultDto.setAbstractText(AbstractRestore.restoreAbstract(work.getAbstractInvertedIndex()));
            workResultDto.setTitle(work.getTitle());
            workResultDto.setCited(work.getCitedByCount());
            workResultDto.setId(work.getId());
            workResultDto.setPublicationDate(work.getPublicationDate());
            workResultDto.setPaperInformation(workService.ToMainInformation(work));
            //这里后续需要修改
            workResultDto.setGrants(work.getGrants());
            workResultDto.setKeywords(JsonDisposer.disposeWorkKeywords(work.getKeywords()));
            workResultDtos.add(workResultDto);
        }
        return workResultDtos;
    }
}
