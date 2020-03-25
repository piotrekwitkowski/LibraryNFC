package com.piotrekwitkowski.nfc.desfire.applications;

import com.piotrekwitkowski.log.Log;
import com.piotrekwitkowski.nfc.ByteUtils;
import com.piotrekwitkowski.nfc.Response;
import com.piotrekwitkowski.nfc.desfire.Commands;
import com.piotrekwitkowski.nfc.desfire.DESFireException;
import com.piotrekwitkowski.nfc.desfire.ResponseCodes;
import com.piotrekwitkowski.nfc.desfire.aids.AID;
import com.piotrekwitkowski.nfc.desfire.keys.AESKey;
import com.piotrekwitkowski.nfc.desfire.keys.ApplicationKey;
import com.piotrekwitkowski.nfc.desfire.states.AuthenticationInProgressState;
import com.piotrekwitkowski.nfc.desfire.states.CommandResult;
import com.piotrekwitkowski.nfc.desfire.states.State;


import java.io.ByteArrayOutputStream;
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

public abstract class Application {
    private static final String TAG = "Application";
    protected static AID aid;
    ApplicationKey applicationKey;

    AID getAid() {
        return aid;
    }

    public CommandResult initiateAESAuthentication(State state, byte keyNumber) {
        Log.i(TAG, "initiateAESAuthentication()");

        // TODO: support many keys and different permissions
        if (keyNumber == applicationKey.getKeyNumber()) {
            try {
                return authenticateAESWithKey(state, applicationKey.getKey());
            } catch (Exception e) {
                e.printStackTrace();
                return new CommandResult(null, ResponseCodes.AUTHENTICATION_ERROR);
            }
        } else {
            return new CommandResult(null, ResponseCodes.NO_SUCH_KEY);
        }
    }

    private CommandResult authenticateAESWithKey(State state, AESKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Log.i(TAG, "authenticateAESWithKey()");

        // 1. The reader asked for AES authentication for a specific key.
        // 2. The card creates a 16 byte random number (B) and encrypts it with the selected AES
        // key. The result is sent to the reader.
        byte[] B = ByteUtils.getRandomBytes(16);
        Log.i(TAG, "challenge: " + ByteUtils.toHexString(B));
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        Key aes = new SecretKeySpec(key.getKey(), "AES");
        IvParameterSpec ivParam = new IvParameterSpec(new byte[key.getKey().length]);
        cipher.init(Cipher.ENCRYPT_MODE, aes, ivParam);
        byte[] challenge = cipher.doFinal(B);
        Log.i(TAG, "cipheredData: " + ByteUtils.toHexString(challenge));

        byte[] response = ByteUtils.concatenate(ResponseCodes.ADDITIONAL_FRAME, challenge);
        return new CommandResult(new AuthenticationInProgressState(state, this, key, B, challenge), response);


        // 3. The reader receives the 16 bytes, and decrypts it using the AES key to get back the
        // original 16 byte random number (B). This is decrypted with an IV of all 00 bytes.
        // 4. The reader generates its own 16 byte random number (A).
        // 5. The reader rotates B one byte to the left.
        // 6. The reader concatenates A and the rotated B together to make a 32 byte value C.
        // 7. The reader encrypts the 32 byte value C with the AES key and sends D to the card. The
        // IV for encrypting this is the 16 bytes received from the card (i.e. before decryption).

        // 8. The card receives the 32 byte value D and decrypts it with the AES key.
        // 9. The card checks the second 16 bytes match the original random number B (rotated one
        // byte left). If this fails the authentication has failed. If it matches, the card knows
        // the reader has the right key.

        // 10. The card rotates the first 16 bytes (A) left by one byte.
        // 11. The card encrypts this rotated A using the AES key and sends it to the reader.
        // 12. The reader receives the 16 bytes and decrypts it. The IV for this is the last 16
        // bytes the reader sent to the card.

        // 13. The reader checks this matches the original A random number (rotated one byte left).
        // If this fails then the authentication fails. If it matches, the reader knows the card
        // has the AES key too.

        // 14. Finally both reader and card generate a 16 byte session key using the random numbers
        // they now know. This is done by concatenating the first 4 bytes of A, first 4 bytes of B,
        // last 4 bytes of A and last 4 bytes of B.
    }

}
