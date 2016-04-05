package ai.eve.util;

import org.bouncycastle.util.encoders.Base64;

import java.security.SecureRandom;

/**
 * AES+RSA混合加密
 * 
 * @author Eve-Wyong
 * @version 2015-2-5 下午2:45:23
 * @Copyright 2014 EVE. All rights reserved.
 */
public final class ESecurity {

	/**
	 * AES随机秘钥每位允许的字符
	 */
	private static final String POSSIBLE_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	/**
	 * 生产一个指定长度的随机字符串 (数字和字母组成的随机数)
	 * 
	 * @param length
	 *            字符串长度 此处AES加密要求16位，需传参数16
	 * @return
	 */
	private static String generateRandomString(int length) {
		StringBuilder sb = new StringBuilder(length);
		SecureRandom random = new SecureRandom();
		for (int i = 0; i < length; i++) {
			sb.append(POSSIBLE_CHARS.charAt(random.nextInt(POSSIBLE_CHARS.length())));
		}
		return sb.toString();
	}

	/**
	 * 混合加密
	 * 
	 * @param data
	 *            数据
	 * 
	 * @return AES加密秘钥
	 * 
	 * @throws Exception
	 */
	public static String Encrypt(String data) {
		try {
			if (data == null || data.equals("")) {
				return null;
			}
			String send;
			// 1 生成随机码（范围以当前时间为准），作为AES加密的密钥
			String randomNumber = generateRandomString(16);
			// 2 获取RSA公钥
			String eRSAPublicKey = EKey.getInstance().rsaPublicKey;
			// 3 RSA加密随机码
			byte[] randomSeqCiper = null;
			randomSeqCiper = ERSA.EncryptByPublicKey(randomNumber.getBytes(), Base64.decode(eRSAPublicKey));
			// 4 AES加密data
			byte[] dataCiper = EAES.Encrypt(data.getBytes(), randomNumber);
			// 5 BASE64编码
			String dataCiper64 = new String(Base64.encode(dataCiper));
			String randomSeqCiper64 = new String(Base64.encode(randomSeqCiper));
			// 6 以逗号分隔返回RSA加密数据及data加密数据
			if (dataCiper64 != null && !dataCiper64.equals("")) {
				if (randomSeqCiper64 != null && !randomSeqCiper64.equals("")) {
					send = dataCiper64 + "," + randomSeqCiper64;
				} else {
					return null;
				}
			} else {
				if (randomSeqCiper64 != null && !randomSeqCiper64.equals("")) {
					return null;
				} else {
					return null;
				}
			}
			return send;
		} catch (Exception e) {
			ELog.E(e.getMessage());
			return null;
		}
	}

	/**
	 * 混合解密
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static String Dncrypt(String back){
		try {
			if (back != null && !back.equals("")) {
				String data[] = back.split(",");
				// 1 base64解码
				byte[] decDataCiper;
				decDataCiper = Base64.decode(data[0]);
				byte[] decRandomSeqCiper64 = Base64.decode(data[1]);
				// 2 获取RSA秘钥
				String eRSAPricateKey = EKey.getInstance().rsaPrivateKey;
				// 3 RSA解密随机码秘钥
				byte[] decRandomSeqByte = null;
				decRandomSeqByte = ERSA.DecryptByPrivateKey(decRandomSeqCiper64, Base64.decode(eRSAPricateKey));
				String decRandomSeq = new String(decRandomSeqByte);
				// 4 AES 通过密钥解密data
				byte[] decDataByte = EAES.Decrypt(decDataCiper,decRandomSeq);
				String decData = new String(decDataByte);
				return decData;
			} else {
				return null;
			}
		} catch (Exception e) {
			ELog.E(e.getMessage());
			return null;
		}
	}
}