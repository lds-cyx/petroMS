package com.lds.admin.pojo.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author lds
 * @since 2025-05-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("plan_schedule")
@ApiModel(value="PlanSchedule对象", description="")
public class PlanSchedule implements Serializable {

    private static final long serialVersionUID = 1L;

    /*在插入数据时，主键无需手动赋值，由数据库自动生成。*/
    @ApiModelProperty(value = "计划调度表主键，唯一标识每条生产计划记录")
    @TableId(value = "id", type = IdType.AUTO)
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

    @ApiModelProperty(value = "记录生产计划更新的时间")
    private LocalDateTime updatedTime;


}
