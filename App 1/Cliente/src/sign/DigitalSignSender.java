package sign;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;

public class DigitalSignSender {
    private KeyPairGenerator kpg;
    private SecureRandom secureRan = new SecureRandom();
    private PublicKey publicKey;
    private PrivateKey privateKey;

    public DigitalSignSender() {
        kpg = getKeyPairGenerator();
        initializeKeyPair();
    }

    private KeyPairGenerator getKeyPairGenerator() {
        try {
            return KeyPairGenerator.getInstance("DSA");
        } catch (Exception err) {
            System.out.println("Falha ao instanciar DSA");
            return null;
        }
    }

    private void initializeKeyPair() {
        kpg.initialize(512, secureRan);

        KeyPair kp = kpg.generateKeyPair();

        publicKey = kp.getPublic();
        privateKey = kp.getPrivate();
    }

    public byte[] generateDigitalSign(String message) {
        try {
            Signature sig = Signature.getInstance("DSA");
            sig.initSign(privateKey);

            sig.update(message.getBytes());
            byte[] signature = sig.sign();
   
            return signature;
        } catch (Exception err) {
            System.out.println("Falha ao criar a assinatura");
            return null;
        }
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
