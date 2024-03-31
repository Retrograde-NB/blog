package com.xin.blog.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.xin.blog.dto.CategoryDto;
import com.xin.blog.dto.CategoryListDto;
import com.xin.blog.entity.Category;
import com.xin.blog.res.AppHttpCodeEnum;
import com.xin.blog.res.ResponseResult;
import com.xin.blog.service.CategoryService;
import com.xin.blog.utils.BeanCopyUtils;
import com.xin.blog.utils.WebUtils;
import com.xin.blog.vo.ExcelCategoryVo;
import com.xin.blog.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        return categoryService.listAllCategory();
    }

    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("/export")
    public void export(HttpServletResponse response){
        try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx",response);
            //获取需要导出的数据
            List<Category> categoryVos = categoryService.list();

            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categoryVos, ExcelCategoryVo.class);
            //把数据写入到Excel中
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);

        } catch (Exception e) {
            //如果出现异常也要响应json
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }

    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, CategoryListDto categoryListDto){
        return categoryService.getPageList(pageNum,pageSize,categoryListDto);
    }
    @PreAuthorize("@ps.hasPermission('content:category:add')")
    @PostMapping
    public ResponseResult addCategory(@RequestBody CategoryDto categoryDto){
        return categoryService.addCategory(categoryDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getCategoryInfoById(@PathVariable("id") Long id){
        return categoryService.getCategoryInfoById(id);
    }

    @PutMapping
    public ResponseResult updateCategory(@RequestBody CategoryDto categoryDto){
        return categoryService.updateCategory(categoryDto);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteCategory(@PathVariable("id") Long id){
        categoryService.removeById(id);
        return ResponseResult.okResult();
    }
    @DeleteMapping("/deleteCategoryByList")
    public ResponseResult deleteCategoryByList(@RequestBody List<Long> ids){
        return categoryService.deleteCategoryByList(ids);
    }
}
