package com.lds.admin.aspect;

import com.lds.admin.properties.WebRSAAndSignProperties;
import com.lds.admin.utils.AESUtils;
import com.lds.admin.utils.RSAUtils;
import com.lds.admin.utils.SecurityUtil;
import com.lds.base.model.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;
@Aspect
@Component
@Slf4j
public class EncryptAspect {

    @Autowired
    WebRSAAndSignProperties webRSAAndSignProperties;


    @Pointcut("@annotation(com.lds.admin.annotation.Encrypt)")
    public void myAnnotation() {

    }

    @Around(value = "myAnnotation()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        PrivateKey privateKey = webRSAAndSignProperties.getPrivateKey();
        PublicKey publicKey = webRSAAndSignProperties.getPublicKey();
        String aesKey = SecurityUtil.getAesKey();

        Object result = joinPoint.proceed();

        if (result instanceof RestResponse) {
            RestResponse<?> response = (RestResponse<?>) result;

            // 加密data字段（假设data是String类型）
            if (response.getResult() != null) {
                Object originalData = response.getResult();
                String result_sign = RSAUtils.signToHexUseSHA256withRSA(privateKey, originalData.toString());
                String encryptedData = AESUtils.encryptToBase64(AESUtils.secretKey(aesKey), originalData.toString());
                // 创建新的RestResponse，保持结构不变，仅替换data字段
                return RestResponse.encryptSuccess(encryptedData, result_sign);
            }
        }
        return result;
    }
}
