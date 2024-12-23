package com.example.scholar.dao;

import com.example.scholar.domain.openalex.Author;
import com.example.scholar.domain.openalex.Institutions;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface InstitutionsMapper {
    @Select("select * from institutions where id = #{institutionId}")
    Institutions selectInstitutionsById(String institutionId);
}
