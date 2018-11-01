package com.mindongji.zxing;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        if (args.length <= 0) {
            System.out.println("Qrcode file path argument is missing");
            return;
        }
        String path = args[0];
        File file = new File(path);
        if (file == null || !file.exists()) {
            System.out.println(System.out.format("%s: File not found", path));
            return;
        }

        try {
            Result result = decode(path);
            if (result == null) {
                System.out.println(System.out.format("%s: exception = %s", path, "(qrcode decode fail)"));
                return;
            }
            System.out.println(System.out.format("%s (format: %S, type: %s):", path, result.getBarcodeFormat(), "file"));
            System.out.println("Raw result:");
            //String rawText = new String(result.getRawBytes(),"ASCII" );
            //System.out.println(rawText);
            System.out.println(result.getText());
            System.out.println("Parsed result:");
            System.out.println(result.getText());

            ResultPoint[] points = result.getResultPoints();
            System.out.println(System.out.format("Found %s result points.", points.length));

            for (int i = 0; i < points.length; i++) {
                ResultPoint point = points[i];
                System.out.println(System.out.format("  Point %d: (%s,%s)", i, point.getX(), point.getY()));
            }
        } catch (Exception e) {
            System.out.println(System.out.format("%s: exception = %s", path, e.getMessage()));
        }
    }

    public static Result decode(String path) throws NotFoundException {
        File imageFile = new File(path);
        BufferedImage image = null;
        Result result = null;
        try {
            image = ImageIO.read(imageFile);
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            //解码获取二维码中信息
            result = new MultiFormatReader().decode(binaryBitmap, hints);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
