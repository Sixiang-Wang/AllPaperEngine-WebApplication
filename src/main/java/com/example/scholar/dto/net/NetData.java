package com.example.scholar.dto.net;

import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Component;

@Data

public class NetData {
    private String name;
    private int symbolSize;
    @Transient
    private NetDataType type;
}
