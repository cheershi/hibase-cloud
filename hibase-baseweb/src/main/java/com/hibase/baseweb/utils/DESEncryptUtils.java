package com.hibase.baseweb.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


/**
 * 加密解密
 * 
 * @author qinjilin
 * 
 */
public class DESEncryptUtils {

	private static Logger LOGGER = LoggerFactory.getLogger(DESEncryptUtils.class);
	
	public static String REALPATH_CRYPT_KEY = "gzllrqc/MKHKO(F<>";			// 当没有传入key的时候，默认使用该key

	public static String DES = "DES";
	
	 public static final String AES_KEY = "AES";
	 
	 public static final String BASE_KEY = "][poiuytrewq";//KEY

	private static byte[] encrypt(byte[] src, byte[] key) throws Exception {
		// DES算法要求有一个可信任的随机数
		SecureRandom sr = new SecureRandom();
		// 从原始密匙数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);
		// 创建一个密匙工厂，然后用它把DESKeySpec转换
		// 一个SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);
		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance(DES);
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
		// 现在，获取数据并加密
		// 正式执行加密操作
		return cipher.doFinal(src);
	}

	private static byte[] decrypt(byte[] src, byte[] key) throws Exception {
		// DES算法要求有一个可信任的随机数
		SecureRandom sr = new SecureRandom();
		// 从原始密匙数据创建一个DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);
		// 创建一个密匙工厂，然后用它把DESKeySpec对象转换
		// 一个SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);
		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance(DES);
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
		// 现在，获取数据并解密
		// 正式执行解密操作
		return cipher.doFinal(src);
	}
	
	private static String byte2hex(byte[] b) {

		String hs = "";
		String stmp = "";

		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}
		return hs.toUpperCase();
	}

	private static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0) {
			throw new IllegalArgumentException("长度不是偶数");
		}
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}

	/**
	 * 密码解密
	 * 
	 * @param data	需要解密的String
	 * @param key   加密时候所用的key
	 * @return	解密之后的原数据
	 * @throws Exception
	 */
	public final static String decrypt(String data, String key) throws Exception {
		try {
			return new String(decrypt(hex2byte(data.getBytes()),
					key == null ? REALPATH_CRYPT_KEY.getBytes() : key.getBytes()));
		} catch (Exception e) {

			throw e;
		}
	}

	/**
	 * 密码加密
	 * 
	 * @param src
	 * @return
	 * @throws Exception
	 */
	public final static String encrypt(String src, String key) throws Exception {
		try {
			return byte2hex(encrypt(src.getBytes(),
					key == null ? REALPATH_CRYPT_KEY.getBytes() : key.getBytes()));
		} catch (Exception e) {

			throw e;
		}
	}
	
	
	    /**
	     * 利用AES加密key
	     *
	     * @param key
	     * @return 返回加密后的key
	     */
	    public static String encodeAndAesEncrypt(String key) throws Exception {
	        return parseByte2HexStr(aesEncrypt(key, AES_KEY));
	    }

	    /**
	     * AES解密加密后的key
	     *
	     * @param secretKey 加密的key
	     * @return 返回解密后的key
	     */
	    public static String decodeAndAesDecrypt(String secretKey) throws Exception {
	        byte[] cardbyte = parseHexStr2Byte(secretKey);

	        return new String(aesDecrypt(cardbyte, AES_KEY));
	    }
	    /**
	    /**
	     * 加密
	     *
	     * @param content  需要加密的内容
	     * @param password 加密密码
	     * @return
	     */
	    public static byte[] aesEncrypt(String content, String password) throws Exception {
	        try {
	            KeyGenerator kgen = KeyGenerator.getInstance("AES");

	            // kgen.init(128, new SecureRandom(password.getBytes()));
	            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
	            secureRandom.setSeed(password.getBytes());
	            kgen.init(128, secureRandom);

	            SecretKey secretKey = kgen.generateKey();
	            byte[] enCodeFormat = secretKey.getEncoded();
	            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
	            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
	            byte[] byteContent = content.getBytes("utf-8");
	            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
	            byte[] result = cipher.doFinal(byteContent);
	            return result; // 加密
	        } catch (NoSuchAlgorithmException e) {

	        	throw e;
	        }
	    }

	    /**
	     * 解密
	     *
	     * @param content  待解密内容
	     * @param password 解密密钥
	     * @return
	     */
		public static byte[] aesDecrypt(byte[] content, String password) throws Exception {
			try {
				KeyGenerator kgen = KeyGenerator.getInstance("AES");
				//kgen.init(128, new SecureRandom(password.getBytes()));
				//防止linux下 随机生成key
				SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
				secureRandom.setSeed(password.getBytes());
				kgen.init(128, secureRandom);

				SecretKey secretKey = kgen.generateKey();
				byte[] enCodeFormat = secretKey.getEncoded();
				SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
				Cipher cipher = Cipher.getInstance("AES");// 创建密码器
				cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
				byte[] result = cipher.doFinal(content);
				return result; // 加密
			} catch (NoSuchAlgorithmException e) {

				throw e;
			}
		}

	    /**
	     * 将二进制转换成16进制
	     *
	     * @param buf
	     * @return
	     */
	    public static String parseByte2HexStr(byte buf[]) {
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < buf.length; i++) {
	            String hex = Integer.toHexString(buf[i] & 0xFF);
	            if (hex.length() == 1) {
	                hex = '0' + hex;
	            }
	            sb.append(hex.toUpperCase());
	        }
	        return sb.toString();
	    }

	    /**
	     * 将16进制转换为二进制
	     *
	     * @param hexStr
	     * @return
	     */
	    public static byte[] parseHexStr2Byte(String hexStr) {
	        if (hexStr.length() < 1)
	            return null;
	        byte[] result = new byte[hexStr.length() / 2];
	        for (int i = 0; i < hexStr.length() / 2; i++) {
	            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
	            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
	            result[i] = (byte) (high * 16 + low);
	        }
	        return result;
	    }

	    public static byte convertHexDigit(byte b) {
	        if ((b >= 48) && (b <= 57)) return (byte) (b - 48);
	        if ((b >= 97) && (b <= 102)) return (byte) (b - 97 + 10);
	        if ((b >= 65) && (b <= 70)) return (byte) (b - 65 + 10);
	        return 0;
	    }
}
