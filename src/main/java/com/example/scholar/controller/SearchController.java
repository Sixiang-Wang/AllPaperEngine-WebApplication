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
            return R.ok().put("getWorkByTitleWord"+word+" page: "+page+"\n",workService.getWorksByTitleWords(word,page));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }

    @GetMapping(value="/getWorkByPublicationYear")
    public R getWorkByPublicationYear(@RequestParam("from") int from,@RequestParam("to")int to,@RequestParam("page")int page){
        try{
            return R.ok().put("getWorkByPublicationYear"+ " from "+from+" to "+to+"\n",workService.getWorksByPublicationYear(from, to, page));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }

    @GetMapping(value="/getWorkByTitleAndPublicationYear")
    public R getWorkByTitleAndPublicationYear(@RequestParam("word") String word,@RequestParam("from") int from,@RequestParam("to")int to,@RequestParam("page")int page){
        try{
            return R.ok().put("getWorkByTitleAndPublicationYear"+" from "+from+" to "+to+"word"+word+"\n",workService.getWorkByTitleAndPublicationYear(word, from, to, page));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }


    @GetMapping(value="/getWorkByKeywords")
    public R getWorkByKeywords(@RequestParam("word") String word,@RequestParam("page")int page){
        try{
            return R.ok().put("getWorkByKeywords"+" keyword: "+word+"\n",workService.getWorkByKeywords(word,page));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }



}
