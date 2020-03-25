package com.piotrekwitkowski.nfc;

import java.util.Arrays;

public class Response {
    private final byte[] bytes;

    Response(byte[] responseBytes) {
        this.bytes = responseBytes;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public byte getResponseCode() {
        return bytes[0];
    }

    public byte[] getData() {
        return Arrays.copyOfRange(bytes, 1, bytes.length);
    }
}
