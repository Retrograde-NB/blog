package com.xin.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xin.blog.dto.ChangeCommentStatusDto;
import com.xin.blog.dto.CommentDto;
import com.xin.blog.entity.Comment;
import com.xin.blog.res.ResponseResult;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2022-10-18 08:45:02
 */
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(String type,Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);

    ResponseResult getCommentList(Integer pageNum, Integer pageSize,CommentDto commentDto);

    ResponseResult changeStatus(ChangeCommentStatusDto changeCommentStatusDto);
}


