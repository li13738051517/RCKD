package com.rckd.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @ author 钱志鹏
 */
public class MD5
{
    public static final String ENCODE = "UTF-8";  //GBK
    public static String hmacSign(String aValue, String aKey)
    {
        byte k_ipad[] = new byte[64];
        byte k_opad[] = new byte[64];
        byte keyb[];
        byte value[];
        try{
            keyb = aKey.getBytes(ENCODE);
            value = aValue.getBytes(ENCODE);
        }
        catch(UnsupportedEncodingException e){
            keyb = aKey.getBytes();
            value = aValue.getBytes();
        }
        Arrays.fill(k_ipad, keyb.length, 64, (byte)54);
        Arrays.fill(k_opad, keyb.length, 64, (byte)92);
        for(int i = 0; i < keyb.length; i++){
            k_ipad[i] = (byte)(keyb[i] ^ 0x36);
            k_opad[i] = (byte)(keyb[i] ^ 0x5c);
        }

        MessageDigest md = null;
        try{
            md = MessageDigest.getInstance("MD5");
        }
        catch(NoSuchAlgorithmException e){
         e.printStackTrace();
            return null;
        }
        md.update(k_ipad);
        md.update(value);
        byte dg[] = md.digest();
        md.reset();
        md.update(k_opad);
        md.update(dg, 0, 16);
        dg = md.digest();//
        return MD5.toHex(dg);
    }

    public static String digest(String aValue) {
        aValue = aValue.trim();
        byte value[];
        try {
            value = aValue.getBytes(ENCODE);
        }
        catch(UnsupportedEncodingException e){
            value = aValue.getBytes();
        }
        MessageDigest md = null;
        try{
            md = MessageDigest.getInstance("SHA");
        }
        catch(NoSuchAlgorithmException e) {
         e.printStackTrace();
            return null;
        }
        return MD5.toHex(md.digest(value));
    }
    
    public static String toHex(byte input[]){
        if(input == null)
            return null;
        StringBuffer output = new StringBuffer(input.length * 2);
        for(int i = 0; i < input.length; i++){
            int current = input[i] & 0xff;
            if(current < 16)
                output.append("0");
            output.append(Integer.toString(current, 16));
        }

        return output.toString();
    }
    
   /* public static void main( String[] args ){
    	String inputStr = "nimeide";
    	inputStr = inputStr.toUpperCase();
    	System.out.println(inputStr);
    	String md5edStr = MD5.hmacSign(inputStr,"sdfkljfkadnmkgfzdmdfkagjkladfg");
    	System.out.println( md5edStr );
    }*/


//    public static String sortRequest(Map<String, String> map){
//        List<String> list = new ArrayList<String>();
//        String nihao="";
//        Iterator it = map.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry e = (Map.Entry) it.next();
//            list.add((String) e.getKey());
//        }
//        Collections.sort(list);
//        for (int i = 0; i < list.size(); i++) {
//            nihao = nihao+list.get(i)+map.get(list.get(i));
//        }
//        return nihao;
//    }


    public static String sortRequest(Map<String, Object> map){
        List<String> list = new ArrayList<String>();
        String nihao="";
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();
            list.add((String) e.getKey());
        }
        Collections.sort(list);
        for (int i = 0; i < list.size(); i++) {
            nihao = nihao+list.get(i)+map.get(list.get(i));
        }
        return nihao;
    }





}
