package com.lds.admin.interceptor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lds.admin.annotation.Decrypt;
import com.lds.admin.filter.MyHttpServletRequestWrapper;
import com.lds.admin.properties.WebRSAAndSignProperties;
import com.lds.admin.utils.AESUtils;
import com.lds.admin.utils.RSAUtils;
import com.lds.base.expection.PMSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.DataInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;


/**
 * @author lds
 * @version 1.0
 */
@Component
public class GlobalInterceptor implements HandlerInterceptor {

    @Autowired
    WebRSAAndSignProperties webRSAAndSignProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 检查方法是否有 @Decrypt 注解
        if (handlerMethod.hasMethodAnnotation(Decrypt.class)) {
            // 执行解密逻辑
            PrivateKey privateKey = webRSAAndSignProperties.getPrivateKey();
            PublicKey publicKey = webRSAAndSignProperties.getPublicKey();
            decryptParameters(request, privateKey, publicKey);
            return true;
        }
        return true;
        //1.判断sign timestamp参数是否携带 app_id不用在这里判断，直接在相关的bean上面通过注解实现
//        checkMustParameter(request, privateKey, publicKey);
        //2.根据URI 判断是否需要鉴权，比如文章设置了 VIP 可见、登录可见、游客可见等，
        // 那么如果是查询文章的 URI 就要做一个鉴权判断
        //3.参数解密


    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

//        ThreadLocalUtil.remove();
    }

    /**
     * 检查每个请求必须携带的参数，并且校验是否合法
     */
   /* private void checkMustParameter(HttpServletRequest request, PrivateKey privateKey, PublicKey publicKey) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, JsonProcessingException {


        String encryptData = request.getParameter("encryptData");
        String encryptKey = request.getParameter("encryptKey");


        String decryptAesKey = RSAUtils.decryptHex(privateKey, encryptKey);

        String decryptData = AESUtils.decryptBase64(AESUtils.secretKey(decryptAesKey), encryptData);


    }*/

    /**
     * 解密参数
     */
    private void decryptParameters(HttpServletRequest request, PrivateKey privateKey, PublicKey publicKey) throws IOException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {

        ServletInputStream inputStream = request.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        byte[] bytes = dataInputStream.readAllBytes();

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> map = objectMapper.readValue(new String(bytes), Map.class);


        String encryptKey = map.get("encryptKey");
        String encryptData = map.get("encryptData");
        String decryptAesKey = RSAUtils.decryptHex(privateKey, encryptKey);
        String decryptData = AESUtils.decryptBase64(AESUtils.secretKey(decryptAesKey), encryptData);
//        String decryptDataWithout = decryptData.replace("\\", "");
        JsonNode jsonNode = objectMapper.readTree(decryptData);
        String originalData = jsonNode.get("dataJson").asText();
        String sign = jsonNode.get("sign").asText();

        boolean flag = RSAUtils.verifyHexSignUseSHA256withRSA(publicKey, originalData, sign);
        if (!flag) {
            PMSException.cast("签名验证失败");
        }

// 问题 1
//        UsernamePasswordAuthenticationToken existingAuth = (UsernamePasswordAuthenticationToken)
//                SecurityContextHolder.getContext().getAuthentication();
//        existingAuth.setDetails(decryptAesKey);
        // 获取当前认证信息
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        // 检查认证状态
        if (authentication == null || !authentication.isAuthenticated()) {
            PMSException.cast("用户未认证");
        }

        // 设置认证信息
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            ((UsernamePasswordAuthenticationToken) authentication).setDetails(decryptAesKey);
        } else {
            // 如果是匿名认证，创建新的认证信息
            UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
                    authentication.getPrincipal(),
                    authentication.getCredentials(),
                    authentication.getAuthorities()
            );
            newAuth.setDetails(decryptAesKey);
            SecurityContextHolder.getContext().setAuthentication(newAuth);
        }


//        String s = objectMapper.writeValueAsString(originalData);
        ((MyHttpServletRequestWrapper) request).setBody(originalData.getBytes());
    }
}
