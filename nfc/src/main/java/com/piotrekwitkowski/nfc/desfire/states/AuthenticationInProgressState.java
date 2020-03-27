package com.piotrekwitkowski.nfc.desfire.states;

import com.piotrekwitkowski.log.Log;
import com.piotrekwitkowski.nfc.ByteUtils;
import com.piotrekwitkowski.nfc.desfire.Command;
import com.piotrekwitkowski.nfc.desfire.ResponseCodes;
import com.piotrekwitkowski.nfc.desfire.Application;
import com.piotrekwitkowski.nfc.desfire.AESKey;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AuthenticationInProgressState extends State {
    private static final String TAG = "AuthenticationInProgressState";
    private final Application application;
    private final byte[] B;
    private final byte[] cardChallenge;
    private final AESKey key;
    private final State oldState;

    public AuthenticationInProgressState(State state, Application application, AESKey key, byte[] B, byte[] challenge) {
        this.oldState = state;
        this.application = application;
        this.key = key;
        this.B = B;
        this.cardChallenge = challenge;
    }

    public CommandResult processCommand(Command command) {
        Log.i(TAG, "processCommand()");

        if (command.getCode() == ResponseCodes.ADDITIONAL_FRAME) {
            try {
                return proceedAuthentication(command.getData());
            } catch (Exception e) {
                e.printStackTrace();
                return new CommandResult(this, ResponseCodes.AUTHENTICATION_ERROR);
            }
        } else {
            return new CommandResult(this, ResponseCodes.ILLEGAL_COMMAND);
        }
    }

    private CommandResult proceedAuthentication(byte[] D) throws Exception {
        // 3. The reader receives the 16 bytes, and decrypts it using the AES key to get back the
        // original 16 byte random number (B). This is decrypted with an IV of all 00 bytes.
        // 4. The reader generates its own 16 byte random number (A).
        // 5. The reader rotates B one byte to the left.
        // 6. The reader concatenates A and the rotated B together to make a 32 byte value C.
        // 7. The reader encrypts the 32 byte value C with the AES key and sends D to the card. The
        // IV for encrypting this is the 16 bytes received from the card (i.e. before decryption).
        // 8. The card receives the 32 byte value D and decrypts it with the AES key.
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        Key aes = new SecretKeySpec(key.getKey(), "AES");
        IvParameterSpec ivParam = new IvParameterSpec(cardChallenge);
        cipher.init(Cipher.DECRYPT_MODE, aes, ivParam);
        byte[] C = cipher.doFinal(D);
        Log.i(TAG, "from Reader: " + ByteUtils.toHexString(C));

        // 9. The card checks the second 16 bytes of C match the original random number B (rotated one
        // byte left). If this fails the authentication has failed. If it matches, the card knows
        // the reader has the right key.
        if (!Arrays.equals(ByteUtils.last16Bytes(C), ByteUtils.rotateOneLeft(B))) {
            return new CommandResult(oldState, ResponseCodes.AUTHENTICATION_ERROR);
        }

        // 10. The card rotates the first 16 bytes (A) left by one byte.
        byte[] A = ByteUtils.first16Bytes(C);
        byte[] rotatedA = ByteUtils.rotateOneLeft(A);

        // 11. The card encrypts this rotated A using the AES key and sends it to the reader.
        // 12. The reader receives the 16 bytes and decrypts it. The IV for this is the last 16
        // bytes the reader sent to the card.
        ivParam = new IvParameterSpec(ByteUtils.last16Bytes(D));
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
        sessionKeyOutputStream.write(B, 0, 4);
        sessionKeyOutputStream.write(A, 12, 4);
        sessionKeyOutputStream.write(B, 12, 4);
        byte[] sessionKey = sessionKeyOutputStream.toByteArray();
        State newState = new ApplicationAuthenticatedState(application, sessionKey);

        byte[] response = ByteUtils.concatenate(ResponseCodes.SUCCESS, encryptedRotatedA);
        return new CommandResult(newState, response);

    }
}
