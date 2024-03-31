package com.xin.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xin.blog.constans.SystemConstants;
import com.xin.blog.dto.CategoryDto;
import com.xin.blog.dto.CategoryListDto;
import com.xin.blog.entity.Article;
import com.xin.blog.entity.Category;
import com.xin.blog.exception.SystemException;
import com.xin.blog.mapper.CategoryMapper;
import com.xin.blog.res.AppHttpCodeEnum;
import com.xin.blog.res.ResponseResult;
import com.xin.blog.service.ArticleService;
import com.xin.blog.service.CategoryService;
import com.xin.blog.utils.BeanCopyUtils;
import com.xin.blog.vo.CategoryVo;
import com.xin.blog.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2022-10-14 11:22:29
 */
@Service("sgCategoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private ArticleService articleService;

    @Override
    public ResponseResult getCategoryList() {
        // 查询文章表，状态已为发布的文章
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        articleWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(articleWrapper);
        // 获取文章类型的id并且去重
        Set<Long> categoryIds = articleList.stream()
                .map(article -> article.getCategoryId())
                .collect(Collectors.toSet());

        System.out.println(categoryIds);

        // 查询分类表
        if (categoryIds == null) {

        }
        List<Category> categories = this.listByIds(categoryIds);

        System.out.println(categories);

        categories = categories.stream()
                .filter(category -> SystemConstants.CATEGORY_STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());
        // 封装vo
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);

        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult listAllCategory() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getStatus,SystemConstants.NORMAL);
        List<Category> categoryList = this.list(queryWrapper);
        List<CategoryVo> categoryVos = categoryList.stream()
                .map(category -> BeanCopyUtils.copyBean(category, CategoryVo.class))
                .collect(Collectors.toList());
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult getPageList(Integer pageNum, Integer pageSize, CategoryListDto categoryListDto) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(categoryListDto.getName()),Category::getName,categoryListDto.getName());
        queryWrapper.eq(StringUtils.hasText(categoryListDto.getStatus()),Category::getStatus,categoryListDto.getStatus());

        Page<Category> page = new Page<>(pageNum,pageSize);
        this.page(page,queryWrapper);

        return ResponseResult.okResult(new PageVo(page.getRecords(),page.getTotal()));
    }

    @Override
    public ResponseResult addCategory(CategoryDto categoryDto) {
        Category category = BeanCopyUtils.copyBean(categoryDto, Category.class);
        this.save(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getCategoryInfoById(Long id) {
        Category category = this.getById(id);
        CategoryVo categoryVo = BeanCopyUtils.copyBean(category, CategoryVo.class);
        return ResponseResult.okResult(categoryVo);
    }

    @Override
    public ResponseResult updateCategory(CategoryDto categoryDto) {
        Category category = BeanCopyUtils.copyBean(categoryDto, Category.class);
        this.updateById(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteCategoryByList(List<Long> ids) {
        ids.forEach(id -> {
            Category category = this.getById(id);
            if (Objects.isNull(category)) {
                throw new SystemException(AppHttpCodeEnum.ID_IS_NOT);
            }
            this.removeById(category);
        });
        return ResponseResult.okResult();
    }
}

