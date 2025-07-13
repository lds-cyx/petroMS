package com.lds.admin.filter;

import cn.hutool.core.io.IoUtil;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MyHttpServletRequestWrapper extends HttpServletRequestWrapper {
            
    private  byte[] body;

    public MyHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
            
        super(request);
        request.setCharacterEncoding("UTF-8");
        body = IoUtil.readBytes(request.getInputStream());
    }

    public void setBody(byte[] body){
            
        this.body = body;
    }

    @Override
    public BufferedReader getReader() throws IOException {
            
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }


    @Override
    public ServletInputStream getInputStream() throws IOException {
            
        final ByteArrayInputStream bis = new ByteArrayInputStream(body);

        return new ServletInputStream() {
            

            @Override
            public int read() throws IOException {
            
                return bis.read();
            }

            @Override
            public boolean isFinished() {
            
                return false;
            }

            @Override
            public boolean isReady() {
            
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            

            }
        };
    }
}