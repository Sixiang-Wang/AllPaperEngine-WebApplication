package com.example.scholar.dao;

import com.example.scholar.domain.openalexElasticsearch.Works;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface SearchedWorkMapper {

    @Insert("INSERT INTO search_work(publicationid, keywordText, type, worklanguage) VALUES (#{id}, #{keywordText}, #{type}, #{language})")
    int insertSearchWork(String id, String keywordText, String type, String language);

    @Select("select * from search_work")
    List<Map<String, Object>> getAllWorks();

    @Select("SELECT keywordText " +
            "FROM search_work " +
            "GROUP BY keywordText " +
            "ORDER BY COUNT(*) DESC " +
            "LIMIT 100000")
    List<String> getCollectiveNum();

    @Select("SELECT type " +
            "FROM search_work " +
            "GROUP BY type " +
            "ORDER BY COUNT(DISTINCT publicationid) DESC " +
            "LIMIT 100000")
    List<String> getTypeNum();

    @Select("SELECT worklanguage " +
            "FROM search_work " +
            "GROUP BY worklanguage " +
            "ORDER BY COUNT(DISTINCT publicationid) DESC " +
            "LIMIT 100000")
    List<String> getLanguageNum();

    @Select("select publicationid from search_work where keywordText = #{keyword}")
    List<String> getWorksByKeyword(String keyword);

    @Select("select publicationid from search_work where type = #{type}")
    List<String> getWorksByType(String type);

    @Select("select publicationid from search_work where work_language = #{language}")
    List<String> getWorksByLanguage(String language);

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
