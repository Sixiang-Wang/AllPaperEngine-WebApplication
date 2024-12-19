package com.example.scholar.service;

import com.example.scholar.domain.AuthorForNet;

import java.util.List;

public interface NetService {
    AuthorForNet getNetNodes(int userId);
}
