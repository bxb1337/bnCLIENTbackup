package net.AzureWare.utils;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class StringUtil {
	
	private static final String KEY = "KillYourMotherIfYouLeakedSomeThing";
	
    public static String encrypt(String content) {
    	/*
    	content = Base64.encodeBase64String(content.getBytes());
    	content = content.replace("A", "尴").replace("B", "尬").replace("C", "死").replace("D", "妈").replace("E", "傻").replace("F", "逼").replace("G", "杂").replace("/", "种").replace("=", "火葬场");
    	content = enCryptAndEncode(content);
    	*/
		return content;    
		
    }
    
    public static String decrypt(String content) {
    	/*
    	try {
			content = deCryptAndDecode(content);
		} catch (Exception e) {
			//e.printStackTrace();
		}
    	content = new String(Base64.decodeBase64(content.replace("尴", "A").replace("尬", "B").replace("死", "C").replace("妈", "D").replace("傻", "E").replace("逼", "F").replace("杂", "G").replace("种", "/").replace("火葬场", "=")));
		*/
		return content;    
    }
    

    public static String enCryptAndEncode(String content) {
        try {
            byte[] sourceBytes = enCryptAndEncode(content, KEY);
            return Base64.encodeBase64URLSafeString(sourceBytes);
        } catch (Exception e) {
            return content;
        }
    }
 
    /**
     * 加密函数
     *
     * @param content 加密的内容
     * @param strKey  密钥
     * @return 返回二进制字符数组
     * @throws Exception
     */
    public static byte[] enCryptAndEncode(String content, String strKey) throws Exception {
 
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128, new SecureRandom(strKey.getBytes()));
 
        SecretKey desKey = keyGenerator.generateKey();
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, desKey);
        return cipher.doFinal(content.getBytes("UTF-8"));
    }
 
    public static String deCryptAndDecode(String content) throws Exception {
        byte[] targetBytes = Base64.decodeBase64(content);
        return deCryptAndDecode(targetBytes, KEY);
    }
 
 
    /**
     * 解密函数
     *
     * @param src    加密过的二进制字符数组
     * @param strKey 密钥
     * @return
     * @throws Exception
     */
    public static String deCryptAndDecode(byte[] src, String strKey) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128, new SecureRandom(strKey.getBytes()));
 
        SecretKey desKey = keyGenerator.generateKey();
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, desKey);
        byte[] cByte = cipher.doFinal(src);
        return new String(cByte, "UTF-8");
    }

}
