package com.piotrekwitkowski.nfc.se;

import com.piotrekwitkowski.nfc.desfire.AESKey;
import com.piotrekwitkowski.nfc.desfire.AID;
import com.piotrekwitkowski.nfc.desfire.File;

public abstract class Application {
    private static final String TAG = "Application";
    private final AID aid;
    private final AESKey key0;
    private final File file0;

    protected Application(AID aid, AESKey aesKey0, File file0) {
        this.aid = aid;
        this.key0 = aesKey0;
        this.file0 = file0;
    }

    public AID getAid() {
        return aid;
    }

    public File getFile0() { return file0; }

    AESKey getKey0() {
        return this.key0;
    }

}
