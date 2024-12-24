package com.example.scholar.util;

import com.example.scholar.service.ElasticWorkService;

import javax.annotation.Resource;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.stream.Collectors;

public class SQLWorkBuilder {

    public static String buildSQLQuery(Set<String> workIds, Date from,Date to) {
        StringBuilder query = new StringBuilder("SELECT * FROM works WHERE ");

        List<String> list = workIds.stream().collect(Collectors.toList());

        if(!list.isEmpty()){
            query.append("id IN (");
            for(int i=0;i<list.size();i++){

                query.append("'").append(list.get(i)).append("'");
                if(i!=list.size()-1){
                    query.append(" ,");
                }else{
                    query.append(")");
                }
            }
        }


        // 添加 publication_date 条件
        if (from != null || to != null) {
            if (!list.isEmpty()) {
                query.append(" AND ");
            }
            query.append("publication_date ");
            if (from != null && to != null) {
                query.append("BETWEEN '")
                        .append(new java.sql.Date(from.getTime()))
                        .append("' AND '")
                        .append(new java.sql.Date(to.getTime()))
                        .append("' ");
            } else if (from != null) {
                query.append(">= '")
                        .append(new java.sql.Date(from.getTime()))
                        .append("' ");
            } else if (to != null) {
                query.append("<= '")
                        .append(new java.sql.Date(to.getTime()))
                        .append("' ");
            }
        }


        query.append(" limit 5000");

        return query.toString();
    }




    public static void main(String[] args) {
        // 示例数据



    }
}
