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
    public int deleteComment(int userId, String workId, String commentIndex) {
        commentMapper.deleteUserComment(userId, workId, commentIndex);
        return 0;
    }
}
