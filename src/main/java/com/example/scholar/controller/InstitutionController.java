package com.example.scholar.controller;


import com.example.scholar.domain.constant.R;
import com.example.scholar.service.InstitutionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin
@RestController
@RequestMapping(value = "/institution")
public class InstitutionController {

    @Resource
    private InstitutionService institutionService;

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



}
