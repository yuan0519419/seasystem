package com.itheima;

import com.itheima.service.SerialReceiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 */
@SpringBootApplication
public class BigEventApplication implements CommandLineRunner {
    @Autowired
    private SerialReceiveService serialService;



    public static void main(String[] args) {
        SpringApplication.run(BigEventApplication.class,args);
    }


    @Override
    public void run(String... args) throws Exception {
        serialService.startListening();
        System.out.println("串口监听已启动，等待接收数据...");
    }

}
