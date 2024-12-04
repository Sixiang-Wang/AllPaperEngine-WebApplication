package com.example.scholar.controller;

import com.example.scholar.domain.constant.R;
import com.example.scholar.dto.TestDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value="/test")
public class TestController {
    @GetMapping(value="/111")
    @ApiOperation(value="这是一个测试接口")
    public R test() {
        return R.ok().put("num",1);
    }
    @PostMapping(value="/222")
    @ApiOperation(value="post接口测试")
    public R test2(@RequestBody TestDto testDto) {
        return R.ok("222").put("num",testDto.getNum());
    }
}
