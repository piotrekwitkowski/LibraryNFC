package com.piotrekwitkowski.nfc;

import java.util.Arrays;

public class Response {
    byte[] bytes;

    public Response(byte[] responseBytes) {
        this.bytes = responseBytes;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public byte getStatus() {
        return bytes[0];
    }

    public byte[] getData() {
        return Arrays.copyOfRange(bytes, 1, bytes.length);

    }
}
