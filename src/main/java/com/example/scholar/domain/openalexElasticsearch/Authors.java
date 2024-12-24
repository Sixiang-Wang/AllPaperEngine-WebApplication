package com.example.scholar.domain.openalexElasticsearch;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "authors_index")
@Data
public class Authors {
    @Id
    @Field(store = true,index = false,type = FieldType.Text)
    private String id;

    @Field(index=false,store = true,type = FieldType.Text)
    private String orcid;

    @Field(index=true,analyzer="ik_smart",store=true,searchAnalyzer="ik_smart",type = FieldType.Text)
    private String display_name;

    @Field(index=false,store = true,type = FieldType.Text)
    private String display_name_alternatives;


    @Field(index=false,store = true,type = FieldType.Integer)
    private int works_count;

    @Field(index=false,store = true,type = FieldType.Integer)
    private int cited_by_count;

    @Field(index=false,store = true,type = FieldType.Text)
    private String last_known_institution;

    @Field(index=false,store = true,type = FieldType.Text)
    private String works_api_url;

    @Field(index=false,analyzer="ik_smart",store=true,searchAnalyzer="ik_smart",type = FieldType.Text)
    private String updated_date;



}
