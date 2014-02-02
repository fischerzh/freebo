package ch.mobileking.nfcreceipt;

import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.*;
import java.security.interfaces.*;
import java.security.spec.*;
import java.util.Properties;
import org.apache.commons.codec.binary.Base64;
/**
 *
 * @author csc
 */
public class CryptoTool {
    public static KeyPair genKeyPair(int keylength) throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        keyGen.initialize(keylength, random);
        KeyPair keyPair = keyGen.genKeyPair();
        return keyPair;
    }

    public static PrivateKey loadPrivateKeyFromFile(String filename) throws Exception {
        File f = new File(filename);
        if(!f.exists())
            throw new Exception("File not exist");
        FileInputStream in = new FileInputStream(f);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        
        byte[] data= new byte[2048];
        int len;
        while((len=in.read(data,0,2048))>0) {
            bao.write(data, 0, len);
        }
        String privatekey = bao.toString();
        Base64 base64 = new Base64();
        byte[] encodedPrivateKey = base64.decode(privatekey.getBytes());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec); 
        return privateKey;
    }    

    
    public static PublicKey loadPublicKeyFromFile(String filename) throws Exception {
        File f = new File(filename);
        if(!f.exists())
            throw new Exception("File not exist");
        FileInputStream in = new FileInputStream(f);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        
        byte[] data= new byte[2048];
        int len;
        while((len=in.read(data,0,2048))>0) {
            bao.write(data, 0, len);
        }
        String publickey = bao.toString();
        Base64 base64 = new Base64();
        byte[] encodedPublicKey = base64.decode(publickey.getBytes());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
        return publicKey;
    }    
    
    public static void savePublicKeyToFile(PublicKey pubKey, String filename) throws Exception {
        File f = new File(filename);
        Properties prop = new Properties();        
        Base64 base64 = new Base64();
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(pubKey.getEncoded());
        byte[] pkb = x509EncodedKeySpec.getEncoded();
        String encodeString = new String(base64.encode(pkb));
        FileOutputStream out = new FileOutputStream(f,false);
        out.write(encodeString.getBytes());
        out.close();
    }
    
    public static void savePrivateKeyToFile(PrivateKey priKey, String filename) throws Exception {
        File f = new File(filename);
        Properties prop = new Properties();        
        Base64 base64 = new Base64();
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(priKey.getEncoded());
        byte[] prikb = pkcs8EncodedKeySpec.getEncoded();
        String encodeString = new String(base64.encode(prikb));
        FileOutputStream out = new FileOutputStream(f,false);
        out.write(encodeString.getBytes());
        out.close();
    }

    
    public static void saveKeyPair(KeyPair keyPair, String filename) throws Exception {
        File f = new File(filename);
        Properties prop = new Properties();
        PrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();
        PublicKey publicKey = (RSAPublicKey)keyPair.getPublic();        
        Base64 base64 = new Base64();
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
        byte[] pkb = x509EncodedKeySpec.getEncoded();
        String encodeString = new String(base64.encode(pkb));
        prop.put("publickey",encodeString);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
        byte[] prikb = pkcs8EncodedKeySpec.getEncoded();
        encodeString = new String(base64.encode(prikb));
        prop.put("privatekey",encodeString);
        prop.store(new FileWriter(f,false), "keys");
    }
    
    public static String getPublicKeyString(PublicKey publicKey) throws Exception {
        Base64 base64 = new Base64();
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
        byte[] pkb = x509EncodedKeySpec.getEncoded();
        String encodeString = new String(base64.encode(pkb));
        return encodeString;
    }

    public static PublicKey getPublicKeyFromString(String publickey) throws Exception {
        Base64 base64 = new Base64();
        byte[] encodedPublicKey = base64.decode(publickey.getBytes());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
        return publicKey;        
    } 
    
    
    public static KeyPair loadKeyPair(String filename) throws Exception {
        File f = new File(filename);
        if(!f.exists())
            throw new Exception("File not exist");
        Properties prop = new Properties();
        prop.load(new FileReader(f));
        String publickey = prop.getProperty("publickey");
        String privatekey = prop.getProperty("privatekey");
        Base64 base64 = new Base64();
        byte[] encodedPublicKey = base64.decode(publickey.getBytes());
        byte[] encodedPrivateKey = base64.decode(privatekey.getBytes());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec); 
        KeyPair keyPair = new KeyPair(publicKey, privateKey);
        return keyPair;
    }
    
    public static byte[] sign(PrivateKey pk, byte[] texttosign, String algorithm) throws Exception{
        Signature sig = Signature.getInstance(algorithm);
        sig.initSign(pk);
        sig.update(texttosign);
        byte[] sigb = sig.sign();
        return sigb;
    }    

    public static boolean verify(PublicKey pubkey, byte[] texttosign, byte[] signature, String algorithm) throws Exception {
        Signature sig = Signature.getInstance(algorithm);
        sig.initVerify(pubkey);    
        sig.update(texttosign); 
        boolean ret = sig.verify(signature);
        return ret;
    }

}
