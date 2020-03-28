package com.piotrekwitkowski.nfc.se;

import com.piotrekwitkowski.log.Log;

public class Emulation {
    private static final String TAG = "Emulation";
    private SEWrapper seWrapper;

    public Emulation(SEWrapper seWrapper) {
        this.seWrapper = seWrapper;
    }

    public byte[] getResponse(byte[] apdu) {
        Log.i(TAG, "getResponse()");
        return seWrapper.processCommand(new Command(apdu));
    }

}
