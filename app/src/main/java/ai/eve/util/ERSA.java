package ai.eve.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

/**
 * <p>
 * 加密算法RSA的工具类
 * </p>
 *
 * @author hanym
 * @version 2015-12-11 下午15:33:06
 * @Copyright 2015 EVE. All rights reserved.
 */
public class ERSA {
    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";

    /**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;


    /**
     * 生成密钥对(公钥和私钥)
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Object> genKeyPair() {
		try {
			KeyPairGenerator keyPairGen = null;
			keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
			keyPairGen.initialize(1024);
			KeyPair keyPair = keyPairGen.generateKeyPair();
			RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
			RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
			Map<String, Object> keyMap = new HashMap<String, Object>(2);
			keyMap.put(PUBLIC_KEY, publicKey);
			keyMap.put(PRIVATE_KEY, privateKey);
			return keyMap;
		} catch (NoSuchAlgorithmException e) {
			ELog.E(e.getMessage());
			return null;
		}
    }

    /**
     * 私钥解密
     *
     * @param encryptedData 已加密数据(非BASE64编码)
     * @param privateKey    私钥(非BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] DecryptByPrivateKey(byte[] encryptedData, byte[] privateKey) {
    	ByteArrayOutputStream out =null;
		try {
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
			KeyFactory keyFactory = null;
			keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			Key privateK = null;
			privateK = keyFactory.generatePrivate(pkcs8KeySpec);
			Cipher cipher = null;
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding",new BouncyCastleProvider());
			cipher.init(Cipher.DECRYPT_MODE, privateK);
			int inputLen = encryptedData.length;
			out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] cache = new byte[] {};
			int i = 0;
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
					cache = cipher.doFinal(encryptedData, offSet,MAX_DECRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * MAX_DECRYPT_BLOCK;
			}
			byte[] decryptedData = out.toByteArray();
			
			return decryptedData;
		} catch (Exception e) {
			ELog.E(e.getMessage());
			return null;
		} finally{
			try {
				out.close();
			} catch (Exception e) {
			}
		}
	}

    /**
     * 公钥解密
     *
     * @param encryptedData 已加密数据(非BASE64编码)
     * @param publicKey     公钥(非BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] DecryptByPublicKey(byte[] encryptedData, byte[] publicKey) {
    	ByteArrayOutputStream out =null;
		try {
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
			KeyFactory keyFactory = null;
			keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			Key publicK = null;
			publicK = keyFactory.generatePublic(x509KeySpec);
			Cipher cipher = null;
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, publicK);
			int inputLen = encryptedData.length;
			out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] cache = new byte[] {};
			int i = 0;
			// 对数据分段解密
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
					cache = cipher.doFinal(encryptedData, offSet,MAX_DECRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * MAX_DECRYPT_BLOCK;
			}
			byte[] decryptedData = out.toByteArray();
			return decryptedData;
		} catch (Exception e) {
			ELog.E(e.getMessage());
			return null;
		} finally{
			try {
				out.close();
			} catch (Exception e) {
			}
		}
	}

    /**
     * 公钥加密
     *
     * @param data      源数据(非BASE64编码)
     * @param publicKey 公钥(非BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] EncryptByPublicKey(byte[] data, byte[] publicKey) {
    	ByteArrayOutputStream out =null;
		try {
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
			KeyFactory keyFactory = null;
			keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			Key publicK = null;
			publicK = keyFactory.generatePublic(x509KeySpec);
			// 对数据加密
			Cipher cipher = null;
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, publicK);
			int inputLen = data.length;
			out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] cache = new byte[] {};
			int i = 0;
			// 对数据分段加密
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
					cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(data, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * MAX_ENCRYPT_BLOCK;
			}
			byte[] encryptedData = out.toByteArray();
			return encryptedData;
		} catch (Exception e) {
			ELog.E(e.getMessage());
			return null;
		} finally{
			try {
				out.close();
			} catch (Exception e) {
			}
		}
    }

    /**
     * 私钥加密
     *
     * @param data       源数据(非BASE64编码)
     * @param privateKey 私钥(非BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] EncryptByPrivateKey(byte[] data, byte[] privateKey) {
    	ByteArrayOutputStream out =null;
		try {
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
			KeyFactory keyFactory = null;
			keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			Key privateK = null;
			privateK = keyFactory.generatePrivate(pkcs8KeySpec);
			Cipher cipher = null;
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding",new BouncyCastleProvider());
			cipher.init(Cipher.ENCRYPT_MODE, privateK);
			int inputLen = data.length;
			out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] cache = new byte[] {};
			int i = 0;
			// 对数据分段加密
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
					cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(data, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * MAX_ENCRYPT_BLOCK;
			}
			byte[] encryptedData = out.toByteArray();
			return encryptedData;
		} catch (Exception e) {
			ELog.E(e.getMessage());
			return null;
		} finally{
			try {
				out.close();
			} catch (Exception e) {
			}
		}
    }

    /**
     * 获取私钥
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static byte[] GetPrivateKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return key.getEncoded();
    }

    /**
     * 获取公钥
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static byte[] GetPublicKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return key.getEncoded();
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       已加密数据(非BASE64编码)
     * @param privateKey 私钥(非BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] Sign(byte[] data, byte[] privateKey) {
		try {
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
			KeyFactory keyFactory = null;
			keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			PrivateKey privateK = null;
			privateK = keyFactory.generatePrivate(pkcs8KeySpec);
			Signature signature = null;
			signature = Signature.getInstance(SIGNATURE_ALGORITHM);
			signature.initSign(privateK);
			signature.update(data);
			return signature.sign();
		} catch (Exception e) {
			ELog.E(e.getMessage());
			return null;
		}
    }

    /**
     * 校验数字签名
     *
     * @param data      已加密数据(非BASE64编码)
     * @param publicKey 公钥(非BASE64编码)
     * @param sign      数字签名
     * @return
     * @throws Exception
     */
    public static boolean Verify(byte[] data, byte[] publicKey, byte[] sign) {
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
		KeyFactory keyFactory = null;
		try {
			keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			PublicKey publicK = null;
			publicK = keyFactory.generatePublic(keySpec);
			Signature signature = null;
			signature = Signature.getInstance(SIGNATURE_ALGORITHM);
			signature.initVerify(publicK);
			signature.update(data);
			return signature.verify(sign);
		} catch (Exception e) {
			ELog.E(e.getMessage());
			return false;
		}
    }

}
