package com.piotrekwitkowski.nfc;

import java.util.Arrays;

public class Response {
    private byte[] bytes;

    public Response(byte[] responseBytes) {
        this.bytes = responseBytes;
    }

    public Response(byte responseCode) {
        this.bytes = new byte[] {responseCode};
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
