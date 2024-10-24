package com.example.scholar.dto;

import lombok.Data;

import java.util.List;

@Data
public class InstitutionsResultDto {
    //用于Json解析Institutions分析
    String id;
    String ror;
    String type;
    List<String> lineage;
    String country_code;
    String display_name;
}
