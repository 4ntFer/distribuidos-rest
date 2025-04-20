import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;
import java.io.InputStream;

public abstract class GetKeyFromFile {

    public static PrivateKey loadPrivateKey(String path){

        try(InputStream is = GetKeyFromFile.class.getResourceAsStream(path);){

            String keyPem = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            keyPem = keyPem
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] keyBytes = Base64.getDecoder().decode(keyPem);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            return KeyFactory.getInstance("RSA").generatePrivate(keySpec);

        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public static PublicKey loadPublicKey(String path){

        try(InputStream is = GetKeyFromFile.class.getResourceAsStream(path);) {
            String keyPem = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            keyPem = keyPem
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] keyBytes = Base64.getDecoder().decode(keyPem);
            X509EncodedKeySpec  keySpec = new X509EncodedKeySpec(keyBytes);
            return KeyFactory.getInstance("RSA").generatePublic(keySpec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }


}