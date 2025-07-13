package com.lds.admin.service;

import com.lds.admin.pojo.dto.UserDto;
import com.lds.admin.pojo.po.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lds
 * @since 2025-05-08
 */
public interface IUserService extends IService<User> {
    String toLogin(UserDto userDto);
}
