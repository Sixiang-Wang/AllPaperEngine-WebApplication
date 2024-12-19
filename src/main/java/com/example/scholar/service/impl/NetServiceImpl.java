package com.example.scholar.service.impl;

import com.example.scholar.dao.NetMapper;
import com.example.scholar.dao.UserMapper;
import com.example.scholar.domain.AuthorForNet;
import com.example.scholar.domain.openalex.Author;
import com.example.scholar.service.NetService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("netService")
public class NetServiceImpl implements NetService {
    @Resource
    private NetMapper netMapper;
    @Resource
    private UserMapper userMapper;
    @Override
    public AuthorForNet getNetNodes(int userId) {
        // TODO: 验证是否为authorId
        if (userMapper.getUserRole(userId) == 0) {
            return null;
        }

        // 通过userId获取用户认证的authorId
        String authorId = netMapper.getAuthorIdByUserId(userId);
        // 通过authorId获取所有论文的id
        List<String> worksIdlist = netMapper.getWorksId(authorId);
        // 通过worksIdList获取对应的其他作者
        List<AuthorForNet> res = new ArrayList<>();
        for (String str : worksIdlist) {
            List<String> tmp = netMapper.getRelatedAuthorIds(str);
            // 获取每条workId对应的相关作者
            for (String authorTmpId : tmp) {
                AuthorForNet authorForNet = netMapper.selectAuthorById(authorTmpId);
                // 获取authorForNet对象
                authorForNet.setName(getFirstArray(authorForNet.getDisplayNameAlterNatives()));
                res.add(authorForNet);
            }
        }

        AuthorForNet authorForNet = netMapper.selectAuthorById(authorId);

        String displayNameAlterNatives = authorForNet.getDisplayNameAlterNatives();
        authorForNet.setName(getFirstArray(displayNameAlterNatives));
        // 设置相关作者
        authorForNet.setRelatedAuthors(res);
        return authorForNet;
    }

    public String getFirstArray(String displayNameAlterNatives){
        // 判断DisplayNameAlterNatives字段是否为JSON数组或对象
        try {

            JsonObject jsonObject = JsonParser.parseString(displayNameAlterNatives).getAsJsonObject();

            if (jsonObject.size() > 0) {
                // 处理为JSON对象时，获取第一个键值对
                String firstKey = jsonObject.keySet().iterator().next();
                String firstValue = jsonObject.get(firstKey).getAsString();
               return firstValue;
            }else{
                return null;
            }
        } catch (Exception e) {
            // 如果是JSON数组格式
            try {
                // 解析为JSON数组
                JsonArray jsonArray = JsonParser.parseString(displayNameAlterNatives).getAsJsonArray();
                if (jsonArray.size() > 0) {
                    // 获取数组中的第一个元素作为name
                    String firstValue = jsonArray.get(0).getAsString();
                    return firstValue;
                }else{
                    return null;
                }
            } catch (Exception ex) {
                // 如果解析失败，可能是格式问题，记录日志或处理异常
                ex.printStackTrace();
            }
        }
        return null;
    }
}
