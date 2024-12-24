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
    @GetMapping(value="/getSortedInstitutions")
    public R sortInstitutions(){
        try{
            List<String> institutions = connectiveService.sortInstitutions();
            return R.ok().put("institutions", institutions);
        }catch (Exception e){
            return R.error(e.toString());
        }
    }

    @GetMapping(value = "/getSortedPublicationYears")
    public R sortPublicationYears()
    {
        try{
            List<Integer> years = connectiveService.sortPublictionYears();
            return R.ok().put("years", years);
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
    @GetMapping(value="/getWorksByInstitution")
    public R getWorksByLanguage(@RequestParam("institution") String institution){
        try {
            List<Works> works = connectiveService.getWorksByInstitution(institution);
            return R.ok().put("works",works);
        }catch (Exception e){
            return R.error(e.toString());
        }
    }

    @GetMapping(value = "/getWorksByPublicationYear")
    public R getWorksByPublicationYear(@RequestParam("year") Integer year){
        try {
            List<Works> works = connectiveService.getWorksByPublicationYear(year);
            return R.ok().put("works",works);
        }catch (Exception e){
            return R.error(e.toString());
        }
    }

    @GetMapping(value = "/getWorksByConditions")
    public R getWorksByConditions(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "institution", required = false) String institution,
            @RequestParam(value = "publicationYear", required = false) Integer publicationYear) {
        try {
            List<Works> works = connectiveService.getWorksByConditions(keyword, type, institution, publicationYear);
            return R.ok().put("works", works);
        } catch (Exception e) {
            return R.error(e.toString());
        }
    }


}
