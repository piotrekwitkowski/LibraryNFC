package com.piotrekwitkowski.libraryreader;

import java.util.Arrays;

class Response {
    private final byte[] bytes;

    Response(byte[] responseBytes) {
        this.bytes = responseBytes;
    }

    byte[] getBytes() {
        return bytes;
    }

    byte getResponseCode() {
        return bytes[0];
    }

    byte[] getData() {
        return Arrays.copyOfRange(bytes, 1, bytes.length);
    }
}
