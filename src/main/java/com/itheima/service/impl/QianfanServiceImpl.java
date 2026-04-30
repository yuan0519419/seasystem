package com.itheima.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class QianfanServiceImpl {
    
    @Value("${qwen.api-key}")
    private String apiKey;
    
    @Value("${qwen.model}")
    private String model;
    
    public String chatCompletion(String prompt) {
        try {
            // 构建请求URL
            URL url = new URL("https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            // 设置请求头
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setDoOutput(true);
            
            // 构建请求体
            String requestBody = "{" +
                "\"model\": \"" + model + "\"," +
                "\"input\": {" +
                "\"prompt\": \"" + prompt + "\"" +
                "}," +
                "\"parameters\": {" +
                "\"max_tokens\": 1000," +
                "\"temperature\": 0.7" +
                "}" +
                "}";
            
            // 发送请求
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(requestBody.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
            
            // 读取响应
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                
                // 解析响应
                String result = response.toString();
                // 简单处理，实际应该使用JSON解析库
                if (result.contains("\"text\":\"")) {
                    int start = result.indexOf("\"text\":\"") + 8;
                    int end = result.indexOf("\"", start);
                    if (end > start) {
                        result = result.substring(start, end);
                    }
                }
                
                connection.disconnect();
                return result;
            } else {
                connection.disconnect();
                return "Error: " + responseCode;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}