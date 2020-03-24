package com.piotrekwitkowski.nfc.desfire.applications;

import com.piotrekwitkowski.nfc.desfire.aids.AID;
import com.piotrekwitkowski.nfc.desfire.keys.ApplicationKey;

public abstract class Application {
    protected AID aid;
    protected ApplicationKey applicationKey;

    AID getAid() {
        return aid;
    }
}
