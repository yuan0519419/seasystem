package com.itheima.service.impl;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.itheima.pojo.FishTag;
import com.itheima.service.FishTagService;
import com.itheima.service.SerialReceiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class SerialReceiveServiceImpl implements SerialReceiveService {
    @Autowired(required = false)
    private SerialPort serialPort;
    @Autowired
    private FishTagService fishTagService;
    private FishTag latestTag;
    @Override
    public void startListening() {
        // 关键修改2：先校验串口是否可用
        if (serialPort == null || !serialPort.isOpen()) {
            System.err.println("串口未初始化或不可用，无法启动监听！");
            return; // 串口不可用时直接返回，避免后续操作
        }

        serialPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                // 额外保险：再次校验串口状态（防止运行中串口断开）
                if (serialPort == null || !serialPort.isOpen()) {
                    System.err.println("串口已断开，停止数据监听！");
                    return;
                }

                if (event.getEventType() == SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
                    byte[] buffer = new byte[serialPort.bytesAvailable()];
                    int bytesRead = serialPort.readBytes(buffer, buffer.length);
                    if (bytesRead > 0) {
                        System.out.println("收到原始数据（十六进制）：" + HexFormat.of().formatHex(buffer));
                        latestTag = parseHdxData(buffer);
                        if (latestTag != null) {
                            fishTagService.saveFishTag(latestTag);
                        }
                    }
                }
            }
        });
    }

    @Override
    public FishTag parseHdxData(byte[] data) {
        FishTag hdxData = new FishTag();

        if (data.length >= 11) {
            byte[] cardNoBytes = Arrays.copyOfRange(data, 7, 11);
            if (cardNoBytes.length == 4) {
                long cardNoDecimal = ((long) (cardNoBytes[0] & 0xFF) << 24)
                        | ((long) (cardNoBytes[1] & 0xFF) << 16)
                        | ((long) (cardNoBytes[2] & 0xFF) << 8)
                        | (cardNoBytes[3] & 0xFF);
                hdxData.setCardNo(cardNoDecimal);
                int randomReefNo = ThreadLocalRandom.current().nextInt(2) + 1;
                hdxData.setReefNo(randomReefNo);
                hdxData.setReceiveTime(LocalDateTime.now());
            } else {
                hdxData = null;
            }
        } else {
            hdxData = null;
        }

        return hdxData;
    }

    @Override
    public FishTag getLatestHdxData() {
        return latestTag;
    }
}



/*


    @Override
    public void startListening() {

        serialPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                // 只监听“数据可用”事件
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() == SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
                    // 读取原始字节数据
                    byte[] buffer = new byte[serialPort.bytesAvailable()];
                    int bytesRead = serialPort.readBytes(buffer, buffer.length);
                    if (bytesRead > 0) {
                        System.out.println("收到原始数据（十六进制）：" + HexFormat.of().formatHex(buffer));
                        // 解析数据为业务对象
                        latestTag = parseHdxData(buffer);
                        // 保存到数据库
                        if (latestTag != null) {
                            fishTagService.saveFishTag(latestTag);
                        }
                    }
                }
            }
        });
    }

    @Override


    public FishTag parseHdxData(byte[] data) {
        FishTag hdxData = new FishTag();

        // 生成1或2的随机数，赋值给reefNo


        // 原有卡号解析逻辑
        if (data.length >= 11) {
            byte[] cardNoBytes = Arrays.copyOfRange(data, 7, 11);
            if (cardNoBytes.length == 4) {
                long cardNoDecimal = ((long) (cardNoBytes[0] & 0xFF) << 24)
                        | ((long) (cardNoBytes[1] & 0xFF) << 16)
                        | ((long) (cardNoBytes[2] & 0xFF) << 8)
                        | (cardNoBytes[3] & 0xFF);
                hdxData.setCardNo(cardNoDecimal);
                int randomReefNo = ThreadLocalRandom.current().nextInt(2) + 1; // nextInt(2)生成0或1，+1后为1或2
                hdxData.setReefNo(randomReefNo);
                hdxData.setReceiveTime(LocalDateTime.now());
            } else {
                hdxData=null;
            }

        } else {
            hdxData=null;
        }

        return hdxData;
    }
    @Override
    public FishTag getLatestHdxData() {
        return latestTag;
    }



*/




















/*  public FishTag parseHdxData(byte[] data) {

        FishTag hdxData = new FishTag();
        // 协议格式参考：02 03 08 FF 52 A3 E7 00 00 00 31 C4 42（示例数据）
        if (data.length >= 10) { // 确保数据长度足够解析
            // 超时时间：第4字节（0xFF → 255*0.02=5.12秒）
            int timeoutRaw = data[3] & 0xFF; // 转无符号整数
            //hdxData.setTimeout(timeoutRaw * 0.02);
            // 信号强度：第5字节（0x52 → 82）
            //hdxData.setSignalStrength(data[4] & 0xFF);
            // 国家代号：第6-7字节（0xA3 E7 → 取低4位0x3E7=999）
            int countryCodeRaw = ((data[5] & 0x0F) << 8) | (data[6] & 0xFF);
           // hdxData.setCountryCode(countryCodeRaw);
            // 卡号：第8-11字节（00 00 00 31 → 转十进制或字符串）
            byte[] cardNoBytes = Arrays.copyOfRange(data, 7, 11);
            hdxData.setCardNo(HexFormat.of().formatHex(cardNoBytes));
            LocalDateTime currentTime = LocalDateTime.now();
            // 接收时间
            hdxData.setReceiveTime(currentTime);
        }
        return hdxData;
    }*/