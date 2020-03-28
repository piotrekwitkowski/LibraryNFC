package com.piotrekwitkowski.nfc.desfire;

public class ResponseCodes {
    public static final byte SUCCESS = (byte) 0x00;
    public static final byte ILLEGAL_COMMAND = (byte) 0x1C;
    public static final byte NO_SUCH_KEY = (byte) 0x40;
    public static final byte LENGTH_ERROR = (byte) 0x7E;
    public static final byte APPLICATION_NOT_FOUND = (byte) 0xA0;
    public static final byte AUTHENTICATION_ERROR = (byte) 0xAE;
    public static final byte ADDITIONAL_FRAME = (byte) 0xAF;
    public static final byte BOUNDARY_ERROR = (byte) 0xBE;
    public static final byte FILE_NOT_FOUND = (byte) 0xF0;
}
