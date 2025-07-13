package com.lds.admin.mapper;

import com.lds.admin.pojo.po.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lds.admin.pojo.po.PermitDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lds
 * @since 2025-05-08
 */
@Mapper
public interface PermissionMapper{

    List<PermitDetail> selectPermission(@Param("id") Long id);
}
