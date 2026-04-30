package com.itheima.controller;

import com.itheima.pojo.Result;
import com.itheima.utils.AliOssUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
public class FileUploadContorller {
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws Exception {
        String originalFilename = file.getOriginalFilename();
        String fliename = UUID.randomUUID().toString()+originalFilename.substring(originalFilename.lastIndexOf("."));
        String url = AliOssUtil.uploadFile(fliename, file.getInputStream());
        return Result.success(url);
    }
}
