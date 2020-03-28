package com.piotrekwitkowski.libraryreader;

import com.piotrekwitkowski.log.Log;
import com.piotrekwitkowski.nfc.ByteUtils;
import com.piotrekwitkowski.nfc.desfire.Commands;
import com.piotrekwitkowski.nfc.desfire.ResponseCodes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

class DESFireReader {
    private static final String TAG = "DESFireReader";

    static void selectApplication(IsoDep isoDep, byte[] aid) throws IOException, DESFireReaderException {
        Log.i(TAG, "selectApplication()");

        Response response = isoDep.transceive(Commands.SELECT_APPLICATION, aid);
        if (response.getResponseCode() != ResponseCodes.SUCCESS) {
            throw new DESFireReaderException("selectApplication() failed. Response status: " + response.getResponseCode());
        }
    }

    static byte[] authenticateAES(IsoDep isoDep, byte[] aesKey, byte keyNumber) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, DESFireReaderException {
        Log.i(TAG, "authenticateAES()");

        // 1. The reader asked for AES authentication for a specific key.
        // 2. The card creates a 16 byte random number (B) and encrypts it with the selected AES
        // key. The result is sent to the reader.
        Response response = isoDep.transceive(Commands.AUTHENTICATE_AES, keyNumber);
        byte[] challenge = response.getData();
        Log.i(TAG, "challenge: " + ByteUtils.toHexString(challenge));

        // 3. The reader receives the 16 bytes, and decrypts it using the AES key to get back the
        // original 16 byte random number (B). This is decrypted with an IV of all 00 bytes.
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        Key aes = new SecretKeySpec(aesKey, "AES");
        IvParameterSpec ivParam = new IvParameterSpec(new byte[16]);
        cipher.init(Cipher.DECRYPT_MODE, aes, ivParam);
        byte[] B = cipher.doFinal(challenge);
        Log.i(TAG, "cipheredData: " + ByteUtils.toHexString(B));

        // 4. The reader generates its own 16 byte random number (A).
        byte[] A = ByteUtils.getRandomBytes(16);

        // 5. The reader rotates B one byte to the left.
        byte[] rotatedB = ByteUtils.rotateOneLeft(B);

        // 6. The reader concatenates A and the rotated B together to make a 32 byte value C.
        byte[] C = ByteUtils.concatenate(A, rotatedB);

        // 7. The reader encrypts the 32 byte value C with the AES key and sends D to the card. The
        // IV for encrypting this is the 16 bytes received from the card (i.e. before decryption).
        ivParam = new IvParameterSpec(challenge);
        cipher.init(Cipher.ENCRYPT_MODE, aes, ivParam);
        byte[] D = cipher.doFinal(C);
        byte[] command = ByteUtils.concatenate(Commands.ADDITIONAL_FRAME, D);
        response = isoDep.transceive(command);
        challenge = response.getData();

        // 8. The card receives the 32 byte value D and decrypts it with the AES key.
        // 9. The card checks the second 16 bytes match the original random number B (rotated one
        // byte left). If this fails the authentication has failed. If it matches, the card knows
        // the reader has the right key.
        if (response.getResponseCode() != ResponseCodes.SUCCESS) {
            throw new DESFireReaderException("authenticateAES failed");
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
        if (!Arrays.equals(ByteUtils.rotateOneLeft(A), E)) {
            throw new DESFireReaderException("authenticateAES failed");
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

    static byte[] readData(IsoDep isoDep, int fileNumber, int offset, int length) throws IOException, DESFireReaderException {
        Log.i(TAG, "readData()");

        // TODO: check if file
        // TODO: check if offset and length smaller than 3 bytes, else throw Exception
        byte[] offsetBytes = ByteUtils.first3Bytes(offset);
        byte[] lengthBytes = ByteUtils.first3Bytes(length);

        byte[] params = ByteUtils.concatenate(offsetBytes, lengthBytes);
        byte[] commandData = ByteUtils.concatenate((byte) fileNumber, params);

        Response response = isoDep.transceive(Commands.READ_DATA, commandData);
        if (response.getResponseCode() == ResponseCodes.SUCCESS) {
            return response.getData();
        } else if (response.getResponseCode() == ResponseCodes.BOUNDARY_ERROR) {
            throw new DESFireReaderException("Boundary error!");
        } else {
            throw new DESFireReaderException("readData failed. Response status: " + response.getResponseCode());
        }
    }

}
