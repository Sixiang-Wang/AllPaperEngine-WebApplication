package com.example.scholar.service.impl;

import com.example.scholar.dao.ComplaintMapper;
import com.example.scholar.dao.WorkMapper;
import com.example.scholar.domain.Complaint;
import com.example.scholar.domain.UserClaimedWork;
import com.example.scholar.domain.openalex.Work;
import com.example.scholar.service.ComplaintService;
import com.example.scholar.service.MessageService;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.swing.text.DateFormatter;
import java.util.List;

@Service("complaintService")
public class ComplaintServiceImpl implements ComplaintService {
    @Resource
    private ComplaintMapper complaintMapper;
    @Resource
    private MessageService messageService;
    @Resource
    private WorkMapper workMapper;

    @Override
    public int addComplaint(int userId, String workId, String nameReal, String workPlace, String mail,
                            String reason,String addition) {
        Work work = workMapper.getWorkById(workId);
        UserClaimedWork userClaimedWork = complaintMapper.selectUserClaimById(workId);
        if(userClaimedWork == null){
            //改论文未被认证过
            return -1;
        }
        complaintMapper.insertComplaint(userId,nameReal,workPlace,mail,workId,reason,addition);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        messageService.createMessage(1, userId,"您于" + new DateTime(System.currentTimeMillis()).toString(formatter)
        +"提交的对\"" + work.getTitle()+"\"的申诉已成功提交，请等待管理员审核");
        messageService.createMessage(1, userClaimedWork.getUserId(),"您的论文\""+ work.getTitle()+"\"被" +
                "用户申诉，原因是"+reason+"，现已提交至管理员审核，如有疑问，请及时联系管理员。");

        return 0;
    }

    @Override
    public int dealComplaint(int complaintId, int result) {
        Complaint complaint = complaintMapper.selectComplaintById(complaintId);
        UserClaimedWork userClaimedWork = complaintMapper.selectUserClaimById(complaint.getWorkId());
        Work work = workMapper.getWorkById(complaint.getWorkId());
        if(result == 1){
            //审核通过，将complaint的id设为1，表示已经审批通过
            complaintMapper.accessComplaint(complaintId);
            //在user_claim表中删除对应认证信息
            complaintMapper.deleteClaimedWork(userClaimedWork.getUserId(), complaint.getWorkId());
            //发送消息
            messageService.createMessage(1, complaint.getUserId(),"您提交的对"+work.getTitle()+"的申诉信息经验证后生效，已将该论文从其个人门户中删除");
            messageService.createMessage(1, userClaimedWork.getUserId(),"您的论文被其他用户申诉，经管理员审核后"+
                    "确认存在"+ complaint.getReason()+ "的问题，现在已经将您认领过的论文："+work.getTitle()+"从您的个人门户中下架，如有问题请联系管理员");
            return 1;
        }else if(result == 0){
            //审核未通过，将complaint id 设为-1，表示审核被驳回
            complaintMapper.denyComplaint(complaintId);
            messageService.createMessage(1, complaint.getUserId(),"您提交的对"+work.getTitle()+
                    "的申诉信息未通过，如有疑问，请联系管理员.");
            return -1;
        }
        return 0;
    }

    @Override
    public List<Complaint> getAllComplaints() {
        return complaintMapper.getAllComplaints();
    }
}
