package com.lds.admin.utils;

import com.lds.admin.enums.RSAKeyType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.buf.HexUtils;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.RSAPrivateKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.*;
import java.util.Base64;
import java.util.Enumeration;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @date 2024/6/1
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RSAUtils {

    private static final String PUBLIC_KEY = "PUBLIC KEY";

    private static final String PRIVATE_KEY = "PRIVATE KEY";

    private static final String RSA_PRIVATE_KEY = "RSA PRIVATE KEY";

    private static final String CERTIFICATE = "CERTIFICATE";

    private static final Pattern PEM_DATA = Pattern.compile("-----BEGIN (.*)-----(.*)-----END (.*)-----", Pattern.DOTALL);

    public static final String ALGORITHM = "RSA";

    public static final String SHA_256 = "SHA-256";

    public static final String SHA_1 = "SHA-1";

    public static final String MGF1 = "MGF1";

    public static final String CERTIFICATE_TYPE_X509 = "X509";

    private static final String PKCS12 = "PKCS12";

    public static final String DEFAULT_ALGORITHM_WITH_MODE_PADDING
            = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";

    public static final String SIGN_ALGORITHM_SHA256_WITH_RSA = "SHA256withRSA";


    public static PublicKey loadRSAX509PublicKey(String filePath) {
        String fileContent = ClassPathFileUtils.classPathFileContent(filePath);
        return (PublicKey) parseRSAKey(fileContent, RSAKeyType.PUBLIC_KEY);
    }


    public static PrivateKey loadRSAPKCS8PrivateKey(String filePath) {
        String fileContent = ClassPathFileUtils.classPathFileContent(filePath);
        return (PrivateKey) parseRSAKey(fileContent, RSAKeyType.PRIVATE_KEY);
    }

    /**
     * 读取PKCS1秘钥, 并生成秘钥对
     *
     * @param filePath
     * @return
     */
    public static KeyPair loadRSAPKCS1KeyPair(String filePath) {
        String fileContent = ClassPathFileUtils.classPathFileContent(filePath);
        return parseKeyPair(fileContent);
    }

    /**
     * 读取PKCS12秘钥
     *
     * @param filePath
     * @param password 秘钥密码
     * @return
     */
    public static PrivateKey loadRSAPKCS12PrivateKey(String filePath, String password) {
        try (InputStream inputStream = ClassPathFileUtils.classPathFileOrOther(filePath)) {
            return parseP12PrivateKey(inputStream, password);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * 读取公钥证书
     *
     * @param filePath
     * @return
     */
    public static PublicKey loadRSACertificate(String filePath) {
        String fileContent = ClassPathFileUtils.classPathFileContent(filePath);
        return parsePublicKeyFromCertificate(fileContent);
    }


    /**
     * 加密后转 16进制
     *
     * @param key
     * @param data
     * @return
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     */
    public static String encryptToHex(PublicKey key, String data) throws InvalidKeyException {
        return encryptToHex(key, data.getBytes());
    }

    /**
     * 加密后转 16进制
     *
     * @param key
     * @param data
     * @return
     * @throws InvalidKeyException
     */
    public static String encryptToHex(PublicKey key, byte[] data) throws InvalidKeyException {
        return HexUtils.toHexString(encrypt(key, data));
    }

    /**
     * 加密
     *
     * @param key
     * @param data
     * @return
     * @throws InvalidKeyException
     */
    public static byte[] encrypt(Key key, byte[] data) throws InvalidKeyException {
        final Cipher cipher;
        try {
            cipher = Cipher.getInstance(DEFAULT_ALGORITHM_WITH_MODE_PADDING);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new IllegalStateException(e);
        }
        OAEPParameterSpec parameterSpec = new OAEPParameterSpec(SHA_256, MGF1,
                new MGF1ParameterSpec(SHA_1), PSource.PSpecified.DEFAULT);
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
        } catch (InvalidAlgorithmParameterException e) {
            throw new IllegalStateException(e);
        }
        try {
            return cipher.doFinal(data);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new IllegalArgumentException("RSA加密参数错误", e);
        }
    }

    /**
     * 解密  16进制数据
     *
     * @param key
     * @param encryptedHex
     * @return
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     */
    public static String decryptHex(PrivateKey key, String encryptedHex) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        byte[] decrypt = decrypt(key, HexUtils.fromHexString(encryptedHex));
        return new String(decrypt);
    }

    /**
     * 解密
     *
     * @param key
     * @param encryptedData
     * @return
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static byte[] decrypt(Key key, byte[] encryptedData) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        final Cipher cipher;
        try {
            cipher = Cipher.getInstance(DEFAULT_ALGORITHM_WITH_MODE_PADDING);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new IllegalStateException(e);
        }
        OAEPParameterSpec parameterSpec = new OAEPParameterSpec(SHA_256, MGF1,
                new MGF1ParameterSpec(SHA_1), PSource.PSpecified.DEFAULT);
        try {
            cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);
        } catch (InvalidAlgorithmParameterException e) {
            throw new IllegalStateException(e);
        }
        return cipher.doFinal(encryptedData);
    }


    /**
     * 提取并解析密钥
     *
     * @param pemContent
     * @param rsaKeyType
     * @return
     */
    public static Key parseRSAKey(String pemContent, RSAKeyType rsaKeyType) {
        String type = switch (rsaKeyType) {
            case PUBLIC_KEY -> PUBLIC_KEY;
            case PRIVATE_KEY -> PRIVATE_KEY;
        };


        byte[] content = validateKeyAndDecodeContent(pemContent, type);

        try {
            return switch (rsaKeyType) {
                case PUBLIC_KEY -> getPublicKey(content);
                case PRIVATE_KEY -> getPrivateKey(content);
            };
        } catch (Exception e) {
            throw new IllegalArgumentException("无法解析RSA密钥", e);
        }

    }


    public static PublicKey getPublicKey(byte[] encryptedPublicKey) throws Exception {
        return KeyFactory.getInstance(RSAUtils.ALGORITHM)
                .generatePublic(new X509EncodedKeySpec(encryptedPublicKey));
    }

    public static PrivateKey getPrivateKey(byte[] encryptedPrivateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return KeyFactory.getInstance(RSAUtils.ALGORITHM)
                .generatePrivate(new PKCS8EncodedKeySpec(encryptedPrivateKey));
    }

    public static byte[] utf8Encode(String string) {
        return string.getBytes(StandardCharsets.UTF_8);
    }


    /**
     * 秘钥文件字符中解析秘钥
     *
     * @param pemData
     * @return
     */
    public static KeyPair parseKeyPair(String pemData) {

        // 校验秘钥内容格式并base64解码秘钥内容
        byte[] content = validateKeyAndDecodeContent(pemData, RSA_PRIVATE_KEY);

        ASN1Sequence seq = ASN1Sequence.getInstance(content);
        if (seq.size() != 9) {
            throw new IllegalArgumentException("RSA秘钥结构不正确");
        }

        // 读取私钥
        RSAPrivateKey key = RSAPrivateKey.getInstance(seq);

        // 获取公钥参数对象
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(key.getModulus(), key.getPublicExponent());
        // 获取私钥参数对象
        RSAPrivateCrtKeySpec privateCrtKeySpec = new RSAPrivateCrtKeySpec(key.getModulus(), key.getPublicExponent(),
                key.getPrivateExponent(), key.getPrime1(), key.getPrime2(), key.getExponent1(), key.getExponent2(),
                key.getCoefficient());

        try {
            // 生成公钥与私钥对象
            KeyFactory fact = KeyFactory.getInstance(ALGORITHM);
            PublicKey publicKey = fact.generatePublic(publicKeySpec);
            PrivateKey privateKey = fact.generatePrivate(privateCrtKeySpec);
            return new KeyPair(publicKey, privateKey);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("无法解析RSA密钥", e);
        }
    }


    /**
     * 从 .cer 文件中读取证书内容
     *
     * @return
     */
    public static X509Certificate parseCertificate(String cer) {

        // 校验证书内容格式并base64解码秘钥内容
        byte[] content = validateKeyAndDecodeContent(cer, CERTIFICATE);

        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance(CERTIFICATE_TYPE_X509);
            return (X509Certificate) certificateFactory.generateCertificate(
                    new ByteArrayInputStream(content));
        } catch (CertificateException e) {
            throw new IllegalArgumentException("解析证书失败", e);
        }
    }

    /**
     * 从证书中读取公钥
     *
     * @param cer
     * @return
     */
    public static PublicKey parsePublicKeyFromCertificate(String cer) {
        return parseCertificate(cer).getPublicKey();
    }


    /**
     * 解析PKCS12格式的RSA私钥
     *
     * @param priKeyStream
     * @param password
     * @return
     * @throws Exception
     */
    public static PrivateKey parseP12PrivateKey(InputStream priKeyStream, String password) throws IOException {
        byte[] reads = IOUtils.toByteArray(priKeyStream);
        return parseP12PrivateKey(reads, password);
    }


    /**
     * 解析PKCS12格式的RSA私钥
     *
     * @param pfxBytes
     * @param password 私钥密码
     * @return
     */
    public static PrivateKey parseP12PrivateKey(byte[] pfxBytes, String password) {

        char[] passwordChar = Optional.ofNullable(password)
                .map(String::toCharArray)
                .orElseGet(() -> new char[0]);

        try {
            KeyStore ks = KeyStore.getInstance(PKCS12);
            ks.load(new ByteArrayInputStream(pfxBytes), passwordChar);

            Enumeration<String> aliasEnum = ks.aliases();
            String keyAlias = null;
            if (aliasEnum.hasMoreElements()) {
                keyAlias = aliasEnum.nextElement();
            }

            return (PrivateKey) ks.getKey(keyAlias, passwordChar);

        } catch (Exception e) {
            throw new IllegalArgumentException("解析证书失败", e);
        }
    }


    /**
     * 校验秘钥内容格式并base64解码秘钥内容
     *
     * @param fileContent
     * @param type
     * @return
     */
    private static byte[] validateKeyAndDecodeContent(String fileContent, String type) {
        Matcher m = PEM_DATA.matcher(fileContent.trim());
        if (!m.matches()) {
            throw new IllegalArgumentException("秘钥格式不正确");
        }
        String title = m.group(1);

        if (!title.equals(type)) {
            throw new IllegalArgumentException(title + " 不是正确的秘钥格式");
        }

        // Base64.getMimeDecoder() 可以处理换行符
        // Base64.getDecoder() 适用于没有换行符的内容, 因此要提前处理换行符
        return Base64.getMimeDecoder().decode(utf8Encode(m.group(2)));
    }


    /**
     * SHA256withRSA 签名
     *
     * @param privateKey
     * @param data
     * @return
     */
    public static String signToHexUseSHA256withRSA(PrivateKey privateKey, String data) {
        return signToHex(privateKey, data, SIGN_ALGORITHM_SHA256_WITH_RSA);
    }

    /**
     * 签名
     *
     * @param privateKey
     * @param data
     * @return
     */
    public static String signToHex(PrivateKey privateKey, String data, String signAlgorithm) {
        return HexUtils.toHexString(sign(privateKey, data, signAlgorithm));
    }



    public static byte[] sign(PrivateKey privateKey, String data, String signAlgorithm) {
        try {
            return sign(privateKey, data.getBytes(), signAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("签名算法: " + signAlgorithm + " 不支持", e);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("签名秘钥无效", e);
        } catch (SignatureException e) {
            throw new IllegalArgumentException("签名失败", e);
        }
    }

    public static byte[] sign(PrivateKey privateKey, byte[] data, String signAlgorithm) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance(signAlgorithm);
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }


    /**
     * SHA256withRSA 验签 (16进制签名)
     *
     * @param publicKey
     * @param data      待验签串
     * @param sign      签名
     * @return
     */
    public static boolean verifyHexSignUseSHA256withRSA(PublicKey publicKey, String data, String sign) {
        return verifyHexSign(publicKey, data, sign, SIGN_ALGORITHM_SHA256_WITH_RSA);
    }

    /**
     * 验签
     *
     * @param publicKey
     * @param data
     * @param sign          hex签名
     * @param signAlgorithm 算法
     * @return
     */
    public static boolean verifyHexSign(PublicKey publicKey, String data, String sign, String signAlgorithm) {
        byte[] bytes;
        try {
            bytes = HexUtils.fromHexString(sign);
        } catch (Exception e) {
            return false;
        }
        try {
            boolean b = verifySign(publicKey, data.getBytes(), bytes, signAlgorithm);
            return b;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("签名算法: " + signAlgorithm + " 不支持", e);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("签名秘钥无效", e);
        } catch (SignatureException e) {
            return false;
        }
    }


    public static boolean verifySign(PublicKey publicKey, byte[] data, byte[] sign, String signAlgorithm) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance(signAlgorithm);
        signature.initVerify(publicKey);
        signature.update(data);
        boolean verify = signature.verify(sign);
        return verify;
    }


}
