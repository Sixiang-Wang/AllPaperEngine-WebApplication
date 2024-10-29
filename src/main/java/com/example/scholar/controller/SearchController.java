package com.example.scholar.controller;

import com.example.scholar.domain.constant.R;
import com.example.scholar.service.WorkService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin
@RestController
@RequestMapping(value = "/search")
public class SearchController {
    @Resource
    private WorkService workService;


    @GetMapping(value="/getWorkByTitleWord")
    public R getConceptsByWorkId(@RequestParam("word") String word){
        try{
            return R.ok().put("getWorkByTitleWord"+word+"\n",workService.getWorksByTitleWords(word));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }

}
