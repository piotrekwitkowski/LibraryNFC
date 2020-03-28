package com.piotrekwitkowski.nfc.se;

public class AuthenticationResponse {
    private final byte[] sessionKey;
    private final byte[] encryptedRotatedA;

    AuthenticationResponse(byte[] sessionKey, byte[] encryptedRotatedA) {
        this.sessionKey = sessionKey;
        this.encryptedRotatedA = encryptedRotatedA;
    }

    public byte[] getSessionKey() {
        return this.sessionKey;
    }

    public byte[] getEncryptedRotatedA() {
        return encryptedRotatedA;
    }
}
