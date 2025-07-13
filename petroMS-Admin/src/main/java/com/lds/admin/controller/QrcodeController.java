package com.lds.admin.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/qrcode")
public class QrcodeController {
    private static final int CODE_WIDTH = 300;
    private static final int CODE_HEIGHT = 300;
    private static final int FRONT_COLOR = 0x000000;
    private static final int BACK_COLOR = 0xFFFFFF;

    @GetMapping(produces = "image/png")
    public void generateQrcode(HttpServletResponse response) throws IOException, WriterException {
        // 设置响应头
        response.setContentType("image/png");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        String content = "123";

        // 生成二维码
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.MARGIN, 1);

        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, CODE_WIDTH, CODE_HEIGHT, hints);
        BufferedImage image = toBufferedImage(bitMatrix);

        // 输出到响应流
        ImageIO.write(image, "png", response.getOutputStream());
    }

    private BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y)? FRONT_COLOR : BACK_COLOR);
            }
        }
        return image;
    }
}    