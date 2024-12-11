package com.example.scholar.dao;

import com.example.scholar.domain.openalexElasticsearch.Works;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface SearchedWorkMapper {

    @Update("""
        DROP TABLE IF EXISTS search_work;
        """)
    void dropTableIfExists();

    @Update("""
        CREATE TABLE search_work (
            id VARCHAR(255) PRIMARY KEY,
            keywordsText VARCHAR(255) NOT NULL
        )
        """)
    void createTable();

    @Insert("INSERT INTO search_work(id, keywordsText)" +
    "VALUES (#{id}, #{keywordsText})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertSearchWork(Works works);

    @Select("select * from search_work")
    List<Map<String, Object>> getAllWorks();

    @Select("SELECT keywordsText " +
            "FROM search_work " +
            "GROUP BY keywordsText " +
            "ORDER BY COUNT(*) DESC " +
            "LIMIT 100000")
    List<String> getCollectiveNum();

    @Select("select id from search_work where keywordsText = #{keyword}")
    List<String> getWorksByKeyword(String keyword);

    @Select("<script>" +
            "SELECT * FROM openalex_works WHERE id IN " +
            "<foreach item='id' collection='ids' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    List<Works> getWorksByIds(@Param("ids") List<String> ids);

}
