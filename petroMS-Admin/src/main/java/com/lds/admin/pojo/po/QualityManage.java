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
@TableName("quality_manage")
@ApiModel(value="QualityManage对象", description="")
public class QualityManage implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "质量管理表主键，唯一标识每条质量检验记录")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "关联 plan_schedule 表的 id，标识该质量检验对应的生产计划")
    private Integer planScheduleId;

    @ApiModelProperty(value = "产品的编号")
    private Integer productId;

    @ApiModelProperty(value = "产品的检验日期")
    private LocalDateTime inspectTime;

    @ApiModelProperty(value = "产品的检验结果，如合格、不合格等")
    private String result;

    @ApiModelProperty(value = "执行检验的人员姓名")
    private String inspector;

    @ApiModelProperty(value = "质量检验的备注信息")
    private String remarks;

    @ApiModelProperty(value = "记录质量检验数据创建的时间")
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "记录质量检验数据更新的时间")
    private LocalDateTime updatedTime;


}
