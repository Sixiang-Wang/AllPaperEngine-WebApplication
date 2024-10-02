package com.example.scholar.service.impl;


import com.example.scholar.dao.UserTokenMapper;
import com.example.scholar.domain.UserToken;
import com.example.scholar.service.UserTokenService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;

@Service("userTokenService")
public class UserTokenServiceImpl implements UserTokenService {
    @Resource
    private UserTokenMapper userTokenMapper;
}
