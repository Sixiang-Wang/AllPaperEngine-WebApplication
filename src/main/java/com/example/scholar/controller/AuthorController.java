package com.example.scholar.controller;
import com.example.scholar.domain.User;
import com.example.scholar.domain.constant.R;
import com.example.scholar.service.AuthorService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin
@RestController
@RequestMapping(value = "/author")
public class AuthorController {
    @Resource
    private AuthorService authorService;


    @GetMapping(value="/getAuthorIdByAuthorName")
    public R getAuthorIdByAuthorName(@RequestParam("authorName") String authorName){
        try{
            return R.ok().put("getAuthorIdByAuthorName",authorService.getAuthorIdByAuthorName(authorName));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }


    @GetMapping(value="/getAuthorsByWorkId")
    public R getAuthorsByWorkId(@RequestParam("workId") String workId){
        try{
            User user = new User();
            int userId = user.getUserid();
            return R.ok().put("getAuthorsByWorkId",authorService.getAuthorsByWorkId(workId));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }

    //总发文
    @GetMapping(value="/getWorksByAuthorId")
    public R getWorksByAuthorId(@RequestParam("authorId") String authorId){
        try{
            return R.ok().put("getWorksByAuthorId",authorService.getWorksByAuthorId(authorId));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }

    //作者总发文量
    @GetMapping(value="/getWorksCountByAuthorId")
    public R getWorksCountByAuthorId(@RequestParam("authorId") String authorId){
        try{
            return R.ok().put("getWorksCountByAuthorId",authorService.getWorksCountByAuthorId(authorId));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }

    //总发高质量文章
    @GetMapping(value="/getHighQualityWorksByAuthorId")
    public R getHighQualityWorksByAuthorId(@RequestParam("authorId") String authorId){
        try{
            return R.ok().put("getHighQualityWorksByAuthorId",authorService.getHighQualityWorksByAuthorId(authorId));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }

    //总发高质量文章数量
    @GetMapping(value="/getHighQualityWorksCountByAuthorId")
    public R getHighQualityWorksCountByAuthorId(@RequestParam("authorId") String authorId){
        try{
            return R.ok().put("getHighQualityWorksCountByAuthorId",authorService.getHighQualityWorksCountByAuthorId(authorId,true));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }


    @GetMapping(value="/getCitedCountByAuthorId")
    public R getCitedCountByAuthorId(@RequestParam("authorId") String authorId){
        try{
            return R.ok().put("getCitedCountByAuthorId",authorService.getCitedCountByAuthorId(authorId));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }


    @GetMapping(value="/getHNumberByAuthorId")
    public R getHNumberByAuthorId(@RequestParam("authorId") String authorId){
        try{
            return R.ok().put("getHNumberByAuthorId",authorService.getHNumberByAuthorId(authorId,true));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }


    @GetMapping(value="/getFirstPublishWorkByAuthorId")
    public R getFirstPublishWorkByAuthorId(@RequestParam("authorId") String authorId){
        try{
            return R.ok().put("getFirstPublishWorkByAuthorId",authorService.getFirstPublishWorkByAuthorId(authorId));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }


    @GetMapping(value="/getFirstPublishWorkCountByAuthorId")
    public R getFirstPublishWorkCountByAuthorId(@RequestParam("authorId") String authorId){
        try{
            return R.ok().put("getFirstPublishWorkCountByAuthorId",authorService.getFirstPublishWorkCountByAuthorId(authorId,true));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }



}
