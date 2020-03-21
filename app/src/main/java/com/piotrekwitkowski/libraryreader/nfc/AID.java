package com.piotrekwitkowski.libraryreader.nfc;

public final class AID {
    private byte[] aid;

    public AID(String aid) {
        this.aid = ByteUtils.toByteArray(aid);
    }

    public byte[] getAid() {
        return aid;
    }
}
