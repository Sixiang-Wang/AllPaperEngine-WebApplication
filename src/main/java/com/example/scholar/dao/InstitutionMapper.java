package com.example.scholar.dao;

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

//    @Select() ;
//    List<Institutions> getTopNMostCitedInstitution(int number);
//@Select("select id from openalex.institutions where display_name REGEXP CONCAT('(?i)', #{name}) limit 10")
}
