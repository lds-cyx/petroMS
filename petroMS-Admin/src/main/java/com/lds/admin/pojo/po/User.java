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
 * @since 2025-05-08
 */
@Data
@TableName("user")
@ApiModel(value="User对象", description="")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    private String salt;

    private String name;

    private String sex;

    private String email;

    private String cellphone;

    private String qq;

    @ApiModelProperty(value = "用户状态")
    private String status;

    @ApiModelProperty(value = "用户创建的时间")
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "用户更新的时间")
    private LocalDateTime updatedTime;


}
