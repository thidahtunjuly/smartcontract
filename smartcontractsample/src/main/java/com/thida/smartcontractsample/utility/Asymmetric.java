package com.thida.smartcontractsample.utility;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.util.Base64;

public class Asymmetric {
	public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048, new SecureRandom());
        KeyPair pair = generator.generateKeyPair();

        return pair;
    }
	
	 public static String sign(String plainText, PrivateKey privateKey) throws Exception {
	        Signature privateSignature = Signature.getInstance("SHA256withRSA");
	        privateSignature.initSign(privateKey);
	        privateSignature.update(plainText.getBytes(UTF_8));

	        byte[] signature = privateSignature.sign();

	        return Base64.getEncoder().encodeToString(signature);
	 }
	 
	 public static boolean verify(String plainText, String signature, PublicKey publicKey) throws Exception {
	        Signature publicSignature = Signature.getInstance("SHA256withRSA");
	        publicSignature.initVerify(publicKey);
	        publicSignature.update(plainText.getBytes(UTF_8));

	        byte[] signatureBytes = Base64.getDecoder().decode(signature);

	        return publicSignature.verify(signatureBytes);
	 }
	 
	 public static String getPublicKey() throws Exception {
		 KeyPair pair = Asymmetric.generateKeyPair();
		 String publicKey=Base64.getEncoder().encodeToString(pair.getPublic().getEncoded());
		 
		 return publicKey; 
	 }
	 
	 public static String getPrivateKey() throws Exception {
		 KeyPair pair = Asymmetric.generateKeyPair();
		 String privateKey=Base64.getEncoder().encodeToString(pair.getPrivate().getEncoded());
		 
		 return privateKey; 
	 }
}
