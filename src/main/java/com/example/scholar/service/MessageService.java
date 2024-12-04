package com.example.scholar.service;

import com.example.scholar.domain.Message;
import com.example.scholar.dto.MessageDto;

import java.util.List;

public interface MessageService {
    int createMessage(int fromId, int toId, String message);

    int deleteMessage(int userId, int messageId);

    int welcomeMessage(int toId);
    List<MessageDto> getMessages(int userId);
    int readMessage(int userId, int messageId);
    MessageDto getMessage(int messageId);

    void deleteAllMessage(int userid);
}
