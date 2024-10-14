package com.example.scholar.controller;

import com.example.scholar.dao.WorkMapper;
import com.example.scholar.domain.constant.R;
import com.example.scholar.service.WorkService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin
@RestController
@RequestMapping(value="/openalex")
public class OpenAlexController {
    @Resource
    private WorkMapper workMapper;
    @Resource
    private WorkService workService;
    @GetMapping(value = "/getall")
    public R getAllWorks(){
        try {

            return R.ok().put("works",workService.getWorks());
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
    @GetMapping(value="/get/page")
    public R getAllWorksByPage(@RequestParam("page")int page){
        try{
            return R.ok().put("works",workService.getWorksByPage(page));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
    @GetMapping(value="/get/length")
    public R getAllWorksLength(){
        try{
            return R.ok().put("leng",workMapper.getWorksLength());
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
}
