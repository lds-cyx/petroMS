package com.lds.base.utils;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Mr.M
 * @version 1.0
 * @description 获取当前用户身份工具类
 * @date 2022/10/18 18:02
 */
@Slf4j
public class SecurityUtil {

    public static Integer getUserId() {
        try {
            Object principalObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principalObj instanceof Integer) {
                //取出用户身份信息
                Integer userId = (Integer) principalObj;
                //将json转成对象
//                User user = JSON.parseObject(principal, User.class);
                return userId;
            }
        } catch (Exception e) {
//            log.error("获取当前登录用户身份出错:{}", e.getMessage());
            e.printStackTrace();
        }

        return null;
    }







}
