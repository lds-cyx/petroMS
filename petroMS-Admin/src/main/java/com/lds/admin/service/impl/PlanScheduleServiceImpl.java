package com.lds.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lds.admin.mapper.ProcessMonitorMapper;
import com.lds.admin.mapper.QualityManageMapper;
import com.lds.admin.pojo.po.PlanSchedule;
import com.lds.admin.mapper.PlanScheduleMapper;
import com.lds.admin.pojo.po.ProcessMonitor;
import com.lds.admin.pojo.po.QualityManage;
import com.lds.admin.service.IPlanScheduleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lds.base.expection.PMSException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lds
 * @since 2025-05-10
 */
@Service
@AllArgsConstructor
@Slf4j
public class PlanScheduleServiceImpl extends ServiceImpl<PlanScheduleMapper, PlanSchedule> implements IPlanScheduleService {

    private final ProcessMonitorMapper processMonitorMapper;

    private final QualityManageMapper qualityManageMapper;


    @Override
    public boolean deleteByID(Integer id) {

        Integer processMonitorCount = processMonitorMapper.selectCount(
                new LambdaQueryWrapper<ProcessMonitor>().eq(ProcessMonitor::getPlanScheduleId, id));

        if (processMonitorCount > 0) {
            PMSException.cast("有对应监控过程存在,删除失败!");
        }


        List<QualityManage> qualityManages = qualityManageMapper.selectList(new LambdaQueryWrapper<QualityManage>().eq(QualityManage::getPlanScheduleId, id));
        qualityManages.stream().forEach(
                qualityManage -> {
                    if (!qualityManage.getResult().equals("合格")){
                        PMSException.cast("计划对应产品质量不达标,删除失败!");
                    }
                }
        );

        boolean planFlag = removeById(id);
        if (!planFlag) {
            log.error("删除计划失败!计划id:{}", id);
            PMSException.cast("删除计划失败!");
        }
        return true;
    }
}
