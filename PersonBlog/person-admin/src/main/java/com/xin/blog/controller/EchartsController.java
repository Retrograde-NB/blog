package com.xin.blog.controller;

import com.xin.blog.res.ResponseResult;
import com.xin.blog.service.EchartsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Retrograde-LX
 * @Date 2023/12/14 下午 6:56
 * @Version 1.0
 * @Remark 又是程序猿秃头的一天
 */
@RestController
@RequestMapping("/index/echarts")
public class EchartsController {
    @Autowired
    private EchartsService echartsService;

    @GetMapping("/getTypeList")
    public ResponseResult getTypeList(){
        return echartsService.getTypeList();
    }

    @GetMapping("/getBlogType")
    public ResponseResult getBlogType(){
        return echartsService.getBlogType();
    }

    @GetMapping("/getCommentList")
    public ResponseResult getCommentList(Integer pageNum, Integer pageSize){
        return echartsService.getCommentList(pageNum, pageSize);
    }

    @GetMapping("/getTendencyData")
    public ResponseResult getTendencyData(){
        return echartsService.getTendencyData();
    }
}
