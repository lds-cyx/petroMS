package com.lds.admin.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 类路径文件工具;
 * <p>
 * 可以读取类路径或其他路径下的文件内容:  类路径必须以 classpath:开头
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClassPathFileUtils {

    private static final String CLASSPATH_STR = "classpath:";

    /**
     * 类路径或者非类路径文件内容
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static String classPathFileContentOrOther(String path, Charset charset) {
        try (InputStream inputStream = classPathFileOrOther(path)) {
            return IOUtils.toString(inputStream, charset);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * 获取类路径
     *
     * @param path
     * @return
     */
    public static InputStream classPathFileStream(String path) throws IOException {
        if (path.length() < CLASSPATH_STR.length()) {
            throw new IllegalArgumentException(path + " 不是类路径");
        }
        path = path.substring(CLASSPATH_STR.length(), path.length());
        ClassPathResource classPathResource = new ClassPathResource(path);
        return classPathResource.getInputStream();
    }

    /**
     * 类路径或者非类路径文件流
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static InputStream classPathFileOrOther(String path) throws IOException {
        if (isClassPath(path)) {
            return classPathFileStream(path);
        }
        return new FileInputStream(path);
    }



    public static String classPathFileContentOrOther(String path) {
        return classPathFileContentOrOther(path, StandardCharsets.UTF_8);
    }

    public static String classPathFileContent(String path, Charset charset) {
        try (InputStream inputStream = classPathFileStream(path)) {
            return IOUtils.toString(inputStream, charset);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String classPathFileContent(String path) {
        return classPathFileContent(path, StandardCharsets.UTF_8);
    }


    public static boolean isClassPath(String path) {
        return StringUtils.isNotBlank(path) && path.startsWith(CLASSPATH_STR);
    }

}