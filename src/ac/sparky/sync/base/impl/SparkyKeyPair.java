package ac.sparky.sync.base.impl;

import ac.sparky.sync.base.type.PluginType;
import ac.sparky.sync.bungee.MainBungee;
import ac.sparky.sync.spigot.MainSpigot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class SparkyKeyPair {

    private File publicKeyFile, privateKeyFile;
    private KeyPair pair;

    public SparkyKeyPair(PluginType type) {
        if(type == PluginType.BUNGEE) {
            publicKeyFile = new File(MainBungee.getInstance().getDataFolder() + "public-key.skp");
            privateKeyFile = new File(MainBungee.getInstance().getDataFolder() + "private-key.skp");
        } else if(type == PluginType.SPIGOT) {
            publicKeyFile = new File(MainSpigot.getInstance().getDataFolder() + "public-key.skp");
            privateKeyFile = new File(MainSpigot.getInstance().getDataFolder() + "private-key.skp");
        }
        try {
            if(!publicKeyFile.exists() && !privateKeyFile.exists()) {
                publicKeyFile.createNewFile();
                privateKeyFile.createNewFile();
                KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DSA");
                keyPairGen.initialize(2048);
                pair = keyPairGen.generateKeyPair();
                PublicKey publicKey = pair.getPublic();
                PrivateKey privateKey = pair.getPrivate();
                // save public key
                X509EncodedKeySpec publicEncoder = new X509EncodedKeySpec(publicKey.getEncoded());
                FileOutputStream publicOs = new FileOutputStream(publicKeyFile.getPath());
                publicOs.write(publicEncoder.getEncoded());
                publicOs.close();
                // save private key
                PKCS8EncodedKeySpec privateEncoder = new PKCS8EncodedKeySpec(privateKey.getEncoded());
                FileOutputStream privateOs = new FileOutputStream(privateKeyFile.getPath());
                privateOs.write(privateEncoder.getEncoded());
                privateOs.close();
            } else {
                // load public key
                FileInputStream publicIs = new FileInputStream(publicKeyFile.getPath());
                byte[] encodedPublicKey = new byte[(int) publicKeyFile.length()];
                publicIs.read(encodedPublicKey);
                publicIs.close();
                // load private key
                FileInputStream privateIs = new FileInputStream(privateKeyFile.getPath());
                byte[] encodedPrivateKey = new byte[(int) privateKeyFile.length()];
                privateIs.read(encodedPrivateKey);
                privateIs.close();
                // generate keypair
                KeyFactory keyFactory = KeyFactory.getInstance("DSA");
                X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
                PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
                PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
                PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
                pair = new KeyPair(publicKey, privateKey);
            }
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException exception) {
            System.out.println("[SparkyBungee] could not load keypair");
        }
    }

    public KeyPair getKeyPair() {
        return pair;
    }

    public String getHexString(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

}
