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
    public R getWorkByTitleWord(@RequestParam("word") String word,@RequestParam("page") int page){
        try{
            return R.ok().put("works",workService.getWorksByTitleWords(word,page));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
    @GetMapping(value="/getWorkLengthByTitle")
    public R getWorkLengthByTitle(@RequestParam("word")String word){
        try{
            return R.ok().put("leng", workService.getWorkLengthByTitleWords(word));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }

    @GetMapping(value="/getWorkByPublicationYear")
    public R getWorkByPublicationYear(@RequestParam("from") int from,@RequestParam("to")int to,@RequestParam("page")int page){
        try{
            return R.ok().put("works",workService.getWorksByPublicationYear(from, to, page));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }

    @GetMapping(value="/getWorkByTitleAndPublicationYear")
    public R getWorkByTitleAndPublicationYear(@RequestParam("word") String word,@RequestParam("from") int from,@RequestParam("to")int to,@RequestParam("page")int page){
        try{
            return R.ok().put("works",workService.getWorkByTitleAndPublicationYear(word, from, to, page));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }


    @GetMapping(value="/getWorkByKeywords")
    public R getWorkByKeywords(@RequestParam("word") String word,@RequestParam("page")int page){
        try{
            return R.ok().put("works",workService.getWorkByKeywords(word,page));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }


}
