package com.piotrekwitkowski.libraryreader.nfc;

import android.util.Log;

import java.io.IOException;

import static com.piotrekwitkowski.libraryreader.nfc.Iso7816.SELECT;
import static com.piotrekwitkowski.libraryreader.nfc.Iso7816.wrapApdu;

public class HCE {
    private static final String TAG = "HCE";

    // TODO: change ID to F...
    // see: https://stackoverflow.com/questions/27533193/android-hce-are-there-rules-for-aid
    private static final AID HCE_APP = new AID("A0000002471001");

    public static byte[] selectAndroidApp(IsoDep isoDep) throws IOException {
        Log.i(TAG, "selectAndroidApp()");
        byte[] commandApdu = wrapApdu((byte) 0x00, SELECT, (byte) 0x04, (byte) 0x00, HCE_APP.getAid());
        return isoDep.transceive(commandApdu);
    }
}
