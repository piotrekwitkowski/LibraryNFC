package com.piotrekwitkowski.nfc;

import android.content.Context;

import com.piotrekwitkowski.log.Log;

import java.io.IOException;

import static com.piotrekwitkowski.nfc.Iso7816.SELECT;
import static com.piotrekwitkowski.nfc.Iso7816.wrapApdu;

public class HCE {
    private static final String TAG = "HCE";

    public static Response selectAndroidApp(Context context, IsoDep isoDep) throws IOException {
        Log.i(TAG, "selectAndroidApp()");

        final AID HCE_AID = new AID(context.getString(R.string.hce_aid));
        byte[] commandApdu = wrapApdu((byte) 0x00, SELECT, (byte) 0x04, (byte) 0x00, HCE_AID.getAid());
        return isoDep.transceive(commandApdu);
    }
}
