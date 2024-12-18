package com.example.scholar.dao;

import com.example.scholar.domain.openalexElasticsearch.Works;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface SearchedWorkMapper {

    @Insert("INSERT INTO search_work(id, keywordsText, publication_year, type, work_language)" +
    "VALUES (#{id}, #{keywordsText}, #{publication_year}, #{type}, #{language})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertSearchWork(Works works);

    @Select("select * from search_work")
    List<Map<String, Object>> getAllWorks();

    @Select("SELECT keywordsText " +
            "FROM search_work " +
            "GROUP BY keywordsText " +
            "ORDER BY COUNT(*) DESC " +
            "LIMIT 10")
    List<String> getCollectiveNum();

//    @Select("SELECT publication_year " +
//            "FROM search_work " +
//            "GROUP BY publication_year " +
//            "ORDER BY COUNT(*) DESC " +
//            "LIMIT 10")
//    List<Integer> getPublication_years();

    @Select("SELECT type " +
            "FROM search_work " +
            "GROUP BY type " +
            "ORDER BY COUNT(*) DESC " +
            "LIMIT 10")
    List<String> getTypes();

    @Select("SELECT type " +
            "FROM search_work " +
            "GROUP BY work_language " +
            "ORDER BY COUNT(*) DESC " +
            "LIMIT 10")
    List<String> getLanguages();

    @Select("select id from search_work where keywordsText = #{keyword}")
    List<String> getWorksByKeyword(String keyword);

//    @Select("select id from search_work where publication_year = #{year}")
//    List<String> getWorksByYear(int year);

    @Select("select id from search_work where type = #{type}")
    List<String> getWorksByType(String type);

    @Select("select id from search_work where work_language = #{work_language}")
    List<String> getWorksByLanguage(String work_language);

    @Select("<script>" +
            "SELECT * FROM openalex_works WHERE id IN " +
            "<foreach item='id' collection='ids' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    List<Works> getWorksByIds(@Param("ids") List<String> ids);

}
