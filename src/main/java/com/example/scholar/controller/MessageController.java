package com.example.scholar.controller;

import com.example.scholar.config.annotation.TokenToUser;
import com.example.scholar.dao.MessageMapper;
import com.example.scholar.domain.User;
import com.example.scholar.domain.constant.R;
import com.example.scholar.dto.MessageDto;
import com.example.scholar.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation("通过token获得用户的所有信息")
    public R getUserMessages(@TokenToUser User user){
        try{
            List<MessageDto> messages = messageService.getMessages(user.getUserid());
            return R.ok("get success").put("messages", messages);
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
    @GetMapping(value="/isRedPoint")
    @ApiOperation("判断用户是否还有未读消息")
    public R isRedPoint(@TokenToUser User user){
        try{
            return R.ok().put("isRed", messageMapper.getCountOfUnreadMessage(user.getUserid()) != 0);
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
    @GetMapping(value= "/delete")
    @ApiOperation("删除消息")
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
    @GetMapping(value="deleteall")
    @ApiOperation("删除所有消息")
    public R deleteAllMessage(@TokenToUser User user){
        try{
            messageService.deleteAllMessage(user.getUserid());
            return R.ok("delete success");
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
    @GetMapping(value="/read")
    @ApiOperation("将信息标记为已读")
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
    @ApiOperation("实现某人给另一人发送消息的接口")
    public R addMessage(@RequestParam("fromId")int fromId, @RequestParam("toId")int toId, @RequestParam("index")String messageIndex){
        try{
            messageService.createMessage(fromId, toId, messageIndex);
            return R.ok("success");
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
    @GetMapping(value="/selectone")
    @ApiOperation("通过信息ID获取某个message的接口")
    public R getOneMessage(@RequestParam("id")int messageId){
        try{
            MessageDto res = messageService.getMessage(messageId);
            return R.ok().put("message", res);
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
}
