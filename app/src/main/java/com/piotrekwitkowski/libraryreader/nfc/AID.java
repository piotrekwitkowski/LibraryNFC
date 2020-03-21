package com.piotrekwitkowski.libraryreader.nfc;

public final class AID {
    private byte[] aid;

    public AID(String aid) {
        this.aid = ByteUtils.hexStringToByteArray(aid);
    }

    public byte[] getAid() {
        return aid;
    }
}
