package com.example.scholar.pojo;


import lombok.Data;

import java.util.List;

@Data
public class AdvancedSearchPojo {
    private List<String> andTitles;
    private List<Boolean> andTitlesFuzzy;
    private List<String> orTitles;
    private List<Boolean> orTitlesFuzzy;
    private List<String> notTitles;
    private List<Boolean> notTitlesFuzzy;

    private List<String> andTopics;
    private List<Boolean> andTopicsFuzzy;
    private List<String> orTopics;
    private List<Boolean> orTopicsFuzzy;
    private List<String> notTopics;
    private List<Boolean> notTopicsFuzzy;

    private List<String> andAuthors;
    private List<Boolean> andAuthorsFuzzy;
    private List<String> orAuthors;
    private List<Boolean> orAuthorsFuzzy;
    private List<String> notAuthors;
    private List<Boolean> notAuthorsFuzzy;

    private List<String> andFirstAuthors;
    private List<Boolean> andFirstAuthorsFuzzy;
    private List<String> orFirstAuthors;
    private List<Boolean> orFirstAuthorsFuzzy;
    private List<String> notFirstAuthors;
    private List<Boolean> notFirstAuthorsFuzzy;

    private List<String> andInstitutions;
    private List<Boolean> andInstitutionsFuzzy;
    private List<String> orInstitutions;
    private List<Boolean> orInstitutionsFuzzy;
    private List<String> notInstitutions;
    private List<Boolean> notInstitutionsFuzzy;

    private List<String> andAbstracts;
    private List<Boolean> andAbstractsFuzzy;
    private List<String> orAbstracts;
    private List<Boolean> orAbstractsFuzzy;
    private List<String> notAbstracts;
    private List<Boolean> notAbstractsFuzzy;

    private List<String> andDOI;
    private List<Boolean> andDOIFuzzy;
    private List<String> orDOI;
    private List<Boolean> orDOIFuzzy;
    private List<String> notDOI;
    private List<Boolean> notDOIFuzzy;

    private java.sql.Date startDate;
    private java.sql.Date endDate;
}
