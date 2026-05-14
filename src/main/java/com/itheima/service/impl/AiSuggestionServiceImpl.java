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
import java.util.List;
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
        // 获取最新的10条数据
        List<seaData> latestDataList = seaDataMapper.selectLatestTen();
        
        if (latestDataList == null || latestDataList.isEmpty()) {
            return createDefaultSuggestion();
        }

        // 获取最新一条数据
        seaData latestData = latestDataList.get(0);
        
        // 计算10条数据的平均值
        seaData avgData = calculateAverage(latestDataList);

        String prompt = buildPrompt(latestData, avgData);
        String aiResponse = callAI(prompt);

        if (aiResponse != null && !aiResponse.isEmpty()) {
            return parseResponse(aiResponse, avgData);
        }
        
        return createFallbackSuggestion(latestData, avgData);
    }

    private seaData calculateAverage(List<seaData> dataList) {
        seaData avgData = new seaData();
        int count = dataList.size();
        
        double sumO = 0, sumNh = 0, sumNo = 0, sumTemp = 0, sumSalinity = 0;
        double sumTurbidity = 0, sumPh = 0, sumChl = 0, sumCurrVel = 0;
        int sumOrp = 0;
        
        for (seaData data : dataList) {
            sumO += data.getO();
            sumNh += data.getNh();
            sumNo += data.getNo();
            sumTemp += data.getTemp();
            sumSalinity += data.getSalinity();
            sumTurbidity += data.getTurbidity();
            sumPh += data.getPh();
            sumChl += data.getChl();
            sumCurrVel += data.getCurrVel();
            if (data.getOrp() != null) {
                sumOrp += data.getOrp();
            }
        }
        
        avgData.setO(Math.round(sumO / count * 100.0) / 100.0);
        avgData.setNh(Math.round(sumNh / count * 100.0) / 100.0);
        avgData.setNo(Math.round(sumNo / count * 100.0) / 100.0);
        avgData.setTemp(Math.round(sumTemp / count * 100.0) / 100.0);
        avgData.setSalinity(Math.round(sumSalinity / count * 100.0) / 100.0);
        avgData.setTurbidity(Math.round(sumTurbidity / count * 100.0) / 100.0);
        avgData.setPh(Math.round(sumPh / count * 100.0) / 100.0);
        avgData.setChl(Math.round(sumChl / count * 100.0) / 100.0);
        avgData.setCurrVel(Math.round(sumCurrVel / count * 100.0) / 100.0);
        avgData.setOrp(sumOrp / count);
        
        return avgData;
    }

    private String buildPrompt(seaData latestData, seaData avgData) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("请根据以下金鲳鱼养殖水质数据进行综合研判：\n\n");
        
        prompt.append("【最新实时数据】\n");
        prompt.append("温度: ").append(latestData.getTemp()).append("℃\n");
        prompt.append("浊度: ").append(latestData.getTurbidity()).append(" NTU\n");
        prompt.append("氨氮: ").append(latestData.getNh()).append(" mg/L\n");
        prompt.append("亚硝酸氮: ").append(latestData.getNo()).append(" mg/L\n");
        prompt.append("溶解氧: ").append(latestData.getO()).append(" mg/L\n");
        prompt.append("pH: ").append(latestData.getPh()).append("\n");
        prompt.append("盐度: ").append(latestData.getSalinity()).append(" PSU\n");
        prompt.append("叶绿素a: ").append(latestData.getChl()).append(" μg/L\n");
        prompt.append("洋流流速: ").append(latestData.getCurrVel()).append(" cm/s\n");
        prompt.append("氧化还原电位: ").append(latestData.getOrp()).append(" mv\n\n");
        
        prompt.append("【最近10条数据平均值】\n");
        prompt.append("温度: ").append(avgData.getTemp()).append("℃\n");
        prompt.append("浊度: ").append(avgData.getTurbidity()).append(" NTU\n");
        prompt.append("氨氮: ").append(avgData.getNh()).append(" mg/L\n");
        prompt.append("亚硝酸氮: ").append(avgData.getNo()).append(" mg/L\n");
        prompt.append("溶解氧: ").append(avgData.getO()).append(" mg/L\n");
        prompt.append("pH: ").append(avgData.getPh()).append("\n");
        prompt.append("盐度: ").append(avgData.getSalinity()).append(" PSU\n");
        prompt.append("叶绿素a: ").append(avgData.getChl()).append(" μg/L\n");
        prompt.append("洋流流速: ").append(avgData.getCurrVel()).append(" cm/s\n");
        prompt.append("氧化还原电位: ").append(avgData.getOrp()).append(" mv\n\n");
        
        prompt.append("【参考标准】\n");
        prompt.append("温度: 正常25-30°C，预警24-25/30-31°C，危险<24/>31°C\n");
        prompt.append("浊度: 正常0-10 NTU，预警10-11 NTU，危险>11 NTU\n");
        prompt.append("氨氮: 正常0-0.2 mg/L，预警0.2-0.22 mg/L，危险>0.22 mg/L\n");
        prompt.append("亚硝酸氮: 正常0-0.1 mg/L，预警0.1-0.3 mg/L，危险0.3-0.5 mg/L\n");
        prompt.append("溶解氧: 正常6.5-8 mg/L，预警5.85-6.5/8-8.8 mg/L，危险<5.85/>8.8 mg/L\n");
        prompt.append("pH: 正常6.5-9.0，预警6.5-7.5/8.5-9.0，危险<6.5/>9.0\n");
        prompt.append("盐度: 正常20-30 PSU，预警15-20/30-35 PSU，危险10-15/35-40 PSU\n");
        prompt.append("叶绿素a: 正常5-20 μg/L，预警0-5/20-50 μg/L，危险50-100 μg/L\n");
        prompt.append("洋流流速: 正常5-20 cm/s，预警0-5/20-30 cm/s，危险30-50 cm/s\n");
        prompt.append("氧化还原电位: 正常300-500 mv，预警200-300/500-600 mv，危险100-200/600-700 mv\n\n");
        
        prompt.append("请结合最新实时数据和平均值进行综合分析，给出研判结果：\n");
        prompt.append("1. 整体建议\n");
        prompt.append("2. 分析摘要\n");
        prompt.append("3. 操作建议\n");
        prompt.append("4. 未来12小时预警\n");
        prompt.append("5. 研判依据\n");
        prompt.append("6. 状态（good/warning/danger）");
        
        return prompt.toString();
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

    private AiSuggestion createFallbackSuggestion(seaData latestData, seaData avgData) {
        AiSuggestion suggestion = new AiSuggestion();
        String status = "good";
        StringBuilder basis = new StringBuilder();
        
        // 综合判断：同时检查最新数据和平均值
        // 如果任一指标在危险范围，整体状态为danger
        // 如果任一指标在预警范围（无危险），整体状态为warning
        
        // 温度
        if (latestData.getTemp() < 24 || latestData.getTemp() > 31 || avgData.getTemp() < 24 || avgData.getTemp() > 31) {
            status = "danger";
            basis.append("温度(").append(latestData.getTemp()).append("℃/平均").append(avgData.getTemp()).append("℃)超出正常范围;");
        } else if ((latestData.getTemp() >= 24 && latestData.getTemp() < 25) || (latestData.getTemp() > 30 && latestData.getTemp() <= 31) ||
                   (avgData.getTemp() >= 24 && avgData.getTemp() < 25) || (avgData.getTemp() > 30 && avgData.getTemp() <= 31)) {
            if (!status.equals("danger")) status = "warning";
            basis.append("温度接近临界值;");
        } else {
            basis.append("温度正常;");
        }

        // 浊度
        if (latestData.getTurbidity() > 11 || avgData.getTurbidity() > 11) {
            status = "danger";
            basis.append("浊度(").append(latestData.getTurbidity()).append("/平均").append(avgData.getTurbidity()).append("NTU)超出正常范围;");
        } else if ((latestData.getTurbidity() > 10 && latestData.getTurbidity() <= 11) || 
                   (avgData.getTurbidity() > 10 && avgData.getTurbidity() <= 11)) {
            if (!status.equals("danger")) status = "warning";
            basis.append("浊度接近临界值;");
        } else {
            basis.append("浊度正常;");
        }

        // 氨氮
        if (latestData.getNh() > 0.22 || avgData.getNh() > 0.22) {
            status = "danger";
            basis.append("氨氮(").append(latestData.getNh()).append("/平均").append(avgData.getNh()).append("mg/L)超出正常范围;");
        } else if ((latestData.getNh() > 0.2 && latestData.getNh() <= 0.22) || 
                   (avgData.getNh() > 0.2 && avgData.getNh() <= 0.22)) {
            if (!status.equals("danger")) status = "warning";
            basis.append("氨氮接近临界值;");
        } else {
            basis.append("氨氮正常;");
        }

        // 亚硝酸氮
        if ((latestData.getNo() > 0.3 && latestData.getNo() <= 0.5) || (avgData.getNo() > 0.3 && avgData.getNo() <= 0.5)) {
            status = "danger";
            basis.append("亚硝酸氮(").append(latestData.getNo()).append("/平均").append(avgData.getNo()).append("mg/L)超出正常范围;");
        } else if ((latestData.getNo() > 0.1 && latestData.getNo() <= 0.3) || 
                   (avgData.getNo() > 0.1 && avgData.getNo() <= 0.3)) {
            if (!status.equals("danger")) status = "warning";
            basis.append("亚硝酸氮接近临界值;");
        } else {
            basis.append("亚硝酸氮正常;");
        }

        // 溶解氧
        if ((latestData.getO() < 5.85 || latestData.getO() > 8.8) || (avgData.getO() < 5.85 || avgData.getO() > 8.8)) {
            status = "danger";
            basis.append("溶解氧(").append(latestData.getO()).append("/平均").append(avgData.getO()).append("mg/L)超出正常范围;");
        } else if (((latestData.getO() >= 5.85 && latestData.getO() < 6.5) || (latestData.getO() > 8 && latestData.getO() <= 8.8)) ||
                   ((avgData.getO() >= 5.85 && avgData.getO() < 6.5) || (avgData.getO() > 8 && avgData.getO() <= 8.8))) {
            if (!status.equals("danger")) status = "warning";
            basis.append("溶解氧接近临界值;");
        } else {
            basis.append("溶解氧正常;");
        }

        // pH
        if ((latestData.getPh() < 6.5 || latestData.getPh() > 9.0) || (avgData.getPh() < 6.5 || avgData.getPh() > 9.0)) {
            status = "danger";
            basis.append("pH(").append(latestData.getPh()).append("/平均").append(avgData.getPh()).append(")超出正常范围;");
        } else if (((latestData.getPh() >= 6.5 && latestData.getPh() < 7.5) || (latestData.getPh() > 8.5 && latestData.getPh() <= 9.0)) ||
                   ((avgData.getPh() >= 6.5 && avgData.getPh() < 7.5) || (avgData.getPh() > 8.5 && avgData.getPh() <= 9.0))) {
            if (!status.equals("danger")) status = "warning";
            basis.append("pH接近临界值;");
        } else {
            basis.append("pH正常;");
        }

        // 盐度
        if (((latestData.getSalinity() >= 10 && latestData.getSalinity() < 15) || (latestData.getSalinity() > 35 && latestData.getSalinity() <= 40)) ||
            ((avgData.getSalinity() >= 10 && avgData.getSalinity() < 15) || (avgData.getSalinity() > 35 && avgData.getSalinity() <= 40))) {
            status = "danger";
            basis.append("盐度(").append(latestData.getSalinity()).append("/平均").append(avgData.getSalinity()).append("PSU)超出正常范围;");
        } else if (((latestData.getSalinity() >= 15 && latestData.getSalinity() < 20) || (latestData.getSalinity() > 30 && latestData.getSalinity() <= 35)) ||
                   ((avgData.getSalinity() >= 15 && avgData.getSalinity() < 20) || (avgData.getSalinity() > 30 && avgData.getSalinity() <= 35))) {
            if (!status.equals("danger")) status = "warning";
            basis.append("盐度接近临界值;");
        } else {
            basis.append("盐度正常;");
        }

        // 叶绿素a
        if ((latestData.getChl() > 50 && latestData.getChl() <= 100) || (avgData.getChl() > 50 && avgData.getChl() <= 100)) {
            status = "danger";
            basis.append("叶绿素a(").append(latestData.getChl()).append("/平均").append(avgData.getChl()).append("μg/L)超出正常范围;");
        } else if (((latestData.getChl() >= 0 && latestData.getChl() < 5) || (latestData.getChl() > 20 && latestData.getChl() <= 50)) ||
                   ((avgData.getChl() >= 0 && avgData.getChl() < 5) || (avgData.getChl() > 20 && avgData.getChl() <= 50))) {
            if (!status.equals("danger")) status = "warning";
            basis.append("叶绿素a接近临界值;");
        } else {
            basis.append("叶绿素a正常;");
        }

        // 洋流流速
        if ((latestData.getCurrVel() > 30 && latestData.getCurrVel() <= 50) || (avgData.getCurrVel() > 30 && avgData.getCurrVel() <= 50)) {
            status = "danger";
            basis.append("洋流流速(").append(latestData.getCurrVel()).append("/平均").append(avgData.getCurrVel()).append("cm/s)超出正常范围;");
        } else if (((latestData.getCurrVel() >= 0 && latestData.getCurrVel() < 5) || (latestData.getCurrVel() > 20 && latestData.getCurrVel() <= 30)) ||
                   ((avgData.getCurrVel() >= 0 && avgData.getCurrVel() < 5) || (avgData.getCurrVel() > 20 && avgData.getCurrVel() <= 30))) {
            if (!status.equals("danger")) status = "warning";
            basis.append("洋流流速接近临界值;");
        } else {
            basis.append("洋流流速正常;");
        }

        // 氧化还原电位
        if (((latestData.getOrp() >= 100 && latestData.getOrp() < 200) || (latestData.getOrp() > 600 && latestData.getOrp() <= 700)) ||
            ((avgData.getOrp() >= 100 && avgData.getOrp() < 200) || (avgData.getOrp() > 600 && avgData.getOrp() <= 700))) {
            status = "danger";
            basis.append("氧化还原电位(").append(latestData.getOrp()).append("/平均").append(avgData.getOrp()).append("mv)超出正常范围;");
        } else if (((latestData.getOrp() >= 200 && latestData.getOrp() < 300) || (latestData.getOrp() > 500 && latestData.getOrp() <= 600)) ||
                   ((avgData.getOrp() >= 200 && avgData.getOrp() < 300) || (avgData.getOrp() > 500 && avgData.getOrp() <= 600))) {
            if (!status.equals("danger")) status = "warning";
            basis.append("氧化还原电位接近临界值;");
        } else {
            basis.append("氧化还原电位正常;");
        }

        suggestion.setBasis(basis.toString());
        suggestion.setStatus(status);

        if ("good".equals(status)) {
            suggestion.setMainAdvice("基于最新实时数据和平均值的综合分析，当前养殖环境状态良好，适合金鲳鱼生存。");
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
