package sign;

import java.security.PublicKey;
import java.security.Signature;

public class DigitalSignReceiver {

    public void getExternalMessage(PublicKey pubKey, String message, byte[] signature) {
        try {
            Signature clientSig = Signature.getInstance("DSA");
            clientSig.initVerify(pubKey);
            clientSig.update(message.getBytes());

            if (clientSig.verify(signature)) {
                // Mensagem corretamente assinada
                System.out.println("A Mensagem recebida foi assinada corretamente.");
            } else {
                // Mensagem não pode ser validada
                System.out.println("A Mensagem recebida NÃO pode ser validada.");
            }
        } catch (Exception err) {
            System.out.println("A Mensagem recebida NÃO pode ser validada.");
        }
    }
}
