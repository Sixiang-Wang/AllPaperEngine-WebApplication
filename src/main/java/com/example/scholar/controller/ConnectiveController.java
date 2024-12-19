package com.example.scholar.controller;

import com.example.scholar.domain.constant.R;
import com.example.scholar.domain.openalexElasticsearch.Works;
import com.example.scholar.dto.HottestKeywordsResult;
import com.example.scholar.service.ConnectiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping(value ="/Connective")
public class ConnectiveController {
    @Resource
    private ConnectiveService connectiveService;

    @GetMapping(value="/getSortedKeywords")
    public R sortKeywords(){
        try{
            List<String> keywords = connectiveService.sortKeywords();
            return R.ok().put("keywords",keywords);
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
    @GetMapping(value="/getSortedTypes")
    public R sortTypes(){
        try{
            List<String> types = connectiveService.sortTypes();
            return R.ok().put("types", types);
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
    @GetMapping(value="/getSortedLanguages")
    public R sortLanguages(){
        try{
            List<String> languages = connectiveService.sortLanguages();
            return R.ok().put("languages", languages);
        }catch (Exception e){
            return R.error(e.toString());
        }
    }

    @GetMapping(value="/getWorksByKeyWord")
    public R getWorksByKeyWord(@RequestParam("keyword") String keyword){
        try {
            List<Works> works = connectiveService.getWorksByKeyword(keyword);
            return R.ok().put("works",works);
        }catch (Exception e){
            return R.error(e.toString());
        }
    }

    @GetMapping(value="/getWorksByType")
    public R getWorksByType(@RequestParam("type") String type){
        try {
            List<Works> works = connectiveService.getWorksByType(type);
            return R.ok().put("works",works);
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
    @GetMapping(value="/getWorksByLanguage")
    public R getWorksByLanguage(@RequestParam("language") String language){
        try {
            List<Works> works = connectiveService.getWorksByLanguage(language);
            return R.ok().put("works",works);
        }catch (Exception e){
            return R.error(e.toString());
        }
    }


}
