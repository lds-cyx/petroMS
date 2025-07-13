package com.lds.admin.pojo.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("process_monitor")
@ApiModel(value="ProcessMonitor对象", description="")
public class ProcessMonitor implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "过程监控表主键，唯一标识每条监控记录")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "关联 plan_schedule 表的 id，标识该监控数据所属的生产计划")
    private Integer planScheduleId;

    @ApiModelProperty(value = "生产过程的名称")
    private String processName;

    @ApiModelProperty(value = "监控数据发生的时间点")
    private LocalDateTime monitorTime;

    @ApiModelProperty(value = "生产过程的状态，如正常、异常等")
    private String status;

    @ApiModelProperty(value = "监控数据的备注信息")
    private String remarks;

    @ApiModelProperty(value = "记录监控数据创建的时间")
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "记录监控数据更新的时间")
    private LocalDateTime updatedTime;


}
