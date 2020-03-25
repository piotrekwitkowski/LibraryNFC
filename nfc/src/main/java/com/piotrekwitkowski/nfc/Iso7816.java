package com.piotrekwitkowski.nfc;

public class Iso7816 {
    public static final byte[] ISO7816_RESPONSE_SUCCESS = new byte[] {(byte) 0x90, (byte) 0x00};
    private static final byte ISO7816_SELECT_COMMAND = (byte) 0xA4;
//    private final static byte READ_BINARY = (byte) 0xB0;
//    private final static byte UPDATE_BINARY = (byte) 0xD6;
//    private final static byte READ_RECORDS = (byte) 0xB2;
//    private final static byte APPEND_RECORD = (byte) 0xE2;
//    private final static byte GET_CHALLENGE = (byte) 0x84;
//    private final static byte INTERNAL_AUTHENTICATE = (byte) 0x88;
//    private final static byte EXTERNAL_AUTHENTICATE = (byte) 0x82;

    static byte[] wrapApdu(byte[] command) {
        byte[] apduRequiredPart = new byte[] {(byte) 0, ISO7816_SELECT_COMMAND, (byte) 4, (byte) 0};
        if (command.length == 0) {
            return apduRequiredPart;
        } else {
            byte[] apduCommandPart = ByteUtils.concatenate((byte) command.length, command);
            return ByteUtils.concatenate(apduRequiredPart, apduCommandPart);
        }
    }

}
