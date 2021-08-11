package com.jalen.android.scaffold.util.code;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSAUtil {
    private static RSAUtil rsa = new RSAUtil();

    public static Key genKey() {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(1024,new SecureRandom());
            KeyPair keyPair = keyPairGen.generateKeyPair();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            Key result = new Key();
            result.publicKey = Base64Util.encode(publicKey.getEncoded());
            result.privateKey = Base64Util.encode(privateKey.getEncoded());
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public static RSAPublicKey getPublicKey(String publicKeyStr) throws IllegalArgumentException {
        try {
            byte[] buffer = Base64Util.decode(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new IllegalArgumentException("公钥非法");
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("公钥数据为空");
        }
    }

    public static byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData) throws IllegalArgumentException {
        if (publicKey == null) {
            throw new IllegalArgumentException("加密公钥为空, 请设置");
        }
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(plainTextData);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("无此加密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("加密公钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new IllegalArgumentException("明文长度非法");
        } catch (BadPaddingException e) {
            throw new IllegalArgumentException("明文数据已损坏");
        }
    }

    public static byte[] decrypt(RSAPublicKey publicKey, byte[] cipherData) throws IllegalArgumentException {
        if (publicKey == null) {
            throw new IllegalArgumentException("解密公钥为空, 请设置");
        }
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return cipher.doFinal(cipherData);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("无此解密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("解密公钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new IllegalArgumentException("密文长度非法");
        } catch (BadPaddingException e) {
            throw new IllegalArgumentException("密文数据已损坏");
        }
    }

    public static RSAPrivateKey getPrivateKey(String privateKeyStr) throws IllegalArgumentException {
        try {
            byte[] buffer = Base64Util.decode(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new IllegalArgumentException("私钥非法");
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("私钥数据为空");
        }
    }

    public static byte[] encrypt(RSAPrivateKey privateKey, byte[] plainTextData) throws IllegalArgumentException {
        if (privateKey == null) {
            throw new IllegalArgumentException("加密私钥为空, 请设置");
        }
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return cipher.doFinal(plainTextData);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("无此加密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("加密私钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new IllegalArgumentException("明文长度非法");
        } catch (BadPaddingException e) {
            throw new IllegalArgumentException("明文数据已损坏");
        }
    }

    public static byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData) throws IllegalArgumentException {
        if (privateKey == null) {
            throw new IllegalArgumentException("解密私钥为空, 请设置");
        }
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(cipherData);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("无此解密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("解密私钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new IllegalArgumentException("密文长度非法");
        } catch (BadPaddingException e) {
            throw new IllegalArgumentException("密文数据已损坏");
        }
    }

    static class Key {
        private String privateKey;
        private String publicKey;

        public String getPrivateKey() {
            return privateKey;
        }

        public String getPublicKey() {
            return publicKey;
        }
    }
}
