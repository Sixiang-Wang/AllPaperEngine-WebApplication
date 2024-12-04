package com.example.scholar.service.impl;

import com.example.scholar.dao.ScholarClaimMapper;
import com.example.scholar.dao.UserMapper;
import com.example.scholar.dao.WorkMapper;
import com.example.scholar.domain.User;
import com.example.scholar.domain.UserClaimedWork;
import com.example.scholar.domain.openalex.Work;
import com.example.scholar.dto.ClaimResultDto;
import com.example.scholar.dto.WorkAuthorResultDto;
import com.example.scholar.dto.WorkResultDto;
import com.example.scholar.service.AuthorService;
import com.example.scholar.service.ClaimWorkService;
import com.example.scholar.service.WorkService;
import com.example.scholar.util.AbstractRestore;
import com.example.scholar.util.JsonDisposer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("claimWorkService")
public class ClaimWorkServiceImpl implements ClaimWorkService {
    @Resource
    private ScholarClaimMapper claimMapper;
    @Resource
    private AuthorService authorService;
    @Resource
    private UserMapper userMapper;
    @Resource
    private WorkMapper workMapper;
    @Resource
    private WorkService workService;
    @Override
    public int claimWork(int userId, String workId) {
            //check if correct.
            int check = claimMapper.checkIfScholar(userId);
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
            claimMapper.claimWork(userId,workId);
            return 2;//成功claim
    }

    @Override
    public int deleteClaimedWork(int id) {
        claimMapper.deleteClaimedWorks(id);
        return 1;
    }

    @Override
    public  List<ClaimResultDto> allClaimUnavailable(){
        List<UserClaimedWork> userClaimedWorkList = claimMapper.allClaimUnavailable();
        if(userClaimedWorkList==null){
            return null;
        }
        List<ClaimResultDto> claimResultList = new ArrayList<>();
        for(UserClaimedWork userClaimedWork:userClaimedWorkList){
            ClaimResultDto claimResult = new ClaimResultDto();
            claimResult.setId(userClaimedWork.getId());
            claimResult.setUserId(userClaimedWork.getUserId());
            User user = userMapper.selectUserById(userClaimedWork.getUserId());
            claimResult.setName(user.getName());
            claimResult.setNameReal(user.getNameReal());

            Work work = workMapper.getWorkById(userClaimedWork.getWorkId());
            claimResult.setTitle(work.getTitle());
            List<WorkAuthorResultDto> authorList = authorService.getAuthorsByWorkId(userClaimedWork.getWorkId());
            List<String> authorNameList=new ArrayList<>();
            for(WorkAuthorResultDto workAuthorResultDto:authorList){
                authorNameList.add(workAuthorResultDto.getAuthorResultDto().getAuthorName().get(0));
            }
            claimResult.setAuthorList(authorNameList);
            claimResultList.add(claimResult);
        }
        return claimResultList;
    }

    @Override
    public List<WorkResultDto> selectClaimedWorks(int userId) {
        int check = claimMapper.checkIfScholar(userId);
        if(check==0){
            return null;
        }
        List<UserClaimedWork> list = claimMapper.selectWorksById(userId);

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

    @Override
    public int ableClaim(int id){
        if(claimMapper.ableClaim(id)>0) {
            return 1;
        }
        else{
            return -1;
        }
    }

    @Override
    public int disableClaim(int id){
        if(claimMapper.disableClaim(id)>0) {
            return 1;
        }
        else{
            return -1;
        }
    }
}
