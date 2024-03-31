package com.xin.blog.controller;

import com.xin.blog.annotatino.SystemLog;
import com.xin.blog.res.ResponseResult;
import com.xin.blog.service.LinkService;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @SystemLog(businessName = "获取有脸信息")
    @GetMapping("/getAllLink")
    public ResponseResult getAllLink(){
        return linkService.getAllLink();
    }

}
