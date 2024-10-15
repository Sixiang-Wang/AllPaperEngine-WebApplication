package com.example.scholar.util;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.*;

public class AuthorNameRestore {

    public static ArrayList<String> restoreAuthorName(String jsonString){
        try {
            // 使用 Gson 解析 JSON
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<String>>(){}.getType();
            ArrayList<String> arrayList = gson.fromJson(jsonString, listType);
            return arrayList;
        }catch (Exception e){
            return null;
        }
    }
    public static void main(String[] args) {
        String jsonString = "[\"Jan Vítek\", \"J. Vitek\", \"Jan Vitek\"]";
        ArrayList<String> str = restoreAuthorName(jsonString);

        // 输出复原的文本
        System.out.println(str); // 去掉末尾空格
    }
}
