package com.xin.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Retrograde-LX
 * @Date 2023/12/03 上午 8:46
 * @Version 1.0
 * @Remark 又是程序猿秃头的一天
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkChangeStatusDto {
    private Long id;
    private String status;
}
