package com.example.scholar.dao;

import com.example.scholar.domain.openalex.Work;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface WorkMapper {
    @Select("select * from openalex_works")
    List<Work> selectAllWorks();
    @Select("select * from openalex_works limit #{from},#{to}")
    List<Work> selectAllWorksByPage(int from, int to);
    @Select("select count(*) from openalex_works")
    int getWorksLength();
    @Select("select * from openalex_works where id = #{workId}")
    Work getWorkById(String workId);
    @Select("select * from openalex_works where match(title) against(#{word}) limit #{from},#{to}")
    List<Work> selectWorksByTitleWord(String word,int from,int to);
    @Select("select * from openalex_works where publication_year between #{from} and #{to} limit #{frompage},#{topage}")
    List<Work> selectWorksByPublicationYear(int from,int to,int frompage,int topage);
    @Select("select * from openalex_works where match(title) against(#{word}) and publication_year between #{from} and #{to} limit #{frompage},#{topage}")
    List<Work> selectWorkByTitleAndPublicationYear(String word,int from,int to,int frompage,int topage);
    @Select("select * from openalex_works where match(keywords_text) against(#{word}) limit #{frompage},#{topage}")
    List<Work> selectWorkByKeywords(String word,int frompage,int topage);

}
