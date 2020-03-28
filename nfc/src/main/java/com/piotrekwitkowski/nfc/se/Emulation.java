package com.piotrekwitkowski.nfc.se;

import com.piotrekwitkowski.log.Log;

public class Emulation {
    private static final String TAG = "Emulation";
    private final SecureElement secureElement;

    public Emulation(SecureElement secureElement) {
        this.secureElement = secureElement;
    }

    public byte[] getResponse(byte[] apdu) {
        Log.i(TAG, "getResponse()");
        return secureElement.processCommand(new Command(apdu));
    }

}
