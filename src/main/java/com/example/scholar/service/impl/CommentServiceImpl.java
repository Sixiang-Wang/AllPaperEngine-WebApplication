package com.example.scholar.service.impl;

import com.example.scholar.dao.CommentMapper;
import com.example.scholar.dao.UserMapper;
import com.example.scholar.domain.Comment;
import com.example.scholar.service.CommentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
@Service("commentService")
public class CommentServiceImpl implements CommentService {
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private UserMapper userMapper;
    @Override
    public void insertComment(String comment, int userId, String workId) {
        commentMapper.insertComments(userId,workId, new Date(System.currentTimeMillis()),comment);
    }

    @Override
    public List<Comment> getComments(String workId) {
        List<Comment> comments = commentMapper.selectCommentsById(workId);
        if(comments == null){
            comments = new ArrayList<>();
        }
        for(Comment comment: comments){
            comment.setUserName(userMapper.selectUserById(comment.getUserId()).getName());
        }
        return comments;
    }

    @Override
    public int deleteComment(int userId,int commentId) {
        //检测该comment是否是该user发的
        Comment comment = commentMapper.selectCommentById(commentId);
        if(comment.getUserId() == userId) {
            commentMapper.deleteUserComment(commentId);
            return 1;
        }else{
            return 0;
        }
    }

    @Override
    public int solveLike(int userId, int commentId) {
        int ifCommentLike = commentMapper.ifUserLiked(userId, commentId);
        if(ifCommentLike == 0){
            //未被点赞过
            commentMapper.likeComment(userId, commentId);
            return 1;
        }else if(ifCommentLike == 1){
            //已被点赞，取消点赞
            commentMapper.deleteCommentLike(userId, commentId);
            return 0;
        }else{
            //有问题
            return -1;
        }
    }
}
