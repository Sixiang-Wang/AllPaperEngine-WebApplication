package com.example.scholar.service.impl;

import com.example.scholar.dao.ScholarClaimMapper;
import com.example.scholar.domain.openalex.Work;
import com.example.scholar.service.ClaimWorkService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("claimWorkService")
public class ClaimWorkServiceImpl implements ClaimWorkService {
    @Resource
    private ScholarClaimMapper claimMapper;
    @Override
    public int claimWork(int scholarId, String workId) {
        try {
            //check if correct.
            int check = claimMapper.checkIfScholar(scholarId);
            if(check == 0){//如果用户不存在或者不是学者
                return 0;//用户有问题
            }
            claimMapper.claimWork(scholarId,workId);
            return 1;//成功claim
        }catch (Exception e){
            return -1;//异常
        }
    }

    @Override
    public int deleteClaimedWork(int scholarId, String workId) {
        return 0;
    }

    @Override
    public List<Work> selectClaimedWorks(int scholarId) {
        return null;
    }
}
