package com.itheima.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiSuggestion {
    private String mainAdvice;        // 大模型建议
    private String analysisSummary;   // 分析摘要
    private String operationAdvice;   // 操作建议
    private String futureWarning;     // 未来12小时预警
    private String basis;             // 依据
    private String status;            // good, warning, danger
}