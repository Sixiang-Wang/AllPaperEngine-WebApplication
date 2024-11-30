package com.example.scholar.domain.openalexElasticsearch;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "openalex_works_index_addingcompletion")
@Data
public class WorksCompletion {

    @Id
    @Field(store = true, index = false, type = FieldType.Text)
    private String id;

    @Field(index = false, store = true, type = FieldType.Text)
    private String doi;


    //    @Field(index=true,analyzer="ik_smart",store=true,searchAnalyzer="ik_smart",type = FieldType.Text)
    @CompletionField
    private String title;

    //    @Field(index=true,analyzer="ik_smart",store=true,searchAnalyzer="ik_smart",type = FieldType.Text)
    @CompletionField
    private String display_name;

    @Field(index = true, analyzer = "ik_smart", store = true, searchAnalyzer = "ik_smart", type = FieldType.Integer)
//    @Field(index = true,  store = true, type = FieldType.Integer)
    private int publication_year;

    @Field(index = true, analyzer = "ik_smart", store = true, searchAnalyzer = "ik_smart", type = FieldType.Text)
//    @Field(index = true, store = true,type = FieldType.Text)
    private String publication_date;

    @Field(index = false, store = true, type = FieldType.Text)
    private String type;

    @Field(index = false, store = true, type = FieldType.Integer)
    private int cited_by_count;

    @Field(index = false, store = true, type = FieldType.Boolean)
    private boolean is_retracted;

    @Field(index = false, store = true, type = FieldType.Boolean)
    private boolean is_paratext;

    @Field(index = false, store = true, type = FieldType.Text)
    private String cited_by_api_url;

    @Field(index = false, store = true, type = FieldType.Text)
    private String abstract_inverted_index;

    @Field(index = false, store = true, type = FieldType.Text)
    private String language;

    @Field(index = false, store = true, type = FieldType.Text)
    private String grants;

    @Field(index = false, store = true, type = FieldType.Text)
    private String keywords;

    //    @Field(index=true,analyzer="ik_smart",store=true,searchAnalyzer="ik_smart",type = FieldType.Text)
    @CompletionField
    private String keywordsText;

    @CompletionField
    private String abstractText;


}
