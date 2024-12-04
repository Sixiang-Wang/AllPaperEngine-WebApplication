package com.example.scholar.service;


import com.example.scholar.domain.myenum.AppealReasonType;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public interface UserAppealService {
    HashMap<String, Object> appealWork(int userId, int workId, String name, String company, AppealReasonType appealReasonType, String mail, String reason);
}
