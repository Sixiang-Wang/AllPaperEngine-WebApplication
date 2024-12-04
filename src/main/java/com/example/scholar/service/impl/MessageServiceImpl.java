package com.example.scholar.service.impl;

import com.example.scholar.dao.MessageMapper;
import com.example.scholar.dao.UserMapper;
import com.example.scholar.domain.Message;
import com.example.scholar.domain.User;
import com.example.scholar.dto.MessageDto;
import com.example.scholar.service.MessageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service("messageService")
public class MessageServiceImpl implements MessageService {
    @Resource
    private MessageMapper messageMapper;
    @Resource
    private UserMapper userMapper;

    @Override
    public int createMessage(int fromId, int toId, String message) {
        Date date = new Date(System.currentTimeMillis());
        messageMapper.createMessage(fromId, toId, message, date);
        return 0;
    }

    @Override
    public int deleteMessage(int userId,int messageId) {
        Message message = messageMapper.selectMessageById(messageId);
        if(message.getToId() != userId){
            //请求删除者不是信息的接受者
            return 0;
        }else{
            messageMapper.deleteMessageById(messageId);
        }
        return 1;
    }

    @Override
    public int welcomeMessage(int toId) {
        User user = userMapper.selectUserById(toId);
        if(user == null){
            return 0;
        }else{
            String name = user.getName();
            StringBuilder welcomeText = new StringBuilder();
            welcomeText.append("嘻嘻，这都让你注册上了，").append(name);
            messageMapper.createMessage(1,toId,welcomeText.toString(),new Date(System.currentTimeMillis()));//由管理员monica发送
            return 1;//发送成功
        }
    }

    @Override
    public List<MessageDto> getMessages(int userId) {
        List<Message> messages = messageMapper.selectMessagesByUser(userId);
        List<MessageDto> res = new ArrayList<>();
        for(Message message: messages){
            MessageDto messageDto = new MessageDto(message.getId(),message.getMessageIndex(),message.getDate(),message.getIsRead(),message.getFromUser().getName());
            res.add(messageDto);
        }
        return res;
    }

    @Override
    public int readMessage(int userId, int messageId) {
        Message message = messageMapper.selectMessageById(messageId);
        if(message.getToId() != userId){
            //请求删除者不是信息的接受者
            return 0;
        }else{
            messageMapper.readMessage(messageId);
        }
        return 1;
    }

    @Override
    public MessageDto getMessage(int messageId) {
        Message message = messageMapper.selectMessageById(messageId);
        User user = userMapper.selectUserById(message.getFromId());
        MessageDto messageDto = new MessageDto(message.getId(),message.getMessageIndex(), message.getDate(), message.getIsRead(), user.getName());
        return messageDto;
    }

    @Override
    public void deleteAllMessage(int userid) {
        messageMapper.deleteAllMessage(userid);
    }

}
