package com.lds.admin.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lds.admin.annotation.Decrypt;
import com.lds.admin.pojo.dto.PlanParams;
import com.lds.admin.pojo.dto.PlanScheduleDto;
import com.lds.admin.pojo.po.PlanSchedule;
import com.lds.admin.service.IPlanScheduleService;
import com.lds.base.expection.PMSException;
import com.lds.base.model.PageResult;
import com.lds.base.model.RestResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lds
 * @since 2025-05-10
 */
@RestController
@Slf4j
@RequestMapping("/plan")
@RequiredArgsConstructor
public class PlanScheduleController {

    private final IPlanScheduleService planService;


//    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/page")
    public RestResponse<PageResult<PlanScheduleDto>> getPlanList(@RequestBody PlanParams planParams) {
        /* 前端做处理 如果没有传pageNO 和 pageSize 那么 前端做默认值处理  后端不做了*/
        Long pageNo = planParams.getPageNo();
        Long pageSize = planParams.getPageSize();

        Page<PlanSchedule> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<PlanSchedule> pageWrapper = new LambdaQueryWrapper<PlanSchedule>()
                .like(StringUtils.isNotBlank(planParams.getPlanName()), PlanSchedule::getPlanName, planParams.getPlanName())
                .ge(planParams.getBeginDate() != null, PlanSchedule::getBeginDate, planParams.getBeginDate())
                .le(planParams.getEndDate() != null, PlanSchedule::getEndDate, planParams.getEndDate())
                .eq( PlanSchedule::getStatus, planParams.getStatus())
                .orderByDesc(PlanSchedule::getCreatedTime)
                .orderByDesc(PlanSchedule::getUpdatedTime);



        Page<PlanSchedule> pages = planService.page(page, pageWrapper);

//        if(null){
//            PMSException.cast("kucunbuzu");
//        }


        PageResult<PlanScheduleDto> pageResult = new PageResult<>
                (BeanUtil.copyToList(pages.getRecords(), PlanScheduleDto.class), pages.getTotal(), pageNo, pageSize);
        return RestResponse.success(pageResult);
    }

    @GetMapping("/get/{id}")
    public RestResponse<PlanScheduleDto> getById(@PathVariable("id") Integer id){
        PlanSchedule planSchedule = planService.getById(id);
        if (planSchedule == null){
            PMSException.cast("该计划不存在!");
        }
        PlanScheduleDto planScheduleDto = BeanUtil.copyProperties(planSchedule, PlanScheduleDto.class);
        return RestResponse.success(planScheduleDto);
    }


    @PostMapping("/save")
    @Decrypt
    public RestResponse<Void> addPlan(@RequestBody PlanParams planParams) {
        log.info("11:{}",planParams);
        PlanSchedule planSchedule = new PlanSchedule();
        BeanUtil.copyProperties(planParams,planSchedule);
        planSchedule.setCreatedTime(LocalDateTime.now());
        planSchedule.setUpdatedTime(LocalDateTime.now());
        boolean flag = planService.save(planSchedule);
        if (!flag){
            log.error("失败新增计划,计划:{}",planParams);
            PMSException.cast("新增计划失败!");
        }
//        String aesKey = SecurityUtil.getAesKey();
//        System.out.println(aesKey);
        return RestResponse.success();
    }

    @PutMapping("/update")
    public RestResponse<Void> updatePlan(@RequestBody PlanScheduleDto planScheduleDto){
        PlanSchedule planSchedule = BeanUtil.copyProperties(planScheduleDto, PlanSchedule.class);
        boolean flag = planService.updateById(planSchedule);
        if (!flag){
            log.error("失败修改计划id,id:{}",planScheduleDto.getId());
            PMSException.cast("修改计划失败!");
        }
        return RestResponse.success();
    }

    @DeleteMapping("/delete/{id}")
    public RestResponse<Boolean> deletePlan(@PathVariable("id") Integer id){
        boolean flag = planService.deleteByID(id);
        return RestResponse.success(flag);
    }

}
