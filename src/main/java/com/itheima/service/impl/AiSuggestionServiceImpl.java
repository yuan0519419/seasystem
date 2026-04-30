package com.itheima.service.impl;

import com.itheima.mapper.seaDataMapper;
import com.itheima.pojo.AiSuggestion;
import com.itheima.pojo.sea.seaData;
import com.itheima.service.AiSuggestionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
public class AiSuggestionServiceImpl implements AiSuggestionService {

    private final seaDataMapper seaDataMapper;

    @Value("${qwen.api-key}")
    private String apiKey;

    @Value("${qwen.model}")
    private String model;

    public AiSuggestionServiceImpl(seaDataMapper seaDataMapper) {
        this.seaDataMapper = seaDataMapper;
    }

    @Override
    public AiSuggestion getSuggestion() {
        seaData latestData = seaDataMapper.selectLatest();
        
        if (latestData == null) {
            return createDefaultSuggestion();
        }

        String prompt = buildPrompt(latestData);
        String aiResponse = callAI(prompt);

        if (aiResponse != null && !aiResponse.isEmpty()) {
            return parseResponse(aiResponse, latestData);
        }
        
        return createFallbackSuggestion(latestData);
    }

    private String buildPrompt(seaData data) {
        return "请分析以下金鲳鱼养殖水质数据，给出综合研判：\n" +
                "温度: " + data.getTemp() + "℃ (适宜:25-30℃)\n" +
                "浊度: " + data.getTurbidity() + " NTU (适宜:0-10 NTU)\n" +
                "氨氮: " + data.getNh() + " mg/L (适宜:0-0.2 mg/L)\n" +
                "溶解氧: " + data.getO() + " mg/L (适宜:6.5-8 mg/L)\n" +
                "pH: " + data.getPh() + " (适宜:6.5-8.5)\n" +
                "\n" +
                "请用中文简要回答：\n" +
                "1. 整体建议\n" +
                "2. 分析摘要\n" +
                "3. 操作建议\n" +
                "4. 未来12小时预警\n" +
                "5. 研判依据\n" +
                "6. 状态（good/warning/danger）";
    }

