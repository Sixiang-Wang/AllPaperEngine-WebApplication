package com.example.scholar.dao;

import com.example.scholar.domain.User;
import com.example.scholar.domain.openalex.Concepts;
import com.example.scholar.domain.openalex.ConceptsRelatedConcepts;
import com.example.scholar.domain.openalex.WorksConcepts;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ConceptsMapper {
    @Select("select * from openalex.concepts where id = #{conceptsId}")
    Concepts selectConceptsById(String conceptsId);
    @Select("select * from openalex.works_concepts where work_id = #{workId}")
    List<WorksConcepts> getWorksConceptsListById(String workId);
    @Select("select * from openalex.concepts_related_concepts where concept_id = #{conceptsId}")
    List<ConceptsRelatedConcepts> getConceptsRelatedConceptsListByConceptsId(String conceptsId);
}
