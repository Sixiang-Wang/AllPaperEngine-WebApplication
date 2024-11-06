package com.example.scholar.util;

import com.example.scholar.dto.InstitutionsResultDto;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.*;

public class JsonDisposer {
    public static Map<String, Float> disposeWorkKeywords(String jsonString) {
        // 使用 Gson 解析 JSON 字符串
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Keyword>>(){}.getType();
        List<Keyword> keywords = gson.fromJson(jsonString, listType);

        // 初始化结果 Map
        Map<String, Float> resultMap = new HashMap<>();

        // 遍历列表并提取 display_name 和 score
        for (Keyword keyword : keywords) {
            resultMap.put(keyword.display_name, keyword.score);
        }

        return resultMap;
    }


    public static String getKeywords(Map<String, Float> map){
        StringBuilder stringBuilder = new StringBuilder();
        for(Map.Entry<String,Float> entry:map.entrySet()){
            stringBuilder.append(entry.getKey()+",");
        }
        if(stringBuilder.isEmpty()){
            return "";
        }else{
            stringBuilder.delete(stringBuilder.length()-1,stringBuilder.length());
            return stringBuilder.toString();
        }

    }

    // 定义 Keyword 类来映射 JSON 对象中的字段
    private static class Keyword {
        String id;
        float score;
        String display_name;

        @Override
        public String toString() {
            return "Keyword{id='" + id + "', score=" + score + ", display_name='" + display_name + "'}";
        }
    }



    public static Map<String, InstitutionsResultDto> disposeInstitutions(String jsonString) {
        //
        // 使用 Gson 解析 JSON 字符串
        Gson gson = new Gson();
        Type listType = new TypeToken<List<InstitutionJson>>(){}.getType();
        List<InstitutionJson> institutionsParts = gson.fromJson(jsonString, listType);

        // 初始化结果 Map
        Map<String, InstitutionsResultDto> resultMap = new HashMap<>();

        // 遍历列表并提取 display_name 和 score
        for (InstitutionJson institutionJson:institutionsParts) {
            InstitutionsResultDto institutionsResultDto = new InstitutionsResultDto();
            institutionsResultDto.setId(institutionJson.id);
            institutionsResultDto.setRor(institutionJson.ror);
            institutionsResultDto.setType(institutionJson.type);
            institutionsResultDto.setLineage(institutionJson.lineage);
            institutionsResultDto.setDisplay_name(institutionJson.display_name);
            institutionsResultDto.setCountry_code(institutionJson.country_code);
            resultMap.put(institutionJson.id,institutionsResultDto);
        }
        return resultMap;
    }


    // 定义 Keyword 类来映射 JSON 对象中的字段
    private static class InstitutionJson {
        String id;
        String ror;
        String type;
        List<String> lineage;
        String country_code;
        String display_name;
    }

//[{"id": "https://openalex.org/I180670191", "ror": "https://ror.org/043mz5j54", "type": "education", "lineage": ["https://openalex.org/I180670191"], "country_code": "US", "display_name": "University of California, San Francisco"}]
    public static void main(String[] args) {
//        String jsonString = "[{\"id\": \"https://openalex.org/keywords/mmp1\", \"score\": 0.74122393, \"display_name\": \"MMP1\"}, {\"id\": \"https://openalex.org/keywords/interleukin-1-polymorphisms\", \"score\": 0.466912, \"display_name\": \"Interleukin-1 Polymorphisms\"}]";
//        Map<String, Float> result = JsonDisposer.disposeWorkKeywords(jsonString);
        String jsonString = "[{\"id\": \"https://openalex.org/I205349734\", \"ror\": \"https://ror.org/02e16g702\", \"type\": \"education\", \"lineage\": [\"https://openalex.org/I205349734\"], \"country_code\": \"JP\", \"display_name\": \"Hokkaido University\"}]";
        Map<String, InstitutionsResultDto> result = JsonDisposer.disposeInstitutions(jsonString);


        System.out.println(result);
    }




}
