package com.example.scholar.dao;

import com.example.scholar.domain.openalexElasticsearch.Works;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface SearchedWorkMapper {

    @Insert("INSERT INTO search_work(workid, keywordText, type, institution, publicationYear) VALUES (#{id}, #{keywordText}, #{type}, #{institution}, #{publicationYear})")
    int insertSearchWork(String id, String keywordText, String type, String institution, Integer publicationYear);

    @Update("UPDATE search_work SET keywordText = #{keywordText} WHERE workid = #{id}")
    int updateKeywordText(String id, String keywordText);

    @Select("select * from search_work")
    List<Map<String, Object>> getAllWorks();

    @Select("SELECT keywordText " +
            "FROM search_work " +
            "WHERE keywordText IS NOT NULL " +
            "GROUP BY keywordText " +
            "ORDER BY COUNT(*) DESC " +
            "LIMIT 10")
    List<String> getCollectiveNum();

    @Select("SELECT type " +
            "FROM search_work " +
            "WHERE type IS NOT NULL " +
            "GROUP BY type " +
            "ORDER BY COUNT(DISTINCT workid) DESC " +
            "LIMIT 10")
    List<String> getTypeNum();

    @Select("SELECT institution " +
            "FROM search_work " +
            "WHERE institution IS NOT NULL " +
            "GROUP BY institution " +
            "ORDER BY COUNT(DISTINCT workid) DESC " +
            "LIMIT 10")
    List<String> getInstitutionNum();

    @Select("SELECT publicationYear " +
            "FROM search_work " +
            "WHERE publicationYear IS NOT NULL " +
            "GROUP BY publicationYear " +
            "ORDER BY COUNT(DISTINCT workid) DESC " +
            "LIMIT 10")
    List<Integer> getPublictionYearsNum();

    @Select("select topic_id from works_topics where work_id = #{work_id} " + "LIMIT 1")
    String getTopicIdByWorkId(String work_id);

    @Select("select keywords from topics where id = #{topicId} " + "LIMIT 1")
    String getKeywordsById(String topicId);

    @Select("SELECT i.display_name " +
            "FROM works_authorships wa " +
            "JOIN institutions i ON wa.institution_id = i.id " +
            "WHERE wa.work_id = #{work_id} " +
            "LIMIT 1")
    String getInstitutionNameByWorkId(String work_id);

    @Select("select workid from search_work where keywordText = #{keyword}")
    List<String> getWorksByKeyword(String keyword);

    @Select("select workid from search_work where type = #{type}")
    List<String> getWorksByType(String type);

    @Select("select workid from search_work where institution = #{institution}")
    List<String> getWorksByInstitution(String institution);

    @Select("select workid from search_work where publicationYear = #{publicationYear}")
    List<String> getWorksByPublictionYears(Integer publicationYear);

    @Select("<script>" +
            "SELECT workid " +
            "FROM search_work " +
            "WHERE 1=1 " +
            "<if test='keyword != null and keyword != \"\"'> " +
            "AND keywordText = #{keyword} " +
            "</if> " +
            "<if test='type != null and type != \"\"'> " +
            "AND type = #{type} " +
            "</if> " +
            "<if test='institution != null and institution != \"\"'> " +
            "AND institution = #{institution} " +
            "</if> " +
            "<if test='publicationYear != null'> " +
            "AND publicationYear = #{publicationYear} " +
            "</if> " +
            "</script>")
    List<String> getWorksByConditions(@Param("keyword") String keyword,
                                      @Param("type") String type,
                                      @Param("institution") String institution,
                                      @Param("publicationYear") Integer publicationYear);

    @Select("<script>" +
            "SELECT * FROM works WHERE id IN " +
            "<foreach item='id' collection='ids' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    List<Works> getWorksByIds(@Param("ids") List<String> ids);

    // 每次搜索后用于清空search_work表
    @Delete("TRUNCATE TABLE search_work")
    void clearSearchWork();

}
