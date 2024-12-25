package com.example.scholar.controller;


import com.example.scholar.domain.constant.R;
import com.example.scholar.domain.openalex.Author;
import com.example.scholar.dto.AuthorSpecificResultDto;
import com.example.scholar.service.InstitutionService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/institution")
public class InstitutionController {

    @Resource
    private InstitutionService institutionService;


    @Cacheable(value = "suggestWorksCache", key = "#workId")
    @GetMapping(value="/getInstitutionById")
    public R getInstitutionById(@RequestParam("id") String id){
        try{
            return R.ok().put("getInstitutionById",institutionService.getInstitutionById(id));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }

    @GetMapping(value="/getInstitutionByName")
    public R getInstitutionByName(@RequestParam("name") String name){
        try{
            return R.ok().put("getInstitutionByName",institutionService.getInstitutionByName(name));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }

    @GetMapping(value="/getInstitutionIdByName")
    public R getInstitutionIdByName(@RequestParam("name") String name){
        try{
            return R.ok().put("getInstitutionIdByName",institutionService.getInstitutionIdByName(name));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }

    @GetMapping(value="/getAuthorByInstitutionId")
    public R getAuthorByInstitutionId(@RequestParam("id") String institutionId){
        List<String> authorIdList = institutionService.getAuthorIdByInstitutionId(institutionId);
        List<AuthorSpecificResultDto> dtoList = institutionService.getdtoList(institutionId, authorIdList);
        if (dtoList.isEmpty()) {
            return R.error("No author found for this institution");
        } else {
            return R.ok().put("authorList", dtoList);
        }
    }

}
