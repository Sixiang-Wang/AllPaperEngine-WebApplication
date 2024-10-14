package com.example.scholar.dao;

import com.example.scholar.domain.openalex.Work;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface WorkMapper {
    @Select("select * from openalex_works")
    List<Work> selectAllWorks();
}
