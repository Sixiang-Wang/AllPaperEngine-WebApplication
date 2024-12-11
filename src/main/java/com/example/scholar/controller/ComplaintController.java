package com.example.scholar.controller;

import com.example.scholar.domain.constant.R;
import com.example.scholar.dto.ComplaintDto;
import com.example.scholar.service.ComplaintService;
import com.example.scholar.service.MessageService;
import io.swagger.annotations.ApiOperation;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Date;

@CrossOrigin
@RestController
@RequestMapping("/complaint")
public class ComplaintController {
    @Resource
    private ComplaintService complaintService;
    @Resource
    private MessageService messageService;
    @GetMapping("/getall")
    @ApiOperation("获得所有申诉表")
    public R getAllComplaints(){
        try{
            return R.ok().put("complaints", complaintService.getAllComplaints());
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
    @PostMapping("/add")
    public R addComplaint(@RequestBody ComplaintDto complaint){
        try {
            String nameReal = complaint.getNameReal();
            String mail = complaint.getMail();
            int userId = complaint.getUserId();
            String workId= complaint.getWorkId();
            String reason = complaint.getReason();
            String addition = complaint.getAddition();
            String workplace = complaint.getWorkplace();
            complaintService.addComplaint(userId,workId,nameReal,workplace,mail,reason,addition);
            return R.ok("success");
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
    @GetMapping("/deal")
    public R dealComplaint(@RequestParam("complaintId")int complaintId, @RequestParam("result")int result){
        try{
            int res = complaintService.dealComplaint(complaintId, result);
            if(res != 0){
                return R.ok("success");
            }else{
                return R.error("something went wrong");
            }
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
}
