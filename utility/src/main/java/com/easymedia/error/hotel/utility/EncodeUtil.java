package com.easymedia.error.hotel.utility;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncodeUtil
{
    public static String encode(String strEncode)
    {
        String rtnEncode = "";
        try
        {
            String toBeHashed = strEncode;
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] bytes = md.digest(toBeHashed.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            rtnEncode = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return rtnEncode;
    }

    /**
     * 비밀번호를 암호화하는 기능(복호화가 되면 안되므로 SHA-256 인코딩 방식 적용)
     *
     * @param password 암호화될 패스워드
     * @param id salt로 사용될 사용자 ID 지정
     * @return
     * @throws Exception
     */
    public static String encryptPassword(String password, String id) throws Exception
    {
        if (password == null)
        {
            return "";
        }

        byte[] hashValue = null; // 해쉬값

        MessageDigest md = MessageDigest.getInstance("SHA-256");

        md.reset();
        md.update(id.getBytes());

        hashValue = md.digest(password.getBytes());

        return new String(Base64.encodeBase64(hashValue));
    }
}
