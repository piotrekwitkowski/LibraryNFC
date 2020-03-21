package com.piotrekwitkowski.lbraryreader.nfc;

public class Iso7816 {
    public static final byte[] RESPONSE_SUCCESS = new byte[] {(byte) 0x90, (byte) 0x00};

    final static byte SELECT = (byte) 0xA4;
//    private final static byte READ_BINARY = (byte) 0xB0;
//    private final static byteUPDATE_BINARY = (byte) 0xD6;
//    private final static byte READ_RECORDS = (byte) 0xB2;
//    private final static byte APPEND_RECORD = (byte) 0xE2;
//    private final static byte GET_CHALLENGE = (byte) 0x84;
//    private final static byte INTERNAL_AUTHENTICATE = (byte) 0x88;
//    private final static byte EXTERNAL_AUTHENTICATE = (byte) 0x82;

    static byte[] wrapApdu(byte cla, byte ins, byte p1, byte p2, byte[] command) {
        byte[] apduRequiredPart = new byte[] { cla, ins, p1, p2};
        if (command.length == 0) {
            return apduRequiredPart;
        } else {
            byte[] apduCommandPart = ByteUtils.concatenate((byte) command.length, command);
            byte[] apdu = ByteUtils.concatenate(apduRequiredPart, apduCommandPart);
            return apdu;
        }
    }

    static byte[] wrapApdu(byte cla, byte ins, byte p1, byte p2, byte[] command, byte le) {
        return ByteUtils.concatenate(wrapApdu(cla, ins, p1, p2, command), le);
    }
}
