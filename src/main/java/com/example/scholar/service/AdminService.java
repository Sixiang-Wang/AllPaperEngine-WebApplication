package com.example.scholar.service;

import org.springframework.stereotype.Component;

@Component
public interface AdminService {

    public int verifyPassword(String admin,String password);

    int putAdmin(String admin,String password);

    int setPassword(String admin, String password);

    int agree(String admin,String code);

    boolean haveAdmin(String admin);







//    HashMap<String, Object> approveApplication(int applicationId, String reason);
//    HashMap<String, Object> rejectApplication(int applicationId, String reason);
//    List<UserApplication> getApplications();
}
