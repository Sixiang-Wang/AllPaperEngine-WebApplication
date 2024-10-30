package com.example.scholar.service;

import com.example.scholar.domain.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public interface UserService {
    HashMap<String,Object> login(String account, String password);

    Boolean updateUserAvatar(Integer userid,String avatar);
}
