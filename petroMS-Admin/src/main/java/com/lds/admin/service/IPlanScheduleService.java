package com.lds.admin.service;

import com.lds.admin.pojo.po.PlanSchedule;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lds
 * @since 2025-05-10
 */
public interface IPlanScheduleService extends IService<PlanSchedule> {

    boolean deleteByID(Integer id);
}
