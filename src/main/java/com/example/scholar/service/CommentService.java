package com.example.scholar.service;

import com.example.scholar.domain.Comment;

import java.util.List;

public interface CommentService {
    void insertComment(String comment, int userId, String workId);

    List<Comment> getComments(String workId);
    int deleteComment(int userId,int commentId);

    int solveLike(int userId, int commentId);
}
