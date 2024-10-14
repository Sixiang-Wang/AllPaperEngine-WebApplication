package com.example.scholar.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.*;

public class AbstractRestore {
    public static void main(String[] args) {
        String jsonString = "{\"a\": [9, 17, 53, 57, 66], \"It\": [15, 55], \"an\": [3, 12, 28, 35, 79], \"be\": [41, 83], \"by\": [93], \"if\": [27], \"in\": [85], \"is\": [25, 33, 39, 47], \"of\": [52, 59, 65, 75, 78, 98], \"to\": [40], \"The\": [0, 43], \"and\": [69, 96, 105], \"are\": [91, 97], \"for\": [20, 49], \"set\": [58], \"the\": [76, 86, 94], \"They\": [90], \"This\": [22], \"also\": [34], \"each\": [50], \"form\": [19, 24], \"into\": [11], \"part\": [74], \"that\": [81], \"uses\": [56], \"will\": [82], \"These\": [71], \"class\": [10, 51, 67], \"paper\": [1], \"three\": [99], \"used.\": [42], \"which\": [7], \"(which\": [32], \"graph.\": [89], \"kinds:\": [100], \"model.\": [54], \"mutual\": [106], \"normal\": [18, 23], \"splits\": [8], \"applied\": [48], \"between\": [63], \"defined\": [62], \"defines\": [16], \"(methods\": [68], \"classes.\": [21], \"designer\": [95], \"existant\": [29], \"presents\": [2], \"proposed\": [44], \"required\": [26], \"supplied\": [92], \"exclusive\": [101], \"expressed\": [84], \"generated\": [87], \"mechanism\": [6, 31, 46], \"semantics\": [77], \"splitting\": [45], \"translate\": [73], \"hierarchy.\": [14], \"mechanism)\": [38], \"application\": [80], \"conditioned\": [103], \"constraints\": [61, 72], \"inheritance\": [13, 88], \"attributes).\": [70], \"constraints.\": [108], \"applicability\": [60, 104, 107], \"factorization\": [30], \"normalization\": [5, 37], \"applicability,\": [102], \"characteristics\": [64], \"object-oriented\": [4, 36]}";
        // 使用 Gson 解析 JSON
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);

        // 存储所有单词及其索引位置
        Map<Integer, String> indexMap = new HashMap<>();

        // 遍历 JSON 对象
        for (Map.Entry<String, ?> entry : jsonObject.entrySet()) {
            String word = entry.getKey();
            JsonArray positionsArray = (JsonArray) entry.getValue(); // 直接获取 JsonArray

            // 遍历 JsonArray，获取每个位置
            for (int i = 0; i < positionsArray.size(); i++) {
                int position = positionsArray.get(i).getAsInt(); // 获取整型值
                indexMap.put(position, word); // 将单词放入相应的索引位置
            }
        }

        // 按索引位置排序
        List<Integer> sortedKeys = new ArrayList<>(indexMap.keySet());
        Collections.sort(sortedKeys);

        // 生成最终的文本
        StringBuilder restoredAbstract = new StringBuilder();
        for (Integer index : sortedKeys) {
            restoredAbstract.append(indexMap.get(index)).append(" "); // 在每个单词后添加空格
        }

        // 输出复原的文本
        System.out.println(restoredAbstract.toString().trim()); // 去掉末尾空格
    }
}
