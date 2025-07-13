package com.lds.admin.pojo.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PlanScheduleDto {
    @ApiModelProperty(value = "计划调度表主键，唯一标识每条生产计划记录")
    private Integer id;

    @ApiModelProperty(value = "生产计划的名称")
    private String planName;

    @ApiModelProperty(value = "生产计划的开始日期")
    private LocalDate beginDate;

    @ApiModelProperty(value = "生产计划的结束日期")
    private LocalDate endDate;

    @ApiModelProperty(value = "生产计划的状态，如未开始、进行中、已完成等")
    private String status;

    @ApiModelProperty(value = "生产计划的描述信息")
    private String description;

    @ApiModelProperty(value = "记录生产计划创建的时间")
    private LocalDateTime createdTime;
}
