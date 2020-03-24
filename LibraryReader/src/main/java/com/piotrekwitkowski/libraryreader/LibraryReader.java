package com.piotrekwitkowski.libraryreader;

import android.content.Context;
import android.nfc.Tag;

import com.piotrekwitkowski.log.Log;
import com.piotrekwitkowski.nfc.AESKey;
import com.piotrekwitkowski.nfc.desfire.aids.AID;
import com.piotrekwitkowski.nfc.IsoDep;
import com.piotrekwitkowski.nfc.desfire.aids.LibraryAID;
import com.piotrekwitkowski.nfc.desfire.keys.ApplicationKey;
import com.piotrekwitkowski.nfc.desfire.keys.LibraryKey0;

import java.util.Arrays;

class LibraryReader {
    private static final String TAG = "LibraryReader";
//    private static final AID AID = new AID("015548");
    private static final AID LIBRARY_AID = new LibraryAID();
    private static final ApplicationKey LIBRARY_KEY = new LibraryKey0();
    private static final byte FILE_NUMBER = (byte) 0x00;
    private static final byte[] FILE_OFFSET = new byte[] {(byte) 0x00, (byte) 0x00,(byte) 0x00};
    private static final byte[] FILE_LENGTH = new byte[] {(byte) 0x00, (byte) 0x00,(byte) 0x00};
    private final Context context;

    LibraryReader(Context ctx) {
        this.context = ctx;
    }

    void processTag(Tag tag) {
        Log.i(TAG, "processTag()");
        IsoDep isoDep = IsoDep.get(tag);
        try {
            StudentId studentId = StudentId.getStudentId(this.context, isoDep);
            studentId.selectApplication(LIBRARY_AID);
            studentId.authenticateAES(LIBRARY_KEY.getKey(), LIBRARY_KEY.getKeyNumber());
            byte[] libraryFileValue = studentId.getValue(FILE_NUMBER, FILE_OFFSET, FILE_LENGTH);
            byte[] libraryId = Arrays.copyOfRange(libraryFileValue, 10, 22);
            Log.i(TAG, "libraryId: " + new String(libraryId));

            studentId.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
