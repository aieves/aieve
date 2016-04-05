package ai.eve.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Key;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EAES {
	private final static String iv = "1234567890123456";
	/**
	 * 加密
	 * 
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public static byte[] Encrypt(byte[] content, String password){
		try {
			Security.addProvider(new BouncyCastleProvider());
			Key key = new SecretKeySpec(password.getBytes(), "AES");
			Cipher in = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
			in.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv.getBytes()));
			byte[] enc = in.doFinal(content);
			return enc;
		}catch (Exception e) {
			ELog.E(e.getMessage());
			return null;
		}
	}

	/**
	 * 解密
	 * 
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public static byte[] Decrypt(byte[] content, String password){
		try {
			Security.addProvider(new BouncyCastleProvider());
			Key key = new SecretKeySpec(password.getBytes(), "AES");
			Cipher out = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
			out.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv.getBytes()));
			byte[] dec = out.doFinal(content);
			return dec;
		}catch (Exception e) {
			ELog.E(e.getMessage());
			return null;
		}
	}
}
