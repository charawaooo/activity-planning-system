package com.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;

public class CaptchaUtil {

    // 生成指定长度的随机验证码字符串
    public static String generateCaptcha(int length) {
        String charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder captcha = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            captcha.append(charset.charAt(random.nextInt(charset.length())));
        }
        return captcha.toString();
    }

    // 将验证码字符串渲染成图片，并写入到输出流中
    public static void generateCaptchaImage(String captcha, int width, int height, ServletOutputStream outputStream) throws IOException {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // 设置背景色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // 设置字体
        Font font = new Font("Arial", Font.BOLD, 30);
        g.setFont(font);

        // 设置字体颜色
        g.setColor(Color.BLACK);

        // 绘制验证码字符串
        g.drawString(captcha, 10, 30);

        // 绘制干扰线
        for (int i = 0; i < 5; i++) {
            int x1 = (int) (Math.random() * width);
            int y1 = (int) (Math.random() * height);
            int x2 = (int) (Math.random() * width);
            int y2 = (int) (Math.random() * height);
            g.drawLine(x1, y1, x2, y2);
        }

        g.dispose();

        // 将图片写入输出流
        ImageIO.write(image, "JPEG", outputStream);
    }
}

