package com.example.scholar.dao;

import com.example.scholar.config.PathConfig;
import com.example.scholar.domain.WorkConcept;
import com.example.scholar.domain.openalex.Concepts;
import com.example.scholar.domain.openalex.WorksConcepts;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SuggestMapper {
    @Select("select DISTINCT * from works_concepts where concept_id = #{id} order by score desc limit 5")
    @Results({
            @Result(property = "concept", column = "concept_id",
            one=@One(select = PathConfig.pathMapper + "SuggestMapper.selectConceptById")),
            @Result(property = "work", column = "work_id",
            one = @One(select = PathConfig.pathMapper + "WorkMapper.getWorkById"))
    })
    List<WorkConcept> getWorkConceptById(String id);

    @Select("select DISTINCT concept_id from works_concepts where work_id = #{id} order by score desc limit 5")
    List<String> getConceptIdByWorkId(String id);
    @Select("select * from concepts where id = #{id} limit 1")
    Concepts selectConceptById(String id);
}
