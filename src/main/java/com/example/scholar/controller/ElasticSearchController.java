package com.example.scholar.controller;


import com.example.scholar.dao.WorkMapper;
import com.example.scholar.domain.constant.R;
import com.example.scholar.service.ElasticWorkService;
import com.example.scholar.service.impl.ElasticWorksServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin
@RestController
@RequestMapping(value="/elasticSearch")
public class ElasticSearchController {

    @Resource
    private ElasticWorkService elasticWorkService;

    @GetMapping(value="/works/getByTitle")
    public R getConceptsByWorkId(@RequestParam("title") String title){
        try{
            return R.ok().put("works",elasticWorkService.searchByTitle(title));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }


    @GetMapping(value="/works/getByTitleOrAbstractOrKeywords")
    public R getByTitleOrAbstractOrKeywords(@RequestParam("searchterm") String searchterm){
        try{
            return R.ok().put("works",elasticWorkService.findByTitleOrKeywordsTextOrAbstract(searchterm));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }


    @GetMapping(value="/works/searchAutocomplete")
    public R searchAutocomplete(@RequestParam("value") String value ,@RequestParam("fuzziness")String fuzziness,@RequestParam("transpositions")boolean transposition, @RequestParam("prefixLength") int prefixLength){
        try{
            return R.ok().put("probability",elasticWorkService.fuzzyAutocomplete(value, fuzziness, transposition, prefixLength));
        }catch (Throwable e){
            return R.error(e.toString());
        }
    }

}
