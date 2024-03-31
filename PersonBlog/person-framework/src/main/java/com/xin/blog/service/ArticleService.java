package com.xin.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xin.blog.dto.AddArticleDto;
import com.xin.blog.dto.ArticleListDto;
import com.xin.blog.dto.UpdateArticleDto;
import com.xin.blog.entity.Article;
import com.xin.blog.res.ResponseResult;
import com.xin.blog.vo.PageVo;

import java.util.List;

public interface ArticleService extends IService<Article> {
    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult add(AddArticleDto article);

    ResponseResult<PageVo> getPageList(Integer pageNum, Integer pageSize, ArticleListDto articleListDto);

    ResponseResult getArticleById(Long id);

    ResponseResult updateArticleById(UpdateArticleDto updateArticleDto);

    ResponseResult deleteArticleById(Long id);

    void updateBatchByIdUpdateViewCount(List<Article> articles);

    ResponseResult deleteArticleByList(List<Long> ids);
}
