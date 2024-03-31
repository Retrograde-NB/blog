package com.xin.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xin.blog.entity.Article;
import org.apache.ibatis.annotations.Param;

public interface ArticleMapper extends BaseMapper<Article> {
    void updateByIdUpdateViewCount(@Param("id") Long id,@Param("viewCount") Long viewCount);
}
