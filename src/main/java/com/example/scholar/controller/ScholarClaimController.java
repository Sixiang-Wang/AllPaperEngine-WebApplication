package com.example.scholar.controller;

import com.example.scholar.domain.constant.R;
import com.example.scholar.dto.ClaimResultDto;
import com.example.scholar.dto.WorkResultDto;
import com.example.scholar.service.ClaimWorkService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/claim")
public class ScholarClaimController {
    @Resource
    private ClaimWorkService claimWorkService;
    @GetMapping(value="/get/personal")
    public R getAllScholars(@RequestParam("scholarId")int scholarId){
        try{
            List<WorkResultDto> works = claimWorkService.selectClaimedWorks(scholarId);
            if(works == null){
                return R.error("Not A Scholar");
            }else{
                return R.ok("success").put("works",works);
            }
        }catch (Exception e){
            return R.error(e.toString());
        }
    }

    @GetMapping(value="/getAllUnavailable")
    public R getAllUnavailable(){
        try{
            List<ClaimResultDto> claimList = claimWorkService.allClaimUnavailable();
            if(claimList == null){
                return R.error("something went wrong");
            }else{
                return R.ok("success").put("claimList",claimList);
            }
        }catch (Exception e){
            return R.error(e.toString());
        }
    }

    @GetMapping(value = "/add")
    public R claimWork(@RequestParam("userId")int userId, @RequestParam("workId")String workId){
        try{
            int res = claimWorkService.claimWork(userId, workId);
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
    public R deleteClaimedWork(@RequestParam("id")int id){
        try{
            int res = claimWorkService.deleteClaimedWork(id);
            if(res == 1){
                return R.ok("delete success");
            }else{
                return R.error("something went wrong");
            }
        }catch (Exception e){
            return R.error(e.toString());
        }
    }

    @GetMapping(value = "/able")
    public R ableClaim(@RequestParam("id")int id){
        try{
            int res = claimWorkService.ableClaim(id);
            if(res == 1){
                return R.ok("able success");
            }else{
                return R.error("something went wrong");
            }
        }catch (Exception e){
            return R.error(e.toString());
        }
    }

    @GetMapping(value = "/disable")
    public R disableClaim(@RequestParam("id")int id){
        try{
            int res = claimWorkService.disableClaim(id);
            if(res == 1){
                return R.ok("able success");
            }else{
                return R.error("something went wrong");
            }
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
}
