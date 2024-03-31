package com.xin.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author Retrograde-LX
 * @Date 2023/12/17 下午 2:56
 * @Version 1.0
 * @Remark 又是程序猿秃头的一天
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EchartsTotalCommentVo {
    private Integer allowCount;
    private Integer rejectCount;
    List<EchartsCommentVo> echartsCommentVoList;
    private Integer total;
}
