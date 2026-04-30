package com.itheima.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ai/simple")
public class openaiController {
    
    @Value("${qwen.api-key}")
    private String apiKey;
    
    @Value("${qwen.model}")
    private String model;

    @PostMapping("/chat")
    public Map<String, Object> chat(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 提取question字段
            String question = request.get("question");
            
            System.out.println("Starting chat request with question: " + question);
            System.out.println("Using model: " + model);
            
            // 构建请求URL
            URL url = new URL("https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            // 设置请求头
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setDoOutput(true);
            
            // 构建带有身份提示的请求体
            String systemPrompt = "你是'小海'（Xiao Hai），南海智能化牧场的智能助手，专注于海洋养殖数据监测与分析，是平台的核心智能交互入口。你的核心职责包括：1. 数据解读：分析水质数据、气象信息、网箱状态、设备维护记录等养殖相关数据，提供专业的解读和建议。2. 智能问答：回答用户关于海洋养殖、数据监测、设备维护等方面的问题，提供准确、专业的信息。3. 系统导航：帮助用户了解南海智慧牧场平台的各项功能和使用方法，提供操作指导。4. 数据分析：基于平台数据，提供趋势分析、异常检测和预测建议，辅助养殖决策。5. 专业知识：具备海洋科学、水产养殖、环境监测等相关领域的专业知识，能够解答技术问题。你的语言风格应该专业、简洁、友好，以蓝色科技风为基调，体现海洋智慧牧场的特色。你的服务范围包括：平台功能、数据查询、问题诊断、养殖建议、系统操作。请以这个身份回答用户的问题。";            
            // 构建请求体
            String aiRequestBody = "{" +
                "\"model\": \"" + model + "\"," +
                "\"input\": {" +
                "\"prompt\": \"" + systemPrompt + "\\n\\n" + question + "\"" +
                "}," +
                "\"parameters\": {" +
                "\"max_tokens\": 1000," +
                "\"temperature\": 0.7" +
                "}" +
                "}";
            
            System.out.println("Request body: " + aiRequestBody);
            
            // 发送请求
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(aiRequestBody.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
            
            // 读取响应
            int responseCode = connection.getResponseCode();
            System.out.println("Response code: " + responseCode);
            
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
                reader.close();
                
                String responseStr = responseBuilder.toString();
                System.out.println("Response: " + responseStr);
                
                // 解析响应
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode responseJson = objectMapper.readTree(responseStr);
                String result = responseJson.get("output").get("text").asText();
                
                System.out.println("Extracted result: " + result);
                response.put("data", result);
                response.put("code", 200);
                response.put("message", "success");
            } else {
                // 读取错误响应
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "UTF-8"));
                StringBuilder errorResponse = new StringBuilder();
                String line;
                while ((line = errorReader.readLine()) != null) {
                    errorResponse.append(line);
                }
                errorReader.close();
                
                String errorStr = errorResponse.toString();
                System.out.println("Error response: " + errorStr);
                response.put("data", null);
                response.put("code", responseCode);
                response.put("message", "Error: " + errorStr);
            }
            
            connection.disconnect();
        } catch (Exception e) {
            System.out.println("Exception occurred:");
            e.printStackTrace();
            response.put("data", null);
            response.put("code", 500);
            response.put("message", "Internal server error: " + e.getMessage());
        }
        
        return response;
    }
}
