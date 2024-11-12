package com.example.scholar.dao;


import com.example.scholar.domain.openalex.Author;
import com.example.scholar.domain.openalex.AuthorShips;
import com.example.scholar.domain.openalex.Work;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;

public interface AuthorMapper {
    @Select("select * from openalex_authors where id = #{authorId}")
    Author selectAuthorById(String authorId);
    @Select("select * from openalex_works_authorships where work_id = #{workId}")
    List<AuthorShips> selectAuthorsById(String workId);
    @Select("SELECT id FROM openalex_authors WHERE JSON_CONTAINS(display_name_alternatives, JSON_QUOTE(#{authorName}), '$') = 1")
    String getAuthorIdByAuthorName(String authorName);
    @Select("SELECT work_id FROM openalex_works_authorships WHERE author_id = #{authorId}")
    List<String> getWorkIdsByAuthorId(String authorId);

}
