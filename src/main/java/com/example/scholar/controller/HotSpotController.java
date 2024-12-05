package com.example.scholar.controller;


import com.example.scholar.domain.constant.R;
import com.example.scholar.dto.HottestKeywordsResult;
import com.example.scholar.dto.WorkResultDto;
import com.example.scholar.service.HotSpotService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value="/hotspot")
public class HotSpotController {


    @Resource
    private HotSpotService hotSpotService;


    @GetMapping(value="/getTopNKeywords")
    public R getTopNKeywords(@RequestParam("number") int number, @RequestParam("publicationYearFrom") int publicationYearFrom, @RequestParam("publicationYearTo") int publicationYearTo){
        try{
            List<HottestKeywordsResult> hottestKeywordsResults = hotSpotService.GetHottestKeywords(number,publicationYearFrom,publicationYearTo);
            return R.ok().put("keywords",hottestKeywordsResults);
        }catch (Exception e){
            return R.error(e.toString());
        }
    }









}
