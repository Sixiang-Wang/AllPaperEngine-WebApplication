package com.example.scholar.controller;

import com.example.scholar.dao.WorkMapper;
import com.example.scholar.domain.constant.R;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@CrossOrigin
@RestController
@RequestMapping(value="/openalex")
public class OpenAlexController {
    @Resource
    private WorkMapper workMapper;
    @GetMapping(value = "/getall")
    public R getAllWorks(){
        try {
            return R.ok().put("works",workMapper.selectAllWorks());
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
}
