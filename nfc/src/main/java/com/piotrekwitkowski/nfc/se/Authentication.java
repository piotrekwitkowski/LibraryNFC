package com.piotrekwitkowski.nfc.se;

import com.piotrekwitkowski.log.Log;
import com.piotrekwitkowski.nfc.ByteUtils;
import com.piotrekwitkowski.nfc.desfire.AESKey;

import java.io.ByteArrayOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

class Authentication {
    private static final String TAG = "ApplicationAuthentication";
    private final Application application;
    private AESKey key;

    private final Cipher cipher;
    private final SecretKeySpec aes;
    private byte[] randomBytes;
    private byte[] challenge;


    Authentication(Application application) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.application = application;
        this.key = this.application.getKey0();

        this.cipher = Cipher.getInstance("AES/CBC/NoPadding");
        this.aes = new SecretKeySpec(application.getKey0().getKey(), "AES");
    }

    byte[] initiate(byte keyNumber) throws InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchKeyException {
        // 1. The reader asked for AES authentication for a specific key.
        if (keyNumber != 0) {
            throw new NoSuchKeyException();
        }

        // 2. The card creates a 16 byte random number (B) and encrypts it with the selected AES
        // key. The result is sent to the reader.
        this.randomBytes = ByteUtils.getRandomBytes(16);
        Log.i(TAG, "random bytes: " + ByteUtils.toHexString(randomBytes));
        IvParameterSpec ivParam = new IvParameterSpec(new byte[key.getKey().length]);
        cipher.init(Cipher.ENCRYPT_MODE, aes, ivParam);
        this.challenge = cipher.doFinal(this.randomBytes);
        return challenge;
    }

    AuthenticationResponse proceed(byte[] readerChallenge) throws InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, AuthenticationException {
        // 3. The reader receives the 16 bytes, and decrypts it using the AES key to get back the
        // original 16 byte random number (B). This is decrypted with an IV of all 00 bytes.
        // 4. The reader generates its own 16 byte random number (A).
        // 5. The reader rotates B one byte to the left.
        // 6. The reader concatenates A and the rotated B together to make a 32 byte value C.
        // 7. The reader encrypts the 32 byte value C with the AES key and sends D to the card. The
        // IV for encrypting this is the 16 bytes received from the card (i.e. before decryption).
        // 8. The card receives the 32 byte value D and decrypts it with the AES key.
        IvParameterSpec ivParam = new IvParameterSpec(this.challenge);
        cipher.init(Cipher.DECRYPT_MODE, aes, ivParam);
        byte[] C = cipher.doFinal(readerChallenge);
        Log.i(TAG, "from Reader: " + ByteUtils.toHexString(C));

        // 9. The card checks the second 16 bytes of C match the original random number B (rotated one
        // byte left). If this fails the authentication has failed. If it matches, the card knows
        // the reader has the right key.
        if (!Arrays.equals(ByteUtils.last16Bytes(C), ByteUtils.rotateOneLeft(this.randomBytes))) {
            throw new AuthenticationException();
        }

        // 10. The card rotates the first 16 bytes (A) left by one byte.
        byte[] A = ByteUtils.first16Bytes(C);
        byte[] rotatedA = ByteUtils.rotateOneLeft(A);

        // 11. The card encrypts this rotated A using the AES key and sends it to the reader.
        // 12. The reader receives the 16 bytes and decrypts it. The IV for this is the last 16
        // bytes the reader sent to the card.
        ivParam = new IvParameterSpec(ByteUtils.last16Bytes(readerChallenge));
        cipher.init(Cipher.ENCRYPT_MODE, aes, ivParam);
        byte[] encryptedRotatedA = cipher.doFinal(rotatedA);

        // 13. The reader checks this matches the original A random number (rotated one byte left).
        // If this fails then the authentication fails. If it matches, the reader knows the card
        // has the AES key too.
        // 14. Finally both reader and card generate a 16 byte session key using the random numbers
        // they now know. This is done by concatenating the first 4 bytes of A, first 4 bytes of B,
        // last 4 bytes of A and last 4 bytes of B.
        ByteArrayOutputStream sessionKeyOutputStream = new ByteArrayOutputStream();
        sessionKeyOutputStream.write(A, 0, 4);
        sessionKeyOutputStream.write(randomBytes, 0, 4);
        sessionKeyOutputStream.write(A, 12, 4);
        sessionKeyOutputStream.write(randomBytes, 12, 4);
        byte[] sessionKey = sessionKeyOutputStream.toByteArray();

        return new AuthenticationResponse(sessionKey, encryptedRotatedA);
    }
}
