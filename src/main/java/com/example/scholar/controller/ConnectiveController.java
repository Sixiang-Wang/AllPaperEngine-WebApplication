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
    @GetMapping(value = "/create")
    public R create()
    {
        try {
            connectiveService.create();
            return R.ok();
        }catch (Exception e){
            return R.error(e.toString());
        }
    }

    @GetMapping(value="/getSortedKeywords")
    public R sortKeywords(){
        try{
            List<String> keywords = connectiveService.sortKeywords();
            return R.ok().put("keywords",keywords);
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


}
