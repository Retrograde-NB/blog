package com.xin.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xin.blog.entity.Article;
import com.xin.blog.entity.Category;
import com.xin.blog.entity.Comment;
import com.xin.blog.mapper.CommentMapper;
import com.xin.blog.res.ResponseResult;
import com.xin.blog.service.*;
import com.xin.blog.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author Retrograde-LX
 * @Date 2023/12/17 上午 11:08
 * @Version 1.0
 * @Remark 又是程序猿秃头的一天
 */
@Service
public class EchartsServiceImpl implements EchartsService {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @Autowired
    private LinkService linkService;

    @Autowired
    private TagService tagService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public ResponseResult getTypeList() {
        return ResponseResult.okResult(toTypeListVo());
    }

    @Override
    public ResponseResult getBlogType() {
        List<Category> categoryList = categoryService.list();
        List<BlogTypeVo> blogTypeVoList = new ArrayList<>();
        categoryList.forEach(item -> {
            LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Article::getCategoryId, item.getId());
            int count = articleService.count(wrapper);
            blogTypeVoList.add(new BlogTypeVo(count, item.getName()));
        });
        return ResponseResult.okResult(blogTypeVoList);
    }

    @Override
    public ResponseResult getCommentList(Integer pageNum, Integer pageSize) {
        EchartsTotalCommentVo echartsTotalCommentVo = new EchartsTotalCommentVo();
        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByDesc(Comment::getCreateTime);
        Page<Comment> page = new Page<>(pageNum, pageSize);
        commentService.page(page, lambdaQueryWrapper);
        List<EchartsCommentVo> echartsCommentVoList = page.getRecords().stream().map(item -> {
            EchartsCommentVo echartsCommentVo = new EchartsCommentVo();
            echartsCommentVo.setDate(item.getCreateTime());
            echartsCommentVo.setName(item.getContent());
            echartsCommentVo.setType(item.getStatus().equals("0") ? "已通过" : "未通过");
            echartsCommentVo.setColor(item.getStatus().equals("0") ? "#59B572" : "#E15C5E");
            return echartsCommentVo;
        }).collect(Collectors.toList());
        LambdaQueryWrapper<Comment> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(Comment::getStatus, "0");
        int allowCount = commentService.count(wrapper1);

        LambdaQueryWrapper<Comment> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(Comment::getStatus, "1");
        int rejectCount = commentService.count(wrapper2);

        int total = commentService.count();

        echartsTotalCommentVo.setEchartsCommentVoList(echartsCommentVoList);
        echartsTotalCommentVo.setAllowCount(allowCount);
        ;
        echartsTotalCommentVo.setRejectCount(rejectCount);
        echartsTotalCommentVo.setTotal(total);
        return ResponseResult.okResult(echartsTotalCommentVo);
    }

    @Override
    public ResponseResult getTendencyData() {
        //List<Integer> allowList = new ArrayList<>();
        //List<Integer> rejectList = new ArrayList<>();
        //int[] mouths = new int[]{1,2,3,4,5,6,7,8,9,10,11,12};
        //int year = LocalDate.now().getYear();
        //for (int mouth : mouths) {
        //    LocalDate startDate = LocalDate.of(year,mouth,1);
        //    LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        //    LambdaQueryWrapper<Comment> allowWrapper = new LambdaQueryWrapper<>();
        //    allowWrapper.eq(Comment::getStatus, "0");
        //    allowWrapper.between(Comment::getCreateTime, startDate, endDate);
        //    LambdaQueryWrapper<Comment> rejectWrapper = new LambdaQueryWrapper<>();
        //    rejectWrapper.eq(Comment::getStatus, "1");
        //    rejectWrapper.between(Comment::getCreateTime, startDate, endDate);
        //    allowList.add(commentService.count(allowWrapper));
        //    rejectList.add(commentService.count(rejectWrapper));
        //}
        //return ResponseResult.okResult(new TendencyVo(allowList, rejectList));
        int year = LocalDate.now().getYear();
        List<Integer> allowList = new ArrayList<>(Collections.nCopies(12, 0));
        List<Integer> rejectList = new ArrayList<>(Collections.nCopies(12, 0));
        List<Map<String, Object>> allowMap = commentMapper.countMonthlyData("0", year);
        List<Map<String, Object>> rejectMap = commentMapper.countMonthlyData("1", year);

        allowMap.forEach(item -> {
            allowList.set(Integer.parseInt(item.get("month").toString()) - 1, Integer.parseInt(item.get("total").toString()));
        });

        rejectMap.forEach(item -> {
            rejectList.set(Integer.parseInt(item.get("month").toString()) - 1, Integer.parseInt(item.get("total").toString()));
        });

        return ResponseResult.okResult(new TendencyVo(allowList, rejectList));
    }

    public List<EchartsTypeVo> toTypeListVo() {
        // 博客数量
        int articleCount = articleService.count();
        EchartsTypeVo articleCountType = new EchartsTypeVo("博客数量", articleCount, "篇");

        // 用户数量
        int userCount = userService.count();
        EchartsTypeVo userCountType = new EchartsTypeVo("用户数量", userCount, "人");

        // 友链数量
        int linkCount = linkService.count();
        EchartsTypeVo linkCountType = new EchartsTypeVo("友链数量", linkCount, "个");

        // 标签数量
        int tagCount = tagService.count();
        EchartsTypeVo tagCountType = new EchartsTypeVo("标签数量", tagCount, "个");

        // 类别个数
        int categoryCount = categoryService.count();
        EchartsTypeVo categoryCountType = new EchartsTypeVo("类别个数", categoryCount, "类");

        List<EchartsTypeVo> echartsTypeVoList = new ArrayList<>();
        echartsTypeVoList.add(articleCountType);
        echartsTypeVoList.add(userCountType);
        echartsTypeVoList.add(linkCountType);
        echartsTypeVoList.add(tagCountType);
        echartsTypeVoList.add(categoryCountType);

        return echartsTypeVoList;
    }
}
