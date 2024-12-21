package com.example.scholar.util;

import java.sql.*;
import java.util.*;

public class TopicAnalyzer {

    private static TopicAnalyzer instance;
    private static Map<String, Domain> analysisResult;

    // Private constructor to prevent instantiation
    private TopicAnalyzer() {
        // Perform the analysis once during instantiation
        analysisResult = analyzeTopicsFromDatabase("jdbc:mysql://116.204.112.5:3306/scholar", "root", "BjMfWi6CFkrW3556");
    }

    // Public method to provide access to the singleton instance
    public static synchronized TopicAnalyzer getInstance() {
        if (instance == null) {
            instance = new TopicAnalyzer();
        }
        return instance;
    }

    // Method to get the analysis result
    public Map<String, Domain> getAnalysisResult() {
        return analysisResult;
    }

    private static Map<String, Domain> analyzeTopicsFromDatabase(String dbUrl, String user, String password) {
        Map<String, Domain> result = new HashMap<>();

        try (Connection connection = DriverManager.getConnection(dbUrl, user, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT domain_display_name, field_display_name, subfield_display_name, display_name, works_count FROM openalex_topics")) {

            while (resultSet.next()) {
                String domainName = resultSet.getString("domain_display_name");
                String fieldName = resultSet.getString("field_display_name");
                String subfieldName = resultSet.getString("subfield_display_name");
                String topicName = resultSet.getString("display_name");
                int worksCount = resultSet.getInt("works_count");

                // Get or create Domain
                Domain domain = result.computeIfAbsent(domainName, Domain::new);
                domain.worksCount += worksCount;

                // Get or create Field
                Field field = domain.fields.computeIfAbsent(fieldName, Field::new);
                field.worksCount += worksCount;

                // Get or create Subfield
                Subfield subfield = field.subfields.computeIfAbsent(subfieldName, Subfield::new);
                subfield.worksCount += worksCount;

                // Add Topic
                subfield.topics.put(topicName, new Topic(topicName, worksCount));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    // Nested classes for Domain, Field, Subfield, and Topic
    public static class Topic {
        public String name;
        public int worksCount;

        Topic(String name, int worksCount) {
            this.name = name;
            this.worksCount = worksCount;
        }
    }

    public static class Subfield {
        String name;
        int worksCount;
        public Map<String, Topic> topics = new HashMap<>();

        Subfield(String name) {
            this.name = name;
            this.worksCount = 0;
        }
    }

    public static class Field {
        String name;
        int worksCount;
        public Map<String, Subfield> subfields = new HashMap<>();

        Field(String name) {
            this.name = name;
            this.worksCount = 0;
        }
    }

    public static class Domain {
        String name;
        int worksCount;
        public Map<String, Field> fields = new HashMap<>();

        Domain(String name) {
            this.name = name;
            this.worksCount = 0;
        }
    }
}