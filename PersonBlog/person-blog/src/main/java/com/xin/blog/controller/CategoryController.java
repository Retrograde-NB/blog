package com.xin.blog.controller;

import com.xin.blog.annotatino.SystemLog;
import com.xin.blog.res.ResponseResult;
import com.xin.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @SystemLog(businessName = "获取菜单类别")
    @GetMapping("/getCategoryList")
    public ResponseResult getCategoryList(){
        ResponseResult result = categoryService.getCategoryList();
        return result;
    }
}
