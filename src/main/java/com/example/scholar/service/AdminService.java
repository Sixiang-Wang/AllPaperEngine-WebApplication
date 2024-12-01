package com.example.scholar.service;

import com.example.scholar.domain.UserApplication;

import java.util.HashMap;
import java.util.List;

public interface AdminService {
    public HashMap<String, Object> approveApplication(int applicationId, String reason);
    public HashMap<String, Object> rejectApplication(int applicationId, String reason);
    public List<UserApplication> getApplications();
}
