package com.example.scholar.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
@ApiModel(value="这是测试dto")
public class TestDto {
    @ApiModelProperty(value = "测试参数")
    private int num;

}
