package com.xin.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xin.blog.constans.SystemConstants;
import com.xin.blog.dto.AddArticleDto;
import com.xin.blog.dto.ArticleListDto;
import com.xin.blog.dto.UpdateArticleDto;
import com.xin.blog.entity.Article;
import com.xin.blog.entity.ArticleTag;
import com.xin.blog.entity.Category;
import com.xin.blog.exception.SystemException;
import com.xin.blog.mapper.ArticleMapper;
import com.xin.blog.res.AppHttpCodeEnum;
import com.xin.blog.res.ResponseResult;
import com.xin.blog.service.ArticleService;
import com.xin.blog.service.ArticleTagService;
import com.xin.blog.service.CategoryService;
import com.xin.blog.utils.BeanCopyUtils;
import com.xin.blog.utils.RedisCache;
import com.xin.blog.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleTagService articleTagService;

    @Override
    public ResponseResult hotArticleList() {
        // 查询热门文章 封装成ResponseResult返回
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // 必须时正式文章
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        // 按照浏览量进行排序
        queryWrapper.orderByDesc(Article::getViewCount);
        // 最多只查询10条
        Page<Article> page = new Page(1, 10);
        page(page, queryWrapper);

        List<Article> articles = page.getRecords();

        // bean拷贝
//        List<HotArticleVo> articleVos = new ArrayList<>();
//        for (Article article : articles) {
//            HotArticleVo vo = new HotArticleVo();
//            BeanUtils.copyProperties(article,vo);
//            articleVos.add(vo);
//        }
        List<HotArticleVo> articleVos = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);
        return ResponseResult.okResult(articleVos);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        // 查询条件
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 有categoryId就需要查询分类
        lambdaQueryWrapper.eq(Objects.nonNull(categoryId) && categoryId > 0, Article::getCategoryId, categoryId);
        // 状态是正式发布的
        lambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        // 对isTop进行降序
        lambdaQueryWrapper.orderByDesc(Article::getIsTop);
        // 分页查询
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, lambdaQueryWrapper);

        // 查询categoryName
        List<Article> articles = page.getRecords();
        // categoryId去查询categoryName进行设置(for循环方式)
//        for (Article article : articles) {
//            Category category = categoryService.getById(article.getCategoryId());
//            article.setCategoryName(category.getName());
//        }
        // categoryId去查询categoryName进行设置(stream流方式)
        articles = articles.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());

        // 封装结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);

        PageVo pageVo = new PageVo(articleListVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        // 根据id查询文章
        Article article = this.getById(id);
        // 从redis中获取viewCount
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        article.setViewCount(viewCount.longValue());
        // 转换成vo
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        // 根据分类id查询分类名
        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if (category != null) {
            articleDetailVo.setCategoryName(category.getName());
        }
        // 封装响应返回
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        //更新redis中对应 id的浏览量
        redisCache.incrementCacheMapValue("article:viewCount", id.toString(), 1);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult add(AddArticleDto articleDto) {
        //添加 博客
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        save(article);


        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());

        //添加 博客和标签的关联
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<PageVo> getPageList(Integer pageNum, Integer pageSize, ArticleListDto articleListDto) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(articleListDto.getTitle()), Article::getTitle, articleListDto.getTitle());
        queryWrapper.like(StringUtils.hasText(articleListDto.getSummary()), Article::getSummary, articleListDto.getSummary());

        Page<Article> page = new Page<>(pageNum,pageSize);
        this.page(page,queryWrapper);

        PageVo pageVo = new PageVo(page.getRecords(),page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleById(Long id) {
        Article article = this.getById(id);
        if (Objects.isNull(article)) {
            throw new SystemException(AppHttpCodeEnum.ID_IS_NOT);
        }
        UpdateGetArticleByIdVo updateGetArticleByIdVo = BeanCopyUtils.copyBean(article, UpdateGetArticleByIdVo.class);
        // 获取tags的值
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId,id);
        List<String> list = articleTagService.list(queryWrapper).stream()
                .map(articleTag -> articleTag.getTagId().toString())
                .collect(Collectors.toList());
        updateGetArticleByIdVo.setTags(list);
        return ResponseResult.okResult(updateGetArticleByIdVo);
    }

    @Override
    public ResponseResult updateArticleById(UpdateArticleDto updateArticleDto) {
        // 获取Article数据
        Article article = BeanCopyUtils.copyBean(updateArticleDto, Article.class);
        // 获取ArticleTag数据（先删后更）
        List<ArticleTag> articleTags = updateArticleDto.getTags().stream()
                .map(tag -> new ArticleTag(updateArticleDto.getId(), Long.parseLong(tag)))
                .collect(Collectors.toList());
        // 删
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId,updateArticleDto.getId());
        articleTagService.remove(queryWrapper);
        // 更
        this.updateById(article);
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteArticleById(Long id) {
        if(Objects.isNull(this.getById(id))){
            throw new SystemException(AppHttpCodeEnum.ID_IS_NOT);
        }
        this.removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public void updateBatchByIdUpdateViewCount(List<Article> articles) {
        articles.forEach(article -> {
            articleMapper.updateByIdUpdateViewCount(article.getId(), article.getViewCount());
        });
        //articles.stream().map(article -> {
        //    articleMapper.updateByIdUpdateViewCount(article.getId(), article.getViewCount());
        //    return null;
        //});
    }

    @Override
    public ResponseResult deleteArticleByList(List<Long> ids) {
        ids.forEach(id -> {
            Article article = this.getById(id);
            if (Objects.isNull(article)){
                throw new SystemException(AppHttpCodeEnum.ID_IS_NOT);
            }
            this.removeById(article);
        });
        return ResponseResult.okResult();
    }
}
