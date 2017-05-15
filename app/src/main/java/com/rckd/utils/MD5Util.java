package com.rckd.utils;

import java.security.MessageDigest;

/**
 * Created by LiZheng on 2017/5/5 0005.
 * 后续完善图形32微码
 * 同时可考虑加密,解密协议算法
 */
public class MD5Util {
    private static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};//16进制加密,
    public static final String ENCODEING = "UTF-8";
    /**
     * @param s ---s : input
     * @return
     */
    public static String getMD5String(String s) {

        try {
            byte[] btInput = s.getBytes(ENCODEING);
            //获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            //使用指定的字节更新摘要
            mdInst.update(btInput);
            //获得密文
            byte[] md = mdInst.digest();
            //把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    /**
     * 返回字符串的16位MD5值
     * (通常是中间的16位)
     *
     * @param s 字符串
     * @return str MD5值
     */
    public final static String getStringD5_16(String s) {
        return getMD5String(s).substring(8, 24);
    }



//    public static void main(String[] args) {
//        String sign = "asdfgbc"; //任何需要被加密的字符串
//        String secretSign = MD5Util.getMD5String(sign);
//        String secretSign_16 = MD5Util.getStringD5_16(sign);
//        System.out.println(secretSign);//
//        System.out.println(secretSign_16);
//    }

}
