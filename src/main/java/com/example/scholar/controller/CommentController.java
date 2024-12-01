package com.example.scholar.controller;

import com.example.scholar.config.annotation.TokenToUser;
import com.example.scholar.dao.CommentMapper;
import com.example.scholar.domain.User;
import com.example.scholar.domain.constant.R;
import com.example.scholar.service.CommentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin
@RestController
@RequestMapping(value="/comment")
public class CommentController {
    @Resource
    private CommentService commentService;
    @Resource
    private CommentMapper commentMapper;
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
    public R deleteComment(@RequestParam("commentId")int commentId, @TokenToUser User user){
        try{
            int res = commentService.deleteComment(user.getUserid(), commentId);
            if(res == 1) {
                return R.ok("delete success");
            }else{
                return R.error("something wrong");
            }
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
    @GetMapping(value="/like")
    @ApiOperation("处理评论点赞")
    public R solveLike(@TokenToUser User user, @RequestParam("commentId")int commentId){
        try{
            int res = commentService.solveLike(user.getUserid(), commentId);

            if(res == 0){
                //取消点赞成功
                return R.ok("cancel success");
            }else if(res == 1){
                //点赞成功
                return R.ok("like success");
            }else{
                return R.error("something wrong");
            }
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
    @GetMapping(value="/ifLiked")
    public R ifLiked(@TokenToUser User user, @RequestParam("commentId")int commentId){
        try{
            return R.ok().put("val",commentMapper.ifUserLiked(user.getUserid(), commentId));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
}
