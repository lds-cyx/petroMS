package com.lds.admin.properties;

import com.lds.admin.utils.RSAUtils;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @date 2024/6/1
 **/
@Data
@ConfigurationProperties(prefix = "web.rsa")
@Component
public class WebRSAAndSignProperties implements InitializingBean {

    private String keyPath;

    private String keyPassword;

    private String clientPublic;

    private PublicKey publicKey;

    private PrivateKey privateKey;

    @Override
    public void afterPropertiesSet() throws Exception {
        publicKey = RSAUtils.loadRSAX509PublicKey(clientPublic);
//        privateKey = RSAUtils.loadRSAPKCS8PrivateKey(privateKeyPath);
        privateKey = RSAUtils.loadRSAPKCS12PrivateKey(keyPath, keyPassword);
    }
}
