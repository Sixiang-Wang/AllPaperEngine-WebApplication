package com.example.scholar.dao;


import com.example.scholar.config.PathConfig;
import com.example.scholar.domain.openalex.Author;
import com.example.scholar.domain.openalex.AuthorShips;
import com.example.scholar.domain.openalex.Work;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;
import java.util.List;

public interface AuthorMapper {
    @Select("select * from openalex.authors where id = #{authorId}")
    Author selectAuthorById(String authorId);
    @Select("select * from openalex.works_authorships where work_id = #{workId}")
    @Results({
            @Result(property = "institution", column = "institution_id",
            one = @One(select = PathConfig.pathMapper + "InstitutionsMapper.selectInstitutionsById"))
    })
    List<AuthorShips> selectAuthorsById(String workId);
    @Select("SELECT id FROM openalex.authors WHERE display_name like #{authorName}")
    List<String> getAuthorIdByAuthorName(String authorName);
    @Select("SELECT work_id FROM openalex.works_authorships WHERE author_id = #{authorId}")
    List<String> getWorkIdsByAuthorId(String authorId);

    @Select("SELECT institution_id FROM openalex.works_authorships WHERE author_id = #{authorId} limit 1")
    String getInstitutionIdByAuthorId(String authorId);

    //

    @Update("UPDATE openalex.authors set highQualityWorkCount = #{size},Hnumber = #{hNumber},isInitialized = true, firstPublishCount=#{firstCount} where id = #{authorId}")
    void insertIntoHighQualityWorksCount(String authorId, int size,int hNumber,int firstCount);

    @Select("SELECT work_id FROM openalex.works_authorships WHERE author_id = #{authorId} AND author_position like 'first'")
    List<String> getWorkIdsByFirstAuthorId(String authorId);
    @Select("select * from openalex.authors where display_name LIKE CONCAT('%', #{name}, '%')")
    List<Author> getAuthorsByName(String name);

    @Select("select * from openalex.authors where display_name LIKE CONCAT( #{name}, '%') LIMIT 100")
    List<Author> get100AuthorsByName(String name);

    // 在openalex.authors查找authorIdList中的作者信息
    @Select("<script>" +
            "SELECT * FROM openalex.authors WHERE id IN " +
            "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>" +
            "#{item}" +
            "</foreach>" +
            "</script>")
    List<Author> selectAuthorByIdList(List<String> authorIdList);
}
