package com.example.scholar.service;

import com.example.scholar.domain.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserService {
    String login(String account, String password);
}
