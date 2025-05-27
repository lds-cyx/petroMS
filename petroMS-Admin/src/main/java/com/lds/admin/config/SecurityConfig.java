package com.lds.admin.config;

import com.lds.admin.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //配置用户信息服务
    // 我们不从内存里面查了
//    @Bean
//    public UserDetailsService userDetailsService() {
//        //这里配置用户信息,这里暂时使用这种方式将用户存储在内存中
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(User.withUsername("zhangsan").password("123").authorities("p1").build());
//        manager.createUser(User.withUsername("lisi").password("456").authorities("p2").build());
//        return manager;
//    }
    @Autowired
    DaoAuthenticationProviderCustom daoAuthenticationProviderCustom;

    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    // 认证
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        daoAuthenticationProviderCustom.setPasswordEncoder(passwordEncoder());
        auth.authenticationProvider(daoAuthenticationProviderCustom);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 密码为明文方式  如果数据库是明文 输入的也是明文 就可以比对
        // 但是现在数据库 是密文 所以要使用BCrypt加密比对
        //return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder();
        // 这种比对方式吊的地方就是 你输入的原密码不变 但是加密后的密码都不一样
        // 但是和数据库里面那个不变的密码却能每次都比对成功
    }

    //配置安全拦截机制
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // 禁用 CSRF 保护
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/registe/**").authenticated()//访问/r开始的请求需要认证通过
                .antMatchers("/level1").hasRole("vlp1") // 这个路劲要有vip1才行
                .antMatchers().permitAll()
                .anyRequest().permitAll()//其它请求全部放行
                .and();
//                .formLogin(); 没有权限会自动到登陆界面  需要开启登陆界面 /login
        http.logout().logoutUrl("/logout");//退出地址

        // 将 JwtAuthenticationFilter 添加到过滤器链中
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
