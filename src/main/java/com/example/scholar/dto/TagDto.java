package com.example.scholar.dto;

import lombok.Data;

import java.util.List;

@Data
public class TagDto {
    int userId;
    List<String> tags;
}
