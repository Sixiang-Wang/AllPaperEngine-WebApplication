package com.example.scholar.controller;

import com.example.scholar.entity.R;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(value="/test")
public class TestController {
    @GetMapping(value="/111")
    public R test() {
        return R.ok("111");
    }
}
