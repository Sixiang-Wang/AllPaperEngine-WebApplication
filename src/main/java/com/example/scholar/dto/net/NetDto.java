package com.example.scholar.dto.net;

import lombok.Data;

import java.util.List;

@Data
public class NetDto {
    private List<NetData> data;
    private List<NetLink> links;
}
