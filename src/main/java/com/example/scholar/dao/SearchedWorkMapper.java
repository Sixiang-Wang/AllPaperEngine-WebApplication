package com.example.scholar.dao;

import com.example.scholar.domain.openalexElasticsearch.Works;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface SearchedWorkMapper {

    @Insert("INSERT INTO search_work(publicationid, keywordText) VALUES (#{id}, #{keywordText})")
    int insertSearchWork(String id, String keywordText);

    @Select("select * from search_work")
    List<Map<String, Object>> getAllWorks();

    @Select("SELECT keywordText " +
            "FROM search_work " +
            "GROUP BY keywordText " +
            "ORDER BY COUNT(*) DESC " +
            "LIMIT 100000")
    List<String> getCollectiveNum();

    @Select("select publicationid from search_work where keywordText = #{keyword}")
    List<String> getWorksByKeyword(String keyword);

    @Select("<script>" +
            "SELECT * FROM openalex_works WHERE id IN " +
            "<foreach item='id' collection='ids' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    List<Works> getWorksByIds(@Param("ids") List<String> ids);

    // 每次搜索后用于清空search_work表
    @Delete("TRUNCATE TABLE search_work")
    void clearSearchWork();

}
