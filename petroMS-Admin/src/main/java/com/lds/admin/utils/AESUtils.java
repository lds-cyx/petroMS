package com.lds.admin.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * AES加密相关工具
 * 注意:  JDK  1.8.0_161 之前AES 只支持 128位即16长度的秘钥
 * 之后可以支持  256位即32长度的秘钥
 *
 * @date 2024/7/27
 **/
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AESUtils {

    /**
     * JDK 默认使用: AES/ECB/PKCS5padding
     */
    private static final String ALGORITHM = "AES";

    private static final int KEY_LENGTH_BYTE = 32;


    /**
     * 从字符中获取 SecretKey
     *
     * @param key32 32长度
     * @return
     */
    public static SecretKey secretKey(String key32) {
        return secretKey(key32.getBytes());
    }

    /**
     * 获取 SecretKey
     *
     * @param keyBytes
     * @return
     */
    private static SecretKey secretKey(byte[] keyBytes) {
        if (keyBytes.length != KEY_LENGTH_BYTE) {
            throw new IllegalArgumentException("密钥长度必须为32");
        }
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }


    /**
     * 加密然后 转base64
     *
     * @param secretKey
     * @param data
     * @return
     */
    public static String encryptToBase64(SecretKey secretKey, String data) {
        byte[] encrypt = encrypt(secretKey, data.getBytes());
        return new String(Base64.getEncoder().encode(encrypt));
    }


    /**
     * AES 加密
     *
     * @param secretKey
     * @param data
     * @return
     */
    public static byte[] encrypt(SecretKey secretKey, byte[] data) {

        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new IllegalArgumentException(e);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("不正确的AES加密密钥", e);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new IllegalArgumentException("AES数据加密失败");
        }
    }


    /**
     * 解密 base64密文
     *
     * @param secretKey
     * @param encryptedBase64
     * @return
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static String decryptBase64(SecretKey secretKey, String encryptedBase64) throws BadPaddingException, IllegalBlockSizeException {
        byte[] decode = Base64.getDecoder().decode(encryptedBase64);
        byte[] decrypt = decrypt(secretKey, decode);
        return new String(decrypt);
    }

    /**
     * 解密
     *
     * @param secretKey
     * @param encrypted
     * @return
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static byte[] decrypt(SecretKey secretKey, byte[] encrypted) throws BadPaddingException, IllegalBlockSizeException {

        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return cipher.doFinal(encrypted);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new IllegalArgumentException(e);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("不正确的AES加密密钥", e);
        }


    }


}
