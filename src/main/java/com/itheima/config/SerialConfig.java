package com.itheima.config;

import com.fazecast.jSerialComm.SerialPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class SerialConfig {
    @Value("${serial.port:COM3}")
    private String portName;
    @Value("${serial.baud-rate:9600}")
    private int baudRate;

    @Bean
    @Lazy  // 延迟初始化（可选，进一步降低启动时耦合）
    public SerialPort serialPort() {
        try {
            SerialPort port = SerialPort.getCommPort(portName);
            if (port.openPort()) {
                // 配置串口参数
                port.setComPortParameters(baudRate, 8, 1, SerialPort.EVEN_PARITY);
                port.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);
                return port;
            } else {
                // 打开失败时返回null，不抛异常
                System.err.println("串口" + portName + "打开失败（端口不可用）");
                return null;
            }
        } catch (Exception e) {
            System.err.println("串口初始化异常：" + e.getMessage());
            return null;
        }
    }
}


    /*@Bean
    public SerialPort serialPort() {
        // 获取串口对象
        SerialPort port = SerialPort.getCommPort(portName);
        // 配置串口参数（必须与硬件一致）
        if (!port.openPort()) {
            throw new RuntimeException("串口" + portName + "打开失败，请检查端口是否被占用或参数是否正确");
        }
        // 设置：波特率、数据位8、停止位1、偶校验（Even）
        port.setComPortParameters(baudRate, 8, 1, SerialPort.EVEN_PARITY);
        // 禁用流控制
        port.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);
        return port;
    }*/