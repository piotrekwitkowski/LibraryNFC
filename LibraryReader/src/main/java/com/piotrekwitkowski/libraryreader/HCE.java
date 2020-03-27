package com.piotrekwitkowski.libraryreader;

import android.content.Context;

import com.piotrekwitkowski.log.Log;
import com.piotrekwitkowski.nfc.ByteUtils;

import java.io.IOException;

import static com.piotrekwitkowski.nfc.Iso7816.wrapApdu;

class HCE {
    private static final String TAG = "HCE";

    static Response selectAndroidApp(Context context, IsoDep isoDep) throws IOException {
        Log.i(TAG, "selectAndroidApp()");

        String HCE_AID = context.getString(com.piotrekwitkowski.nfc.R.string.hce_aid);
        byte[] commandApdu = wrapApdu(ByteUtils.toByteArray(HCE_AID));
        return isoDep.transceive(commandApdu);
    }
}
