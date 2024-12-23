package com.example.scholar.dao;

import com.example.scholar.config.PathConfig;
import com.example.scholar.domain.User;
import com.example.scholar.domain.openalex.Concepts;
import com.example.scholar.domain.openalex.ConceptsRelatedConcepts;
import com.example.scholar.domain.openalex.WorksConcepts;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ConceptsMapper {
    @Select("select * from openalex.concepts where id = #{conceptsId}")
    Concepts selectConceptsById(String conceptsId);
    @Select("select distinct display_name from concepts where id = #{id}")
    String selectConceptNameById(String id);
    @Select("select distinct * from openalex.works_concepts where work_id = #{workId}")
    @Results({
            @Result(property = "displayName", column = "concept_id",
            one = @One(select = PathConfig.pathMapper + "ConceptsMapper.selectConceptNameById"))
    })
    List<WorksConcepts> getWorksConceptsListById(String workId);
    @Select("select * from openalex.concepts_related_concepts where concept_id = #{conceptsId}")
    List<ConceptsRelatedConcepts> getConceptsRelatedConceptsListByConceptsId(String conceptsId);
}
