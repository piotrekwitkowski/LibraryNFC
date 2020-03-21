package com.piotrekwitkowski.libraryreader.nfc;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DESFire {
    private static final String TAG = "DESFire";
    private static final byte SELECT_APPLICATION = (byte) 0x5A;
    private static final byte AUTHENTICATE_AES = (byte) 0xAA;
    private static final byte ADDITIONAL_FRAME = (byte) 0xAF;
    private static final byte GET_VALUE = (byte) 0xBD;
    private static final byte RESPONSE_SUCCESS = (byte) 0x00;

    private static final int AES_KEY_LENGTH = 16;

    public static void selectApplication(IsoDep isoDep, byte[] aid) throws IOException, DESFireException {
        Log.i(TAG, "selectApplication()");
        byte[] command = ByteUtils.concatenate(SELECT_APPLICATION, aid);
        byte[] response = isoDep.transceive(command);
        byte responseStatus = ByteUtils.firstByte(response);
        if (responseStatus != RESPONSE_SUCCESS) {
            throw new DESFireException("selectApplication() failed. Response status: " + responseStatus);
        }
    }

    public static byte[] authenticateAES(IsoDep isoDep, byte[] aesKey, byte keyNumber) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, DESFireException {
        Log.i(TAG, "authenticateAES()");
        // 1. The reader asked for AES authentication for a specific key.
        // 2. The card creates a 16 byte random number (B) and encrypts it with the selected AES
        // key. The result is sent to the reader.
        byte[] command = ByteUtils.concatenate(AUTHENTICATE_AES, keyNumber);
        byte[] response = isoDep.transceive(command);
        byte[] challenge = ByteUtils.trimOneFront(response);
        Log.i(TAG, "challenge: " + ByteUtils.toHexString(challenge));

        // 3. The reader receives the 16 bytes, and decrypts it using the AES key to get back the
        // original 16 byte random number (B). This is decrypted with an IV of all 00 bytes.
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        Key aes = new SecretKeySpec(aesKey, "AES");
        IvParameterSpec ivParam = new IvParameterSpec(new byte[AES_KEY_LENGTH]);
        cipher.init(Cipher.DECRYPT_MODE, aes, ivParam);
        byte[] B = cipher.doFinal(challenge);
        Log.i(TAG, "cipheredData: " + ByteUtils.toHexString(B));

        // 4. The reader generates its own 16 byte random number (A).
        byte[] A = new byte[AES_KEY_LENGTH];
        new SecureRandom().nextBytes(A);

        // 5. The reader rotates B one byte to the left.
        byte[] rotatedB = ByteUtils.rotateOneLeft(B);

        // 6. The reader concatenates A and the rotated B together to make a 32 byte value C.
        byte[] C = ByteUtils.concatenate(A, rotatedB);

        // 7. The reader encrypts the 32 byte value C with the AES key and sends D to the card. The
        // IV for encrypting this is the 16 bytes received from the card (i.e. before decryption).
        ivParam = new IvParameterSpec(challenge);
        cipher.init(Cipher.ENCRYPT_MODE, aes, ivParam);
        byte[] D = cipher.doFinal(C);
        command = ByteUtils.concatenate(ADDITIONAL_FRAME, D);
        response = isoDep.transceive(command);
        challenge = ByteUtils.trimOneFront(response);

        // 8. The card receives the 32 byte value D and decrypts it with the AES key.
        // 9. The card checks the second 16 bytes match the original random number B (rotated one
        // byte left). If this fails the authentication has failed. If it matches, the card knows
        // the reader has the right key.
        if (ByteUtils.firstByte(response) != RESPONSE_SUCCESS) {
            throw new DESFireException("authenticateAES failed");
        }

        // 10. The card rotates the first 16 bytes (A) left by one byte.
        // 11. The card encrypts this rotated A using the AES key and sends it to the reader.
        // 12. The reader receives the 16 bytes and decrypts it. The IV for this is the last 16
        // bytes the reader sent to the card.
        byte[] last16Bytes = ByteUtils.last16Bytes(command);
        ivParam = new IvParameterSpec(last16Bytes);
        cipher.init(Cipher.DECRYPT_MODE, aes, ivParam);
        byte[] E = cipher.doFinal(challenge);

        // 13. The reader checks this matches the original A random number (rotated one byte left).
        // If this fails then the authentication fails. If it matches, the reader knows the card
        // has the AES key too.
        if (Arrays.equals(ByteUtils.rotateOneLeft(A), E)) {
            throw new DESFireException("authenticateAES failed");
        }

        // 14. Finally both reader and card generate a 16 byte session key using the random numbers
        // they now know. This is done by concatenating the first 4 bytes of A, first 4 bytes of B,
        // last 4 bytes of A and last 4 bytes of B.
        ByteArrayOutputStream sessionKeyOutputStream = new ByteArrayOutputStream();
        sessionKeyOutputStream.write(A, 0, 4);
        sessionKeyOutputStream.write(B, 0, 4);
        sessionKeyOutputStream.write(A, 12, 4);
        sessionKeyOutputStream.write(B, 12, 4);
        return sessionKeyOutputStream.toByteArray();
    }

    public static byte[] getValue(IsoDep isoDep, byte fileNumber, byte[] offset, byte[] length) throws IOException, DESFireException {
        Log.i(TAG, "getValue()");

        byte[] command = ByteUtils.concatenate(GET_VALUE, fileNumber);
        command = ByteUtils.concatenate(command, offset);
        command = ByteUtils.concatenate(command, length);
        byte[] response = isoDep.transceive(command);
        byte responseStatus = ByteUtils.firstByte(response);
        if (responseStatus == RESPONSE_SUCCESS) {
            return ByteUtils.trimOneFront(response);
        } else {
            throw new DESFireException("getValue failed. Response status: " + responseStatus);
        }
    }

}
