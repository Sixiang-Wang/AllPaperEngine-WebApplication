package com.example.scholar.service;

import com.example.scholar.domain.Complaint;

import java.util.List;

public interface ComplaintService {
    int addComplaint(int userId, String workId, String nameReal, String workPlace, String mail,
                     String reason,String addition);

    int dealComplaint(int complaintId, int result);

    List<Complaint> getAllComplaints();
}
