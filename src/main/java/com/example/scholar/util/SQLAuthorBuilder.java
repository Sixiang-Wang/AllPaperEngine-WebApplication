package com.example.scholar.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SQLAuthorBuilder {

    public static String buildSQLQuery(Set<String> authorIds) {
        StringBuilder query = new StringBuilder("SELECT work_id FROM works_authorships WHERE ");

        List<String> list = authorIds.stream().collect(Collectors.toList());

        if(!list.isEmpty()){
            query.append("author_id IN (");
            for(int i=0;i<list.size();i++){

                query.append("'").append(list.get(i)).append("'");
                if(i!=list.size()-1){
                    query.append(" ,");
                }else{
                    query.append(") limit 20");
                }
            }
        }else{
            query.append("author_id = '123456789'");
        }

        return query.toString();
    }

    public static String buildSQLQuery1(Set<String> authorIds) {
        StringBuilder query = new StringBuilder("SELECT work_id FROM works_authorships WHERE ");

        List<String> list = authorIds.stream().collect(Collectors.toList());

        if(!list.isEmpty()){
            query.append("author_id IN (");
            for(int i=0;i<list.size();i++){

                query.append("'").append(list.get(i)).append("'");
                if(i!=list.size()-1){
                    query.append(" ,");
                }else{
                    query.append(") and author_position like 'first' limit 20");
                }
            }
        }else{
            query.append("author_id = '123456789'");
        }

        return query.toString();
    }


    public static void main(String[] args) {
        // 示例数据

        Set<String> authorIds = new HashSet<>();
        authorIds.add("A5071879534");
        authorIds.add("A5012063344");

        // 生成 SQL 查询
        String sqlQuery = buildSQLQuery1(authorIds);
        System.out.println(sqlQuery);

        String url = "jdbc:mysql://116.204.112.5:3306/openalex";
        String username = "root";
        String password = "BjMfWi6CFkrW3556";
        List<String> works_ids = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.print(rs);
            // 处理查询结果
            while (rs.next()) {
                // 读取每一行数据
                works_ids.add(rs.getString("work_id"));
            }


            System.out.print(works_ids);
        } catch (SQLException e) {
            e.printStackTrace();
        }




    }


}
