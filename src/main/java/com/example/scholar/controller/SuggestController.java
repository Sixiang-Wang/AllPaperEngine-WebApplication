package com.example.scholar.controller;

import com.example.scholar.domain.constant.R;
import com.example.scholar.service.SuggestService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@CrossOrigin
@RequestMapping("/suggest")
public class SuggestController {
    @Resource
    private SuggestService suggestService;
    @GetMapping("/works")
    public R getSuggestWorks(@RequestParam("id")String id){
        try{
            return R.ok().put("suggests", suggestService.getSuggestWorks(id));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
}
