package com.lds.admin.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.StreamUtils;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class EncryptAndDecryptHttpMessageConverter {
//    extends AbstractHttpMessageConverter<Object>


//    private final ObjectMapper objectMapper;

    @Value("${security.encrypt.key}") // 从配置文件或环境变量读取
    private String secretKey;


//    @Autowired
//    public EncryptAndDecryptHttpMessageConverter(ObjectMapper objectMapper, Cipher decryptCipher, Cipher encryptCipher) {
//        super(MediaType.APPLICATION_JSON); // 只处理JSON类型的请求
//        this.objectMapper = objectMapper;
//    }

//    @Override
    protected boolean supports(Class<?> clazz) {
        // 指定哪些类型的对象需要使用此转换器
        // 这里可以根据需要调整，例如：return BaseEntity.class.isAssignableFrom(clazz);
        return true;
    }

/*//    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        *//** 在 readInternal 方法中，先读取请求体的字节流（加密数据）。
        使用解密算法（如 AES）将字节流解密为明文（如 JSON 字符串）。
        通过 ObjectMapper 将明文 JSON 反序列化为目标对象。*//*
//        try {
//            // 读取加密请求体
//            byte[] encryptedData = StreamUtils.copyToByteArray(inputMessage.getBody());
//
//            // 解密数据
//
////            byte[] decryptedData = decryptCipher.doFinal(encryptedData);
////            String json = new String(decryptedData, StandardCharsets.UTF_8);
//
//        return  ;
//            // 转换为对象
//            return objectMapper.readValue(json, clazz);
//        } catch (Exception e) {
//            throw new HttpMessageNotReadableException("Failed to decrypt request body", e, inputMessage);
//        }
    }*/

//    @Override
    protected void writeInternal(Object o, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
         /** 在 writeInternal 方法中，先将对象序列化为明文 JSON 字符串。
            使用加密算法（如 AES）将 JSON 字符串加密为字节流。
            通过 CipherOutputStream 将加密后的字节流写入响应体。*/
//        try (CipherOutputStream cos = new CipherOutputStream(outputMessage.getBody(), encryptCipher)) {
//
//            // 将对象转换为JSON并加密响应
//            String json = objectMapper.writeValueAsString(o);
//            cos.write(json.getBytes(StandardCharsets.UTF_8));
//        } catch (Exception e) {
//            throw new HttpMessageNotWritableException("Failed to encrypt response body", e);
//        }
    }
}
