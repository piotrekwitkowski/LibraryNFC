package com.piotrekwitkowski.nfc;

import java.security.SecureRandom;
import java.util.Arrays;

public class ByteUtils {

    public static byte[] toByteArray(String s) throws IllegalArgumentException {
        int len = s.length();
        if (len % 2 == 1) {
            throw new IllegalArgumentException("Hex string must have even number of characters");
        }
        byte[] data = new byte[len / 2]; // Allocate 1 byte per 2 hex characters
        for (int i = 0; i < len; i += 2) {
            // Convert each character into a integer (base-16), then bit-shift into place
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static String toHexString(byte[] bytes) {
        final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[bytes.length * 2]; // Each byte has two hex characters (nibbles)
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF; // Cast bytes[j] to int, treating as unsigned value
            hexChars[j * 2] = hexArray[v >>> 4]; // Select hex character from upper nibble
            hexChars[j * 2 + 1] = hexArray[v & 0x0F]; // Select hex character from lower nibble
        }
        return new String(hexChars);
    }

    static byte[] concatenate(byte a, byte b) {
        return new byte[] {a, b};
    }

    public static byte[] concatenate(byte a, byte[] b) {
        return concatenate(new byte[] { a }, b );
    }

    public static byte[] concatenate(byte[] a, byte[] b) {
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

    public static byte[] rotateOneLeft(byte[] a) {
        final byte[] rotated = new byte[a.length];
        if (a.length - 1 >= 0) System.arraycopy(a, 1, rotated, 0, a.length - 1);
        rotated[rotated.length - 1] = a[0];
        return rotated;
    }

    public static byte[] first16Bytes(byte[] a) {
        return Arrays.copyOfRange(a,0, 16);
    }

    public static byte[] last16Bytes(byte[] a) {
        return Arrays.copyOfRange(a,a.length - 16, a.length);
    }

    public static byte[] getRandomBytes(int length) {
        byte[] random = new byte[length];
        new SecureRandom().nextBytes(random);
        return random;
    }


}
