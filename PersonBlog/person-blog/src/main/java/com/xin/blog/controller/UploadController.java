package com.xin.blog.controller;

import com.xin.blog.annotatino.SystemLog;
import com.xin.blog.res.ResponseResult;
import com.xin.blog.service.UploadService;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @SystemLog(businessName = "上传头像")
    @PostMapping("/upload")
    public ResponseResult uploadImg(MultipartFile img){
        return uploadService.uploadImg(img);
    }

}
