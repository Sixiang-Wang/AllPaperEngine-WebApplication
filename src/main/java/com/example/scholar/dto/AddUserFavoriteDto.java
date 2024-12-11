package com.example.scholar.dto;

import lombok.Data;

import java.util.List;

@Data
public class AddUserFavoriteDto {
    private int userId;
    private String publicationId;
    private List<String> tags;
}
