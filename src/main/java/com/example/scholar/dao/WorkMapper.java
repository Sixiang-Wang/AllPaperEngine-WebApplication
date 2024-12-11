package com.example.scholar.dao;

import com.example.scholar.domain.openalex.Work;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface WorkMapper {
    @Select("select * from openalex_works")
    List<Work> selectAllWorks();
    @Select("select * from openalex_works limit 20 offset #{from}")
    List<Work> selectAllWorksByPage(int from);
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
    @Select("select count(*) from openalex_works where match(title) against(#{word})")
    int getWorkLengthByTitle(String word);
    @Update("update openalex_works set keywordsText = #{keywords},abstract=#{abstractText} where id = #{id}")
    void insertKeywordsAndAbstract(String id,String keywords,String abstractText);
    /**
     * 参考文献：引用别人的
     */
    @Select("select * from openalex_works where id in (" +
            "select referenced_work_id from openalex_works_referenced_works " +
            "where work_id = #{workId})")
    List<Work> selectWorkByItsReference(String wordId);
    /**
     * 引用文献：别人引用它的
     */
    @Select("select * from openalex_works where id in (" +
            "select work_id from openalex_works_referenced_works " +
            "where referenced_work_id = #{workId})")
    List<Work> selectWorkByReferenceIt(String wordId);


    /**
     * tpz: 这是我暂时用来测试的，别删
     */
    @Select("select * from openalex_works limit 5")
    List<Work> selectRecommendTest();

}
