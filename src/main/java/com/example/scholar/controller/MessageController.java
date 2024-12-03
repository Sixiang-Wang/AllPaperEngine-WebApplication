package com.example.scholar.controller;

import com.example.scholar.config.annotation.TokenToUser;
import com.example.scholar.dao.MessageMapper;
import com.example.scholar.domain.User;
import com.example.scholar.domain.constant.R;
import com.example.scholar.dto.MessageDto;
import com.example.scholar.service.MessageService;
import org.apache.coyote.Request;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value="/message")
public class MessageController {
    @Resource
    private MessageService messageService;
    @Resource
    private MessageMapper messageMapper;
    @GetMapping(value = "/select")
    public R getUserMessages(@TokenToUser User user){
        try{
            List<MessageDto> messages = messageService.getMessages(user.getUserid());
            return R.ok("get success").put("messages", messages);
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
    @GetMapping(value="/isRedPoint")
    public R isRedPoint(@TokenToUser User user){
        try{
            return R.ok().put("isRed", messageMapper.getCountOfUnreadMessage(user.getUserid()) != 0);
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
    @GetMapping(value= "/delete")
    public R deleteMessage(@TokenToUser User user, @RequestParam("messageId")int messageId){
        try{
            int res = messageService.deleteMessage(user.getUserid(), messageId);
            if(res == 1){
                return R.ok("success");
            }else{
                return R.error("something wrong");
            }
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
    @GetMapping(value="/read")
    public R readMessage(@TokenToUser User user, @RequestParam("messageId")int messageId){
        try{
            int res = messageService.readMessage(user.getUserid(), messageId);
            if(res == 1){
                return R.ok("success");
            }else{
                return R.error("something wrong");
            }
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
    @GetMapping(value="/add")
    public R addMessage(@RequestParam("fromId")int fromId, @RequestParam("toId")int toId, @RequestParam("index")String messageIndex){
        try{
            messageService.createMessage(fromId, toId, messageIndex);
            return R.ok("success");
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
    @GetMapping(value="/selectone")
    public R getOneMessage(@RequestParam("id")int messageId){
        try{
            MessageDto res = messageService.getMessage(messageId);
            return R.ok().put("message", res);
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
}
