package com.piotrekwitkowski.nfc.se;

class AuthenticationResponse {
    private final byte[] sessionKey;
    private final byte[] encryptedRotatedA;

    AuthenticationResponse(byte[] sessionKey, byte[] encryptedRotatedA) {
        this.sessionKey = sessionKey;
        this.encryptedRotatedA = encryptedRotatedA;
    }

    byte[] getSessionKey() {
        return this.sessionKey;
    }

    byte[] getEncryptedRotatedA() {
        return encryptedRotatedA;
    }
}
