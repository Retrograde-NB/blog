package com.xin.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xin.blog.entity.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 评论表(Comment)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-18 08:45:02
 */
public interface CommentMapper extends BaseMapper<Comment> {
    List<Map<String, Object>> countMonthlyData(@Param("status") String status, @Param("year") Integer year);
}
