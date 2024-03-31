package com.xin.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author Retrograde-LX
 * @Date 2023/12/18 下午 2:33
 * @Version 1.0
 * @Remark 又是程序猿秃头的一天
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TendencyVo {
    private List<Integer> allowList;
    private List<Integer> rejectList;
}
