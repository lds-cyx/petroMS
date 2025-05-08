package com.lds.admin.filter;

import com.lds.base.properties.JwtProperties;
import com.lds.base.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtProperties jwtProperties;

    //白名单
    private static List<String> whitelist = null;

    static {
        //加载白名单
        try (
                InputStream resourceAsStream = JwtAuthenticationFilter.class.getResourceAsStream("/security-whitelist.properties");
        ) {
            Properties properties = new Properties();
            properties.load(resourceAsStream);
            Set<String> strings = properties.stringPropertyNames();
            whitelist= new ArrayList<>(strings);

        } catch (Exception e) {
            log.error("加载/security-whitelist.properties出错:{}",e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestUrl = request.getRequestURI();
        AntPathMatcher pathMatcher = new AntPathMatcher();
        //白名单放行
        for (String url : whitelist) {
            if (pathMatcher.match(url, requestUrl)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        //1、从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getAdminTokenName());


        if(StringUtils.isBlank(token)) {
            throw new RuntimeException("没有令牌!");
        }

        //2、校验令牌
        try {
            log.info("jwt校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);

            Integer userId = claims.get("userId",Integer.class);
            log.info("当前用户id:{}", userId);

            // 获取 authorities 信息
            List<Map<String, String>> authoritiesMaps = (List<Map<String, String>>) claims.get("authorities");
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            if (authoritiesMaps != null) {
                for (Map<String, String> authorityMap : authoritiesMaps) {
                    // 假设权限信息存储在 "authority" 键中
                    String authority = authorityMap.get("authority");
                    if (authority != null) {
                        authorities.add(new SimpleGrantedAuthority(authority));
                    }
                }
            }

            //存入SecurityContextHolder
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId,null,authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            try {
                // 继续处理请求
                filterChain.doFilter(request, response);
            } finally {
                // 请求处理完成后，清空 Spring Security 上下文
                SecurityContextHolder.clearContext();
            }
        } catch (Exception ex) {
            //4、不通过，响应401状态码
            SecurityContextHolder.clearContext();
            response.setStatus(401);
        }
    }
}
