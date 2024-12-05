package com.example.scholar.dao;

import com.example.scholar.domain.openalex.Work;
import com.example.scholar.dto.HottestKeywordsResult;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface KeywordMapper {

    @Select("SELECT oa_works.* "+
            "FROM openalex_works_keywords oa_kw "+
            "JOIN openalex_works oa_works oa_works "+
            "ON oa_kw.work_id = oa_works.id "+
            "WHERE oa_kw.keyword_display_name = #{displayName}")
    List<Work> getWorksByKeywords(String displayName);



    @Select("SELECT oa_works.* "+
            "FROM openalex_works_keywords oa_kw "+
            "JOIN openalex_works oa_works "+
            "ON oa_kw.work_id = oa_works.id "+
            "WHERE oa_kw.keyword_display_name = #{displayName} "+
            "ORDER BY oa_kw.cited_by_count DESC "+
            "limit #{limit}")
    List<Work> getTopNWorksByKeywords(String displayName,int limit);


    @Results({
            @Result(property = "id", column = "keyword_id"),
            @Result(property = "displayName", column = "keyword_display_name"),
            @Result(property = "totalScore", column = "total_score_cited"),
            @Result(property = "rank", column = "rank1")
    })
    @Select("SELECT keyword_id, " +
            "keyword_display_name, " +
            "SUM(score * cited_by_count) AS total_score_cited, " +
            "ROW_NUMBER() OVER (ORDER BY SUM(score * cited_by_count) DESC) AS rank1 " +
            "FROM openalex_works_keywords " +
            "WHERE publication_year BETWEEN #{fromYear} AND #{toYear} " +
            "GROUP BY keyword_display_name " +
            "ORDER BY total_score_cited DESC " +
            "LIMIT #{limit}")
    List<HottestKeywordsResult> getTopNKeywordsByScore(
            int fromYear,
            int toYear,
            int limit);







}
