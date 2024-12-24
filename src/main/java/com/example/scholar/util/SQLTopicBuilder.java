package com.example.scholar.util;

import java.util.List;

public class SQLTopicBuilder {

    public static String buildSQLQuery(List<String> andTopics, List<String> orTopics, List<String> notTopics) {
        StringBuilder query = new StringBuilder("SELECT id FROM topics WHERE ");

        // 处理 AND 条件
        if (andTopics != null && !andTopics.isEmpty()) {
            query.append("display_name IN (")
                    .append(String.join(",", quoteItems(andTopics)))
                    .append(")");
        }

        // 处理 OR 条件
        if (orTopics != null && !orTopics.isEmpty()) {
            if (andTopics != null && !andTopics.isEmpty()) {
                query.append(" OR ");
            }
            query.append("display_name IN (")
                    .append(String.join(",", quoteItems(orTopics)))
                    .append(")");
        }

        // 如果没有 AND 或 OR 条件，直接返回查询
        if ((andTopics == null || andTopics.isEmpty()) &&
                (orTopics == null || orTopics.isEmpty())) {
            query.append("id like 'I10000573821'"); // 如果没有条件，返回0个记录
        }

        // 处理 NOT 条件
        if (notTopics != null && !notTopics.isEmpty()) {
            query.append(" AND display_name NOT IN (")
                    .append(String.join(",", quoteItems(notTopics)))
                    .append(")");
        }

        query.append(" limit 20");

        return query.toString();
    }


    private static List<String> quoteItems(List<String> items) {
        return items.stream()
                .map(item -> "'" + item + "'")
                .toList();
    }
}
