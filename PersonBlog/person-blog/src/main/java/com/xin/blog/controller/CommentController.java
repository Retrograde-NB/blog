package com.xin.blog.controller;

import com.xin.blog.annotatino.SystemLog;
import com.xin.blog.constans.SystemConstants;
import com.xin.blog.dto.AddCommentDto;
import com.xin.blog.entity.Comment;
import com.xin.blog.res.ResponseResult;
import com.xin.blog.service.CommentService;
import com.xin.blog.utils.BeanCopyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "评论", description = "评论相关接口")
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @SystemLog(businessName = "获取文章评论信息")
    @GetMapping("/commentList")
    public ResponseResult commentList(Long articleId,Integer pageNum,Integer pageSize){
        return commentService.commentList(SystemConstants.ARTICLE_COMMENT,articleId,pageNum,pageSize);
    }

    @SystemLog(businessName = "添加评论信息")
    @PostMapping
    public ResponseResult addComment(@RequestBody AddCommentDto comment){
        Comment comment1 = BeanCopyUtils.copyBean(comment, Comment.class);
        return commentService.addComment(comment1);
    }

    @ApiOperation(value = "友联评论列表", notes = "获取一页友联评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页号"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小")
    })
    @SystemLog(businessName = "获取友联评论信息")
    @GetMapping("/linkCommentList")
    public ResponseResult linkCommentList(Integer pageNum,Integer pageSize){
        return commentService.commentList(SystemConstants.LINK_COMMENT,null,pageNum,pageSize);
    }
}
