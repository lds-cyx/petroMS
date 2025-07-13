package com.lds.base.model;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * @author Mr.M
 * @version 1.0
 * @description 分页查询通用参数
 * @date 2022/9/6 14:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PageParams {

    @ApiModelProperty("当前页码")
    private Long pageNo = 1L;

    @ApiModelProperty("每页记录数默认值")
    private Long pageSize = 30L;
}