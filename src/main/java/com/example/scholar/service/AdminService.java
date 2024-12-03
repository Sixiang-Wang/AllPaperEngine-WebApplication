package com.example.scholar.service;

import com.example.scholar.domain.UserApplication;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public interface AdminService {
    HashMap<String, Object> approveApplication(int applicationId, String reason);
    HashMap<String, Object> rejectApplication(int applicationId, String reason);
    List<UserApplication> getApplications();
}
