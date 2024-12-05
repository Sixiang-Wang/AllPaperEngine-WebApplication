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

    @GetMapping(value="/getAuthorIdByAuthorName")
    public R getAuthorIdByAuthorName(@RequestParam("authorName") String authorName){
        try{
            return R.ok().put("getAuthorIdByAuthorName",authorService.getAuthorIdByAuthorName(authorName));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }

    @GetMapping(value="/getWorksByAuthorName")
    public R getWorksByAuthorName(@RequestParam("authorName") String authorName){
        try{
            return R.ok().put("getWorksByAuthorName",authorService.getWorksByAuthorName(authorName));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
}
