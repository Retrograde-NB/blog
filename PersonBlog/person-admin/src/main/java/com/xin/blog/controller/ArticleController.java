package com.xin.blog.controller;

import com.xin.blog.dto.AddArticleDto;
import com.xin.blog.dto.ArticleListDto;
import com.xin.blog.dto.UpdateArticleDto;
import com.xin.blog.res.ResponseResult;
import com.xin.blog.service.ArticleService;
import com.xin.blog.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, ArticleListDto articleListDto){
        return articleService.getPageList(pageNum,pageSize,articleListDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getArticleById(@PathVariable("id") Long id){
        return articleService.getArticleById(id);
    }

    @PutMapping
    public ResponseResult updateArticleById(@RequestBody UpdateArticleDto updateArticleDto){
        return articleService.updateArticleById(updateArticleDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteArticleById(@PathVariable("id") Long id){
        return articleService.deleteArticleById(id);
    }

    @PostMapping
    public ResponseResult add(@RequestBody AddArticleDto article){
        return articleService.add(article);
    }

    @DeleteMapping("/deleteArticleByList")
    public ResponseResult deleteArticleByList(@RequestBody List<Long> ids){
        return articleService.deleteArticleByList(ids);
    }
}
