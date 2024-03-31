package com.xin.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Retrograde-LX
 * @Date 2023/12/17 上午 11:21
 * @Version 1.0
 * @Remark 又是程序猿秃头的一天
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EchartsTypeVo {
    private String title;
    private Integer count;
    private String type;
}
