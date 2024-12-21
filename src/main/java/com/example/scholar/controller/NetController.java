package com.example.scholar.controller;

import com.example.scholar.config.annotation.TokenToUser;
import com.example.scholar.domain.AuthorForNet;
import com.example.scholar.domain.User;
import com.example.scholar.domain.constant.R;
import com.example.scholar.dto.net.NetConfig;
import com.example.scholar.dto.net.NetData;
import com.example.scholar.dto.net.NetDataType;
import com.example.scholar.dto.net.NetLink;
import com.example.scholar.service.NetService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/net")
public class NetController {
    @Resource
    private NetService netService;
    @GetMapping("/get")
    public R getNetByUser(@TokenToUser User user){
        try{
            AuthorForNet res = netService.getNetNodes(user.getUserid());
            if(res == null){
                return R.ok("something went wrong");
            }
            List<NetData> data = new ArrayList<>();
            List<NetLink> links = new ArrayList<>();
            //获取中心点
            NetData netData = new NetData();
            netData.setName(res.getName());
            netData.setSymbolSize(NetConfig.AUTHOR_SYMBOL_SIZE);
            data.add(netData);
            for (AuthorForNet authorForNet : res.getRelatedAuthors()) {
                if(authorForNet.getType() == NetDataType.WORK_RELATED) {
                    if (!res.getName().equals(authorForNet.getName())) {
                        NetData tmp = new NetData();
                        tmp.setName(authorForNet.getName());
                        tmp.setSymbolSize(NetConfig.RELATE_AUTHOR_SYMBOL_SIZE);
                        data.add(tmp);
                    }
                    if (!res.getName().equals(authorForNet.getName())) {
                        NetLink netLink = new NetLink();
                        netLink.setSource(res.getName());
                        netLink.setTarget(authorForNet.getName());
                        links.add(netLink);
                    }
                }else if (authorForNet.getType() == NetDataType.INSTITUTION_RELATED){
                    if (!res.getName().equals(authorForNet.getName())) {
                        NetData tmp = new NetData();
                        tmp.setName(authorForNet.getName());
                        tmp.setSymbolSize(NetConfig.RELATE_INSTITUTION_AUTHOR_SYMBOL_SIZE);
                        data.add(tmp);
                    }
                    if (!res.getName().equals(authorForNet.getName())) {
                        NetLink netLink = new NetLink();
                        netLink.setSource(res.getName());
                        netLink.setTarget(authorForNet.getName());
                        links.add(netLink);
                    }
                }
            }
            return R.ok().put("data", data).put("links", links);
        }catch (Exception e){
            return R.error(e.toString());
        }
        //{
        //  "code": 200,
        //  "net": {
        //    "id": "https://openalex.org/A5006602845",
        //    "displayNameAlterNatives": "[\"Michail Churnosov\", \"Mikhail Ivanovich Churnosov\", \"M. Churnosov\", \"М. И. Чурносов\", \"Churnosov Mi\", \"Mikhail I. Churnosov\", \"Churnosov M.I. Churnosov\", \"Churnosov M.I. Churnosov M\", \"Mikhail Churnosov\", \"M. L. Churnosov\", \"M. I. Churnosov\"]",
        //    "name": "Michail Churnosov",
        //    "relatedAuthors": [
        //      {
        //        "id": "https://openalex.org/A5062725362",
        //        "displayNameAlterNatives": "[\"Oksana Minyaylo\", \"O. N. Minyaylo\", \"Oksana N. Minyaylo\"]",
        //        "name": "Oksana Minyaylo",
        //        "relatedAuthors": null
        //      },
        //      {
        //        "id": "https://openalex.org/A5030703501",
        //        "displayNameAlterNatives": "[\"Irina Ponomarenko\", \"I. V. Ponomarenko\", \"Irina V. Ponomarenko\", \"Ponomarenko I.V. Ponomarenko\", \"Irina Vasilevna Ponomarenko\", \"И. В. Пономаренко\", \"I. Ponomarenko\"]",
        //        "name": "Irina Ponomarenko",
        //        "relatedAuthors": null
        //      },
        //      {
        //        "id": "https://openalex.org/A5006602845",
        //        "displayNameAlterNatives": "[\"Michail Churnosov\", \"Mikhail Ivanovich Churnosov\", \"M. Churnosov\", \"М. И. Чурносов\", \"Churnosov Mi\", \"Mikhail I. Churnosov\", \"Churnosov M.I. Churnosov\", \"Churnosov M.I. Churnosov M\", \"Mikhail Churnosov\", \"M. L. Churnosov\", \"M. I. Churnosov\"]",
        //        "name": "Michail Churnosov",
        //        "relatedAuthors": null
        //      }
        //    ]
        //  }
        //}
    }
    @GetMapping("/get2")
    public R getNetByUser(@RequestParam("userId")int userId){
        try{
            AuthorForNet res = netService.getNetNodes(userId);
            if(res == null){
                return R.ok("something went wrong");
            }
            List<NetData> data = new ArrayList<>();
            List<NetLink> links = new ArrayList<>();
            //获取中心点
            NetData netData = new NetData();
            netData.setName(res.getName());
            netData.setSymbolSize(NetConfig.AUTHOR_SYMBOL_SIZE);
            data.add(netData);
            for(AuthorForNet authorForNet: res.getRelatedAuthors()){
                if(!res.getName().equals(authorForNet.getName())) {
                    NetData tmp = new NetData();
                    tmp.setName(authorForNet.getName());
                    tmp.setSymbolSize(NetConfig.RELATE_AUTHOR_SYMBOL_SIZE);
                    data.add(tmp);
                }
                if(!res.getName().equals(authorForNet.getName())) {
                    NetLink netLink = new NetLink();
                    netLink.setSource(res.getName());
                    netLink.setTarget(authorForNet.getName());
                    links.add(netLink);
                }
            }
            return R.ok().put("data", data).put("links", links);
        }catch (Exception e){
            return R.error(e.toString());
        }
        //{
        //  "code": 200,
        //  "net": {
        //    "id": "https://openalex.org/A5006602845",
        //    "displayNameAlterNatives": "[\"Michail Churnosov\", \"Mikhail Ivanovich Churnosov\", \"M. Churnosov\", \"М. И. Чурносов\", \"Churnosov Mi\", \"Mikhail I. Churnosov\", \"Churnosov M.I. Churnosov\", \"Churnosov M.I. Churnosov M\", \"Mikhail Churnosov\", \"M. L. Churnosov\", \"M. I. Churnosov\"]",
        //    "name": "Michail Churnosov",
        //    "relatedAuthors": [
        //      {
        //        "id": "https://openalex.org/A5062725362",
        //        "displayNameAlterNatives": "[\"Oksana Minyaylo\", \"O. N. Minyaylo\", \"Oksana N. Minyaylo\"]",
        //        "name": "Oksana Minyaylo",
        //        "relatedAuthors": null
        //      },
        //      {
        //        "id": "https://openalex.org/A5030703501",
        //        "displayNameAlterNatives": "[\"Irina Ponomarenko\", \"I. V. Ponomarenko\", \"Irina V. Ponomarenko\", \"Ponomarenko I.V. Ponomarenko\", \"Irina Vasilevna Ponomarenko\", \"И. В. Пономаренко\", \"I. Ponomarenko\"]",
        //        "name": "Irina Ponomarenko",
        //        "relatedAuthors": null
        //      },
        //      {
        //        "id": "https://openalex.org/A5006602845",
        //        "displayNameAlterNatives": "[\"Michail Churnosov\", \"Mikhail Ivanovich Churnosov\", \"M. Churnosov\", \"М. И. Чурносов\", \"Churnosov Mi\", \"Mikhail I. Churnosov\", \"Churnosov M.I. Churnosov\", \"Churnosov M.I. Churnosov M\", \"Mikhail Churnosov\", \"M. L. Churnosov\", \"M. I. Churnosov\"]",
        //        "name": "Michail Churnosov",
        //        "relatedAuthors": null
        //      }
        //    ]
        //  }
        //}
    }
}
