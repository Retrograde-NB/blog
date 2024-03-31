package com.xin.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Retrograde-LX
 * @Date 2023/12/11 下午 4:28
 * @Version 1.0
 * @Remark 又是程序猿秃头的一天
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeCommentStatusDto {
    private Long id;
    private String status;
}
