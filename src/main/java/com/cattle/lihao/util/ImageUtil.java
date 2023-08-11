package com.cattle.lihao.util;

import cn.hutool.core.img.Img;
import cn.hutool.core.io.FileUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

/**
 * 图片工具类
 *
 * @author niujie
 * @date 2023/8/7 11:50
 */
public class ImageUtil {

    /**
     * 图片加工方法
     * <p>
     * 支持的图片格式: BMP, bmp, jpg, JPG, wbmp, jpeg, png, PNG, JPEG, WBMP, GIF, gif
     *
     * @param imgUrl       目标文件的地址
     * @param outputStream 输出流
     * @param width        图片裁剪后的宽度
     * @param height       图片裁剪后的高度
     * @throws IOException
     */
    public static void imageProcess(String imgUrl, ByteArrayOutputStream outputStream,
                                    int width, int height) throws IOException {
        byte[] imgBytes = getImgBytes(imgUrl);
        try (InputStream pojoInputStream = new ByteArrayInputStream(imgBytes);
             InputStream readInputStream = new ByteArrayInputStream(imgBytes)) {
            // 图片对象
            BufferedImage bufferedImage = ImageIO.read(pojoInputStream);
            // 高度
            float imgHeight = Float.parseFloat(String.valueOf(bufferedImage.getHeight()));
            // 缩放比例
            float scale = 1 / (imgHeight / height);
            // 矩形对象
            Rectangle rectangle = new Rectangle(0, 0, width, height);
            // 读文件 压缩 裁剪 写文件
            Img.from(readInputStream)
                    .scale(scale)
                    .cut(rectangle)
                    .write(outputStream);
        }
    }

    /**
     * 获取文件二进制数据
     *
     * @param imgUrl 图片地址
     * @return 二进制数据
     */
    public static byte[] getImgBytes(String imgUrl) throws IOException {
        try (InputStream inputStream = getImgInputStream(imgUrl);
             ByteArrayOutputStream swapStream = new ByteArrayOutputStream()) {
            int rc;
            byte[] buff = new byte[100];
            while ((rc = inputStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            return swapStream.toByteArray();
        }
    }

    /**
     * 获取文件输入流
     *
     * @param imgUrl 图片地址
     * @return 输入流
     */
    public static InputStream getImgInputStream(String imgUrl) throws IOException {
        InputStream inputStream;
        try {
            // 网络图片
            URL url = new URL(imgUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            inputStream = connection.getInputStream();
        } catch (Exception urlException) {
            // 本地图片
            File file = FileUtil.file(imgUrl);
            inputStream = Files.newInputStream(file.toPath());
        }
        return inputStream;
    }

    public static void main(String[] args) throws IOException {
//        try (ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
//             OutputStream outputStream = Files.newOutputStream(new File("D:/testImg/testImg1.jpg").toPath())) {
//            String url = "https://cn.bing.com/th?id=OHR.SunriseCastle_ZH-CN6235928386_1920x1080.jpg";
//            ImageUtil.imageProcess(url, arrayOutputStream, 600, 600);
//            arrayOutputStream.writeTo(outputStream);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        cutIdCordImg("D:\\testImg\\niujie.jpg","D:\\testImg\\niujie1.jpg");
        File fromFile = new File("D:\\testImg\\niujie.jpg");
        File toFile = new File("D:\\testImg\\niujie1.jpg");
        resizePng(fromFile, toFile, 600, 800, false);

    }

    public static void cutIdCordImg(String sourceImgPath, String targetImgPath) throws IOException {
        // 读入大图
        File file = new File(sourceImgPath);
        FileInputStream fis = new FileInputStream(file);
        BufferedImage image = ImageIO.read(fis);
        /*
         * 假设我需要切割的四点坐标为a(20,30)b(200,40)c(30,200)d(200,210)
         * 起始坐标为(最小的x,最小的y)
         * 此时的实际切割坐标应为由a为起始坐标,width为(x坐标最大的点的x - a的x坐标),height为(y坐标最大的点的y - a的y坐标)
         * 此时的width为180,height为180
         */
        //切割图片
        BufferedImage bf = new BufferedImage(180, 180, image.getType());
        Graphics2D graphics2D = bf.createGraphics();
        graphics2D.drawImage(image, 20, 30, 180, 180, 0, 0, 180, 180, null);
        graphics2D.dispose();
        //输出图片
        ImageIO.write(bf, "jpg", new File(targetImgPath));
        System.out.println("success...");
    }

    public static void resizePng(File fromFile, File toFile, int outputWidth, int outputHeight,
                                 boolean proportion) {
        try {
            BufferedImage bi2 = ImageIO.read(fromFile);
            int newWidth;
            int newHeight;
            // 判断是否是等比缩放
            if (proportion) {
                // 为等比缩放计算输出的图片宽度及高度
                double rate1 = ((double) bi2.getWidth(null)) / (double) outputWidth + 0.1;
                double rate2 = ((double) bi2.getHeight(null)) / (double) outputHeight + 0.1;
                // 根据缩放比率大的进行缩放控制
                double rate = rate1 < rate2 ? rate1 : rate2;
                newWidth = (int) (((double) bi2.getWidth(null)) / rate);
                newHeight = (int) (((double) bi2.getHeight(null)) / rate);
            } else {
                newWidth = outputWidth; // 输出的图片宽度
                newHeight = outputHeight; // 输出的图片高度
            }
            BufferedImage to = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = to.createGraphics();
            to = g2d.getDeviceConfiguration().createCompatibleImage(newWidth, newHeight,
                    Transparency.TRANSLUCENT);
            g2d.dispose();
            g2d = to.createGraphics();
            @SuppressWarnings("static-access")
            Image from = bi2.getScaledInstance(newWidth, newHeight, bi2.SCALE_AREA_AVERAGING);
            g2d.drawImage(from, 0, 0, null);
            g2d.dispose();
            ImageIO.write(to, "png", toFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
