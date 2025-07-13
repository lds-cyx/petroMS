package com.lds.admin.service.impl;


import com.alibaba.fastjson.JSON;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lds.admin.mapper.PermissionMapper;
import com.lds.admin.mapper.UserMapper;
import com.lds.admin.pojo.po.PermitDetail;
import com.lds.admin.pojo.po.User;
import com.lds.base.expection.PMSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));

        if (user == null) {
            PMSException.cast("用户不存在!");
        }

        String password = user.getPassword();

        Long id = user.getId();
        // 用户表  角色表  根据角色表到权限表里面搜索对应权限
        List<PermitDetail> permission = permissionMapper.selectPermission(id);

        List<String> permitList = permission.stream().map(PermitDetail::getPermit).collect(Collectors.toList());

        List<String> splitResult = new ArrayList<>();

        for (int i = 0; i < permitList.size(); i++) {
            String[] split = permitList.get(i).split(",");
            for (int j = 0; j < split.length; j++) {
                splitResult.add(split[j]);
            }
        }

        String[] authorities = splitResult.toArray(new String[0]);


        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(JSON.toJSONString(user)).password(password).authorities(authorities).build();

        return userDetails;
    }

}
