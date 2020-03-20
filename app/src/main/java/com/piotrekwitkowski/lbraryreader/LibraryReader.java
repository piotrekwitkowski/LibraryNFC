package com.piotrekwitkowski.lbraryreader;

import android.nfc.Tag;
import android.util.Log;

import java.io.IOException;

class LibraryReader {
    private static final String TAG = "LibraryReader";

    // TODO: change ID to F...
    // see: https://stackoverflow.com/questions/27533193/android-hce-are-there-rules-for-aid
    private static final String HCE_AID = "A0000002471001";

    void processTag(Tag tag) {
        Log.i(TAG, "processTag called");
        IsoDep isoDep = IsoDep.get(tag);
        try {
            isoDep.connect();

            getDesfireMasterFile(isoDep);

            isoDep.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getDesfireMasterFile(IsoDep isoDep) throws IOException {
        Log.i(TAG, "getDesfireMasterFile");
        byte[] response = isoDep.transceive(ByteUtils.hexStringToByteArray("00A4040007" + HCE_AID));

        if (ByteUtils.byteArrayToHexString(response).equals("9000")) {
            Log.i(TAG, "it was HCE!");
        } else {
            Log.i(TAG, "it was DESFire card!");
        }
    }

}
