package com.example.scholar.dao;

import com.example.scholar.domain.openalex.Author;
import com.example.scholar.domain.openalex.Institutions;
import com.example.scholar.domain.openalex.Work;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface InstitutionMapper {




    @Select("select * from openalex.institutions where id = #{id}")
    Institutions getInstitutionById(String id);
    @Select("select * from openalex.institutions where display_name REGEXP CONCAT('(?i)', #{name}) limit 10")
    List<Institutions> getInstitutionByName(String name);
    @Select("select id from openalex.institutions where display_name REGEXP CONCAT('(?i)', #{name}) limit 10")
    List<String> getInstitutionIdByName(String name);

    // 在authors表的last_known_institution列中查找为#{institutionId}的作者，并返回整行数据
    @Select("select * from openalex.authors where last_known_institution = #{institutionId}")
    List<Author> getAuthorByInstitutionId(String institutionId);

    // 在openalex.works_authorships表中查找institution_id对应的author_id（不重复的author_id）
    @Select("select distinct author_id from openalex.works_authorships where institution_id = #{institutionId}")
    List<String> getAuthorIdByInstitutionId(String institutionId);

//    @Select() ;
//    List<Institutions> getTopNMostCitedInstitution(int number);
//@Select("select id from openalex.institutions where display_name REGEXP CONCAT('(?i)', #{name}) limit 10")
}
