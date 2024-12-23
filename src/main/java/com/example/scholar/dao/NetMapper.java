package com.example.scholar.dao;

import com.example.scholar.domain.AuthorForNet;
import com.example.scholar.domain.openalex.Author;
import com.example.scholar.domain.openalex.Work;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface NetMapper {
    @Select("select * from openalex.authors where id = #{authorId}")
    AuthorForNet selectAuthorById(String authorId);
    @Select("select author_id from openalex.works_authorships where work_id = #{workId}")
    List<String> getRelatedAuthorIds(String workId);
    @Select("select author_id from user where userid = #{userId}")
    String getAuthorIdByUserId(int userId);
    @Select("select work_id from openalex.works_authorships where author_id = #{authorId}")
    List<String> getWorksId(String authorId);
    @Select("select last_known_institution from authors where id = #{authorId}")
    String getInstitutionByAuthorId(String authorId);
    @Select("select * from authors where last_known_institution = #{institution}")
    List<AuthorForNet> getRelatedAuthorsByInstitution(String institution);

}