    private String callAI(String prompt) {
        try {
            URL url = new URL("https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setDoOutput(true);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            
            Map<String, Object> input = new HashMap<>();
            input.put("prompt", prompt);
            requestBody.put("input", input);
            
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("max_tokens", 2000);
            parameters.put("temperature", 0.3);
            requestBody.put("parameters", parameters);

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody = objectMapper.writeValueAsString(requestBody);

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(jsonBody.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();

            int responseCode = connection.getResponseCode();
            BufferedReader reader;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "UTF-8"));
            }

            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }
            reader.close();
            connection.disconnect();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String responseStr = responseBuilder.toString();
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    return mapper.readTree(responseStr).get("output").get("text").asText().trim();
                } catch (Exception e) {
                    return responseStr;
                }
            } else {
                System.out.println("AI API Error: " + responseBuilder.toString());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private AiSuggestion parseResponse(String aiResponse, seaData data) {
        AiSuggestion suggestion = new AiSuggestion();
        
        String mainAdvice = extractContent(aiResponse, "整体建议");
        String analysisSummary = extractContent(aiResponse, "分析摘要");
        String operationAdvice = extractContent(aiResponse, "操作建议");
        String futureWarning = extractContent(aiResponse, "未来12小时预警");
        String basis = extractContent(aiResponse, "研判依据");
        String statusStr = extractContent(aiResponse, "状态");
        String status = statusStr.toLowerCase().contains("good") ? "good" : 
                 statusStr.toLowerCase().contains("warning") ? "warning" : 
                 statusStr.toLowerCase().contains("danger") ? "danger" : "good";
        
        suggestion.setMainAdvice(mainAdvice.isEmpty() ? "基于AI分析，当前水质环境状态良好。" : mainAdvice);
        suggestion.setAnalysisSummary(analysisSummary.isEmpty() ? "各项水质指标分析正常" : analysisSummary);
        suggestion.setOperationAdvice(operationAdvice.isEmpty() ? "维持当前养殖计划" : operationAdvice);
        suggestion.setFutureWarning(futureWarning.isEmpty() ? "未来12小时无明显风险" : futureWarning);
        suggestion.setBasis(basis.isEmpty() ? aiResponse : basis);
        suggestion.setStatus(status);
        
        return suggestion;
    }
    
    private String extractContent(String text, String fieldName) {
        int startIndex = text.indexOf(fieldName);
        if (startIndex == -1) {
            startIndex = text.indexOf("**" + fieldName + "**");
            if (startIndex == -1) return "";
        }
        
        int contentStart = text.indexOf("：", startIndex);
        if (contentStart == -1) {
            contentStart = text.indexOf(":", startIndex);
            if (contentStart == -1) return "";
        }
        contentStart += 1;
        
        int nextSection = text.indexOf("\n\n", contentStart);
        if (nextSection == -1) {
            nextSection = text.length();
        }
        
        String content = text.substring(contentStart, nextSection).trim();
        content = content.replaceAll("^[-*\\s]+", "").replaceAll("[-*\\s]+$", "");
        return content;
    }

    private AiSuggestion createDefaultSuggestion() {
        AiSuggestion suggestion = new AiSuggestion();
        suggestion.setMainAdvice("暂无水质数据，请确保监测设备正常运行。");
        suggestion.setAnalysisSummary("无法获取最新水质数据");
        suggestion.setOperationAdvice("检查水质监测设备连接状态");
        suggestion.setFutureWarning("无数据可用，建议尽快排查设备问题");
        suggestion.setBasis("无数据");
        suggestion.setStatus("warning");
        return suggestion;
    }

    private AiSuggestion createFallbackSuggestion(seaData data) {
        AiSuggestion suggestion = new AiSuggestion();
        String status = "good";
        StringBuilder basis = new StringBuilder();

        if (data.getTemp() < 25 || data.getTemp() > 30) {
            status = "danger";
            basis.append("温度").append(data.getTemp()).append("℃超出正常范围;");
        } else if (data.getTemp() >= 24 && data.getTemp() < 25 || data.getTemp() > 30 && data.getTemp() <= 31) {
            if (!status.equals("danger")) status = "warning";
            basis.append("温度接近临界值;");
        } else {
            basis.append("温度正常;");
        }

        if (data.getTurbidity() > 11) {
            status = "danger";
            basis.append("浊度").append(data.getTurbidity()).append("NTU超出正常范围;");
        } else if (data.getTurbidity() > 10 && data.getTurbidity() <= 11) {
            if (!status.equals("danger")) status = "warning";
            basis.append("浊度接近临界值;");
        } else {
            basis.append("浊度正常;");
        }

        if (data.getNh() > 0.22) {
            status = "danger";
            basis.append("氨氮").append(data.getNh()).append("mg/L超出正常范围;");
        } else if (data.getNh() > 0.2 && data.getNh() <= 0.22) {
            if (!status.equals("danger")) status = "warning";
            basis.append("氨氮接近临界值;");
        } else {
            basis.append("氨氮正常;");
        }

        if (data.getO() < 5.85 || data.getO() > 8.8) {
            status = "danger";
            basis.append("溶解氧").append(data.getO()).append("mg/L超出正常范围;");
        } else if ((data.getO() >= 5.85 && data.getO() < 6.5) || (data.getO() > 8 && data.getO() <= 8.8)) {
            if (!status.equals("danger")) status = "warning";
            basis.append("溶解氧接近临界值;");
        } else {
            basis.append("溶解氧正常;");
        }

        if (data.getPh() < 5.85 || data.getPh() > 9.35) {
            status = "danger";
            basis.append("pH").append(data.getPh()).append("超出正常范围;");
        } else if ((data.getPh() >= 5.85 && data.getPh() < 6.5) || (data.getPh() > 8.5 && data.getPh() <= 9.35)) {
            if (!status.equals("danger")) status = "warning";
            basis.append("pH接近临界值;");
        } else {
            basis.append("pH正常;");
        }

        suggestion.setBasis(basis.toString());
        suggestion.setStatus(status);

        if ("good".equals(status)) {
            suggestion.setMainAdvice("基于水质数据，当前养殖环境状态良好，适合金鲳鱼生存。");
            suggestion.setAnalysisSummary("各项水质指标均在适宜范围内");
            suggestion.setOperationAdvice("维持当前养殖计划和日常运维");
            suggestion.setFutureWarning("当前水质稳定，继续保持监测频率即可");
        } else if ("warning".equals(status)) {
            suggestion.setMainAdvice("部分水质指标接近临界值，建议加强监测。");
            suggestion.setAnalysisSummary("部分指标接近警戒范围");
            suggestion.setOperationAdvice("增加监测频率，准备应对可能的水质变化");
            suggestion.setFutureWarning("建议未来12小时内增加监测次数");
        } else {
            suggestion.setMainAdvice("检测到危险水质指标，需要立即采取措施！");
            suggestion.setAnalysisSummary("关键水质指标超出安全范围");
            suggestion.setOperationAdvice("立即检查水源和增氧设备");
            suggestion.setFutureWarning("如果水质继续恶化，将严重影响鱼群健康");
        }

        return suggestion;
    }
}