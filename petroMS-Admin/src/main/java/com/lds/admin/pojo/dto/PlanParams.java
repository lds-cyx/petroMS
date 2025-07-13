package com.lds.admin.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lds.base.model.PageParams;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDate;



@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class PlanParams extends PageParams {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "生产计划的名称")
    private String planName;

    @ApiModelProperty(value = "生产计划的开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate beginDate;

    @ApiModelProperty(value = "生产计划的结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @ApiModelProperty(value = "生产计划的状态，如未开始、进行中、已完成等")
    private int status;

    @ApiModelProperty(value = "生产计划的描述信息")
    private String description;


}
