package com.xin.blog.controller;

import com.xin.blog.dto.ChangeCommentStatusDto;
import com.xin.blog.dto.CommentDto;
import com.xin.blog.res.ResponseResult;
import com.xin.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author Retrograde-LX
 * @Date 2023/12/11 下午 2:39
 * @Version 1.0
 * @Remark 又是程序猿秃头的一天
 */
@RestController
@RequestMapping("/content/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize,CommentDto commentDto) {
        return commentService.getCommentList(pageNum, pageSize, commentDto);
    }

    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody ChangeCommentStatusDto changeCommentStatusDto){
        return commentService.changeStatus(changeCommentStatusDto);
    }
}
