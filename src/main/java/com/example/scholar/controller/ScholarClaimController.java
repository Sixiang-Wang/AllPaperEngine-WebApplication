package com.example.scholar.controller;

import com.example.scholar.domain.constant.R;
import com.example.scholar.domain.openalex.Work;
import com.example.scholar.dto.WorkResultDto;
import com.example.scholar.service.ClaimWorkService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/scholar/claim")
public class ScholarClaimController {
    @Resource
    private ClaimWorkService claimWorkService;
    @GetMapping(value="/get/personal")
    public R getAllScholars(@RequestParam("scholarId")int scholarId){
        try{
            List<WorkResultDto> works = claimWorkService.selectClaimedWorks(scholarId);
            if(works == null){
                return R.error("something went wrong");
            }else{
                return R.ok("success").put("works",works);
            }
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
    @GetMapping(value = "/add")
    public R claimWork(@RequestParam("scholarId")int scholarId, @RequestParam("workId")String workId){
        try{
            int res = claimWorkService.claimWork(scholarId, workId);
            if(res == 0){
                return R.error("invalid user");
            }else if(res == -1){
              return R.error("invalid work");
            } else if(res == 1){
                return R.error("work has already been claimed");
            }else if(res == 2){
                return R.ok("claim success");
            }else{
                return R.ok("something went wrong");
            }
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
    @GetMapping(value = "/delete")
    public R deleteClaimedWork(@RequestParam("scholarId")int scholarId, @RequestParam("workId")String workId){
        try{
            int res = claimWorkService.deleteClaimedWork(scholarId, workId);
            if(res == 1){
                return R.ok("delete success");
            }else{
                return R.error("something went wrong");
            }
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
}
