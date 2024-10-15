package com.example.scholar.dao;


import com.example.scholar.domain.openalex.Author;
import com.example.scholar.domain.openalex.AuthorShips;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;

public interface AuthorMapper {
    @Select("select * from openalex_authors where id = #{authorId}")
    Author selectAuthorById(String authorId);
    @Select("select * from openalex_works_authorships where work_id = #{workId}")
    List<AuthorShips> selectAuthorsById(String workId);

}
