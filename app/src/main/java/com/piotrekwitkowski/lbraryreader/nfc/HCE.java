package com.piotrekwitkowski.lbraryreader.nfc;

import android.util.Log;

import com.piotrekwitkowski.lbraryreader.ByteUtils;

import java.io.IOException;

import static com.piotrekwitkowski.lbraryreader.nfc.Iso7816.SELECT;
import static com.piotrekwitkowski.lbraryreader.nfc.Iso7816.wrapApdu;

public class HCE {
    private static final String TAG = "HCE";

    // TODO: change ID to F...
    // see: https://stackoverflow.com/questions/27533193/android-hce-are-there-rules-for-aid
    private static final byte[] HCE_AID = ByteUtils.hexStringToByteArray("A0000002471001");

    public static byte[] selectAndroidApp(IsoDep isoDep) throws IOException {
        Log.i(TAG, "selectAndroidApp()");
        byte[] commandApdu = wrapApdu((byte) 0x00, SELECT, (byte) 0x04, (byte) 0x00, HCE_AID);
        return isoDep.transceive(commandApdu);
    }
}
