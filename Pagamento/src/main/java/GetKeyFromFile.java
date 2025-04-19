import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;
import java.io.InputStream;
import java.io.FileNotFoundException;

public class GetKeyFromFile {

    public static PrivateKey loadPrivateKey(String path) throws Exception {
        InputStream is = GetKeyFromFile.class.getResourceAsStream(path);
        if (is == null) {
            throw new FileNotFoundException("Key file not found in resources!");
        }
        String keyPem = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        keyPem = keyPem
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] keyBytes = Base64.getDecoder().decode(keyPem);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        return KeyFactory.getInstance("RSA").generatePrivate(keySpec);
    }

    public static PublicKey loadPublicKey(String path) throws Exception {
        InputStream is = GetKeyFromFile.class.getResourceAsStream(path);
        if (is == null) {
            throw new FileNotFoundException("Key file not found in resources!");
        }
        String keyPem = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        keyPem = keyPem
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] keyBytes = Base64.getDecoder().decode(keyPem);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        return KeyFactory.getInstance("RSA").generatePublic(keySpec);
    }


}
