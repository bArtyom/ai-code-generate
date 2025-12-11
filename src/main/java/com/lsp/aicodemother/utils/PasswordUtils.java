package com.lsp.aicodemother.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtils {

    private static final String SALT="lsp_ai_code_mother_salt_2025";

    public static String getEncryptPassword(String userPassword){
        try {
            String raw=SALT+userPassword;
            MessageDigest md=MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(raw.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb=new StringBuilder();
            //防止显示乱码
            for(byte b:bytes){
                //8位到32位，防止负数自动补一，所以先0xff过滤，保证前面补零,再转16进制,才能获得原来的值
                //因为byte是-128-127，如果为全1，那么转成int就是-1，转16进制就是ffffffff，所以要和0xff与运算
                String hex = Integer.toHexString(0xff & b);
                if(hex.length()==1)sb.append('0');
                sb.append(hex);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
