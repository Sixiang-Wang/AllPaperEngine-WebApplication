package com.example.scholar.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SQLInstitutionBuilder {


    /**
     * 生成 SQL 查询字符串
     *
     * @param andInstitutions 必须包含的机构名称列表
     * @param orInstitutions  可以包含的机构名称列表
     * @param notInstitutions 不能包含的机构名称列表
     * @return 生成的 SQL 查询字符串
     */
    public static String buildSQLQuery(List<String> andInstitutions, List<String> orInstitutions, List<String> notInstitutions) {
        StringBuilder query = new StringBuilder("SELECT id FROM institutions WHERE ");

        // 处理 AND 条件
        if (andInstitutions != null && !andInstitutions.isEmpty()) {
            query.append("display_name IN (")
                    .append(String.join(",", quoteItems(andInstitutions)))
                    .append(")");
        }

        // 处理 OR 条件
        if (orInstitutions != null && !orInstitutions.isEmpty()) {
            if (andInstitutions != null && !andInstitutions.isEmpty()) {
                query.append(" OR ");
            }
            query.append("display_name IN (")
                    .append(String.join(",", quoteItems(orInstitutions)))
                    .append(")");
        }

        // 如果没有 AND 或 OR 条件，直接返回查询
        if ((andInstitutions == null || andInstitutions.isEmpty()) &&
                (orInstitutions == null || orInstitutions.isEmpty())) {
            query.append("id like 'I10000573821'"); // 如果没有条件，返回一个记录
        }

        // 处理 NOT 条件
        if (notInstitutions != null && !notInstitutions.isEmpty()) {
            query.append(" AND display_name NOT IN (")
                    .append(String.join(",", quoteItems(notInstitutions)))
                    .append(")");
        }

        query.append(" limit 20");

        return query.toString();
    }
    public static String buildSQLQuery1(Set<String> institution_id) {
        List<String> institution_ids = institution_id.stream().collect(Collectors.toList());
        StringBuilder query = new StringBuilder("SELECT work_id FROM works_authorships WHERE ");

        // 全部or在一起
        if (institution_ids != null && !institution_ids.isEmpty()) {
            for(int i=0;i<institution_ids.size();i++){
                query.append("institution_id like ")
                        .append("'") .append(institution_ids.get(i)).append("'");
                if(i != institution_ids.size()-1){
                    query.append(" OR ");
                }
            }
        }else{
            query.append("institution_id like '123456778'");
        }
        query.append(" limit 20");
        return query.toString();
    }


    /**
     * 将列表中的每个元素加上单引号，用于 SQL 查询
     *
     * @param items 输入的列表
     * @return 加上单引号的字符串列表
     */
    private static List<String> quoteItems(List<String> items) {
        return items.stream()
                .map(item -> "'" + item + "'")
                .toList();
    }

}
