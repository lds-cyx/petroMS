package com.lds.admin.service.impl;
import com.alibaba.fastjson.JSON;
import com.lds.admin.pojo.dto.UserDto;
import com.lds.admin.pojo.po.User;
import com.lds.admin.mapper.UserMapper;
import com.lds.admin.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lds.base.expection.PMSException;
import com.lds.admin.properties.JwtProperties;
import com.lds.admin.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lds
 * @since 2025-05-08
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public String toLogin(UserDto userDto) {

        // 判断验证码 晚点弄

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userDto.getUsername(), userDto.getPassword()));

        if (Objects.isNull(authentication)){
            PMSException.cast("账号或者密码错误!");
        }

        //获取userid 生成token
        org.springframework.security.core.userdetails.User user = (org.springframework.
                security.core.userdetails.User) authentication.getPrincipal();

        Long userId = JSON.parseObject(user.getUsername(), User.class).getId();

        Collection<GrantedAuthority> authorities = user.getAuthorities();

        HashMap<String, Object> map = new HashMap<>();
        map.put("userId",userId);
        map.put("authorities",authorities);

        String token = JwtUtil.createJWT(jwtProperties.getAdminSecretKey(), jwtProperties.getAdminTtl(), map);

        return token;
    }
}
