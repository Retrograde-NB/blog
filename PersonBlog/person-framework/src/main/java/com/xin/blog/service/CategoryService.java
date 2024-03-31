package com.xin.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xin.blog.dto.CategoryDto;
import com.xin.blog.dto.CategoryListDto;
import com.xin.blog.entity.Category;
import com.xin.blog.res.ResponseResult;

import java.util.List;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2022-10-14 11:22:29
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    ResponseResult listAllCategory();

    ResponseResult getPageList(Integer pageNum, Integer pageSize, CategoryListDto categoryListDto);

    ResponseResult addCategory(CategoryDto categoryDto);

    ResponseResult getCategoryInfoById(Long id);

    ResponseResult updateCategory(CategoryDto categoryDto);

    ResponseResult deleteCategoryByList(List<Long> ids);
}


