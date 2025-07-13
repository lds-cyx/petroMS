package com.lds.admin.filter;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "myFilter", urlPatterns = {
            "/**"})
@Component
public class GlobalFilter implements Filter {
            
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
            
        //上传文件不做解密处理
        if ("multipart/form-data".equals(((HttpServletRequest) request).getHeader("Content-Type"))) {
            
            chain.doFilter(request, response);
        } else {
            
            MyHttpServletRequestWrapper MyHttpServletRequest = new MyHttpServletRequestWrapper((HttpServletRequest) request);
            chain.doFilter(MyHttpServletRequest, response);
        }
    }
}