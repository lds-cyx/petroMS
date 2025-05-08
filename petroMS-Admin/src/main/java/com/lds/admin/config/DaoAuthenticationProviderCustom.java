package com.lds.admin.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * @description 自定义DaoAuthenticationProvider
 * @author Mr.M
 * @date 2022/9/29 10:31
 * @version 1.0
 */
@Slf4j
@Component
public class DaoAuthenticationProviderCustom extends DaoAuthenticationProvider {


 // 我个人认为userDetailsService的实现类已经写了loadUsername这个方法
 // 所以在没有写这个统一登陆接口时候 没有把这个set进去
 @Autowired
 public void setUserDetailsService(UserDetailsService userDetailsService) {
  super.setUserDetailsService(userDetailsService);
 }


 // 屏蔽密码对比   因为统一登陆接口 里面有些登陆方法 不需要判断密码
 // 所以处理密码这个逻辑方法 需要我们自己写
// protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
//
//
// }

}