package com.lds.admin.aspect;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lds.admin.properties.WebRSAAndSignProperties;
import com.lds.admin.utils.AESUtils;
import com.lds.admin.utils.RSAUtils;
import com.lds.base.expection.PMSException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;

import static com.lds.admin.utils.RSAUtils.decryptHex;


@Component
@Slf4j
public class DecryptAspect {
    @Autowired
    private WebRSAAndSignProperties webRSAAndSignProperties;

//
//    @Pointcut("@annotation(com.lds.admin.annotation.Decrypt)")
//    public void myAnnotation() {
//
//    }
//
//
//    @Around("myAnnotation()")
//    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
////        PrivateKey privateKey = webRSAAndSignProperties.getPrivateKey();
////        PublicKey publicKey = webRSAAndSignProperties.getPublicKey();
////
////        Object[] args = joinPoint.getArgs();
////        String encryptAesKey = (String) args[1];
////        String aesKey = RSAUtils.decryptHex(privateKey, encryptAesKey);
////
////        String encryptData = (String) args[0];
////        String decryptData = AESUtils.decryptBase64(AESUtils.secretKey(aesKey), encryptData);
////
////        ObjectMapper objectMapper = new ObjectMapper();
////        JsonNode jsonNode = objectMapper.readTree(decryptData);
////        String sign = jsonNode.get("sign").asText();
////        String data = jsonNode.get("data").asText();
////        boolean flag = RSAUtils.verifyHexSignUseSHA256withRSA(publicKey, data, sign);
////        if (!flag) {
////            PMSException.cast("签名验证失败");
////        }
////        return joinPoint.proceed(new Object[]{decryptData});
//        return null;
//    }
}
