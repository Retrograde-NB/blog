package com.xin.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author Retrograde-LX
 * @Date 2023/12/13 下午 10:29
 * @Version 1.0
 * @Remark 又是程序猿秃头的一天
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BackendCommentVo {
    private Long id;
    private Long articleId;
    private String title;
    private Long rootId;
    private String content;
    private Date createTime;
    private String status;
}
