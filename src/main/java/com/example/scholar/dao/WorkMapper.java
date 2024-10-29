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
    @Select("select * from openalex_works where match(title) against(#{word})")
    List<Work> selectWorksByTitleWord(String word);
}
