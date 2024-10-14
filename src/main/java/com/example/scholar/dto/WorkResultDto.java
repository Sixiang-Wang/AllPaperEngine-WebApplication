package com.example.scholar.dto;

import lombok.Data;

@Data
public class WorkResultDto {
    private String title;
    private String paperInformation;
    private String abstractText;
    private int cited;

}
