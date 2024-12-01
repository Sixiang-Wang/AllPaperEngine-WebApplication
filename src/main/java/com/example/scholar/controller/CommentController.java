package com.example.scholar.controller;

import com.example.scholar.config.annotation.TokenToUser;
import com.example.scholar.dao.CommentMapper;
import com.example.scholar.domain.User;
import com.example.scholar.domain.constant.R;
import com.example.scholar.service.CommentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin
@RestController
@RequestMapping(value="/comment")
public class CommentController {
    @Resource
    private CommentService commentService;
    @GetMapping(value = "/insert")
    public R addComment(@TokenToUser User user, @RequestParam("workId")String workId, @RequestParam("commentIndex")String commentIndex){
        try{
            commentService.insertComment(commentIndex, user.getUserid(), workId);
            return R.ok("insert comment success");
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
    @GetMapping(value="/get")
    public R getComments(@RequestParam("workId")String workId){
        try{
            return R.ok("get success").put("comments",commentService.getComments(workId));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
    @GetMapping(value="/delete")
    public R deleteComment(@RequestParam("workId")String workId, @RequestParam("userId")int userId, @RequestParam("commentIndex")String commentIndex){
        try{
            commentService.deleteComment(userId, workId, commentIndex);
            return R.ok("delete success");
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
}
