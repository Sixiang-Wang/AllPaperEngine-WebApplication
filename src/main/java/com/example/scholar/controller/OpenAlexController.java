package com.example.scholar.controller;

import com.example.scholar.dao.AuthorMapper;
import com.example.scholar.dao.WorkMapper;
import com.example.scholar.domain.constant.R;
import com.example.scholar.dto.WorkResultDto;
import com.example.scholar.service.AuthorService;
import com.example.scholar.service.ConceptsService;
import com.example.scholar.service.InstitutionsService;
import com.example.scholar.service.WorkService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value="/openalex")
public class OpenAlexController {
    @Resource
    private WorkMapper workMapper;
    @Resource
    private WorkService workService;
    @Resource
    private ConceptsService conceptsService;
    @Resource
    private InstitutionsService institutionsService;
    @Resource
    private AuthorMapper authorMapper;
    @Resource
    private AuthorService authorService;



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
            List<WorkResultDto> workResultDtoList = workService.getWorksByPage(page);
            return R.ok().put("works",workResultDtoList).put("length",workResultDtoList.size() );
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


    @GetMapping(value="/work/get")
    public R getWorkById(@RequestParam("workId") String workId){
        try{
            return R.ok().put("work",workService.getWorkById(workId));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }



    @GetMapping(value="/work/getWorkItsReference")
    public R getWorkItsReference(@RequestParam("workId") String workId){
        try{
            return R.ok().put("work",workService.getWorkItsReferenced(workId));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }



    @GetMapping(value="/work/getWorkReferenceIt")
    public R getWorkReferenceIt(@RequestParam("workId") String workId){
        try{
            return R.ok().put("work",workService.getWorkReferenceIt(workId));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }


    @PostMapping(value="/work/updateKeywordsAndAbstract")
    public R updateKeywordsAndAbstract(){
        try{
            workService.updateKeywordsAndAbstract();
            return R.ok().put("successfully update","end");
        }catch (Exception e){
            return R.error(e.toString());
        }
    }


    @GetMapping(value="/work/getTopNWorksByKeyword")
    public R getTopNWorksByKeyword(@RequestParam("keyword") String keyword,@RequestParam("number") int number){
        try{
            List<WorkResultDto> workResultDtoList = workService.getTopNWorkByKeywords(keyword,number);
            return R.ok().put("work",workResultDtoList);
        }catch (Exception e){
            return R.error(e.toString());
        }
    }



    @GetMapping(value="/concepts/getByWorkId")
    public R getConceptsByWorkId(@RequestParam("workId") String workId){
        try{
            return R.ok().put("concepts"+workId+"\n",conceptsService.getConceptsByWorkId(workId));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }

    @GetMapping(value="/author/getAuthorsByWorkId")
    public R getAuthorsByWorkId(@RequestParam("workId") String workId){
        try{
            return R.ok().put("authors",authorService.getAuthorsByWorkId(workId));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }



    @GetMapping(value="/institutions/getInstitutionById")
    public R getInstitutionById(@RequestParam("institutionId") String institutionId){
        try{
            return R.ok().put("institutions",institutionsService.getInstitutionById(institutionId));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }



}
