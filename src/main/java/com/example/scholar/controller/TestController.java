package com.example.scholar.controller;

import com.example.scholar.dao.WorkMapper;
import com.example.scholar.domain.constant.R;
import com.example.scholar.dto.TestDto;
import com.example.scholar.service.WorkService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin
@RestController
@RequestMapping(value="/test")
public class TestController {
    @Resource
    private WorkService workService;
    @GetMapping(value="/111")
    @ApiOperation(value="这是一个测试接口")
    public R test() {
        return R.ok().put("num",1);
    }
    @GetMapping(value="/recommend")
    @ApiOperation("还没有实际推荐的论文，暂时用这个代替")
    public R recommend(){
        try{
            return R.ok().put("recommends", workService.getRecommends());
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
    @PostMapping(value="/222")
    @ApiOperation(value="post接口测试")
    public R test2(@RequestBody TestDto testDto) {
        return R.ok("222").put("num",testDto.getNum());
    }
}
