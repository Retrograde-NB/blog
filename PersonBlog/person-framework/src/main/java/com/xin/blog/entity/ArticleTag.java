package com.xin.blog.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 文章标签关联表(ArticleTag)表实体类
 *
 * @author makejava
 * @since 2022-10-20 16:04:25
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("article_tag")
public class ArticleTag  {
    private static final long serialVersionUID = 625337492348897098L;
    //文章id@TableId
    private Long articleId;
    //标签id@TableId
    private Long tagId;
}
