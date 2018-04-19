package com.gzedu.xlims.common;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 图片base64的转化
 */
public class ImgBase64Convert {

    public static void main(String[] args) {
        String strImg = getImageStr("d:\\1.jpg");
        System.out.println(strImg);
        generateImage(strImg, "d:\\11.jpg");
    }

    /**
     * 图片转化为Base64字符串
     * @param imgFilePath 待处理的图片
     * @return
     */
    public static String getImageStr(String imgFilePath) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFilePath);
            data = new byte[in.available()];
            in.read(data);

            // 对字节数组Base64编码
            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encode(data);// 返回Base64编码过的字节数组字符串
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(in != null) {
                try {
                    in.close();
                } catch (IOException e) {

                }
            }
        }
        return null;
    }

    /**
     * Base64字符串转化为图片
     * @param imgCode base64字符串
     * @param imgFilePath 保存的路径
     * @return
     */
    public static boolean generateImage(String imgCode, String imgFilePath) {// 对字节数组字符串进行Base64解码并生成图片
        if (imgCode == null) // 图像数据为空
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        ByteArrayInputStream bais = null;
        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(imgCode);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            // 生成图片
//            OutputStream os = new FileOutputStream(imgFilePath);
//            os.write(b);
//            os.flush();
            bais = new ByteArrayInputStream(b);
//            BufferedImage bfi = ImageIO.read(bais);
//            File file = new File(imgFilePath);// 可以是jpg,png,gif格式
//            ImageIO.write(bfi, imgFilePath.substring(imgFilePath.lastIndexOf(".") + 1, imgFilePath.length()), file);// 不管输出什么格式图片，此处不需改动
            IOUtils.copy(bais, new FileOutputStream(imgFilePath));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            if(os != null) {
//                try {
//                    os.close();
//                } catch (IOException e) {
//
//                }
//            }
            if(bais != null) {
                try {
                	bais.close();
                } catch (IOException e) {

                }
            }
        }
        return false;
    }


    /**
     * 将网络图片进行Base64位编码
     *
     * @param urlStr 图片的url路径，如http://.....xx.jpg
     * @return
     */
    public static String encodeImgageToBase64(String urlStr) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        ByteArrayOutputStream outputStream = null;
        try {
            URL url = new URL(urlStr);
            BufferedImage bufferedImage = ImageIO.read(url);
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", outputStream);

            // 对字节数组Base64编码
            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encode(outputStream.toByteArray());// 返回Base64编码过的字节数组字符串
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
