package com.lds.admin.utils;

import com.lds.base.expection.PMSException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * @author Mr.M
 * @version 1.0
 * @description 获取当前用户身份工具类
 * @date 2022/10/18 18:02
 */
@Slf4j
public class SecurityUtil {

    public static Long getUserId() {
        try {
            Object principalObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principalObj instanceof Long) {
                //取出用户身份信息
                Long userId = (Long) principalObj;
                //将json转成对象
//                User user = JSON.parseObject(principal, User.class);
                return userId;
            }
        } catch (Exception e) {
            log.error("获取当前登录用户身份出错:{}", e.getMessage());
            PMSException.cast("获取当前登录用户身份出错");
        }

        return null;
    }

    public static String getAesKey() {
        try {
            Object details = SecurityContextHolder.getContext().getAuthentication().getDetails();
            if (details instanceof String) {
                String aesKey = (String) details;
                return aesKey;
            }
        } catch (Exception e) {
            log.error("获取AesKey出错:{}", e.getMessage());
            PMSException.cast("获取AesKey出错");
        }
        return null;
    }







}
