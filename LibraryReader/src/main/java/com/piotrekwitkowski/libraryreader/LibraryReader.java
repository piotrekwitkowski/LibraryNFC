package com.piotrekwitkowski.libraryreader;

import android.content.Context;
import android.nfc.Tag;

import com.piotrekwitkowski.log.Log;
import com.piotrekwitkowski.nfc.desfire.aids.AID;
import com.piotrekwitkowski.nfc.desfire.InvalidParameterException;
import com.piotrekwitkowski.nfc.desfire.keys.AESKey;

class LibraryReader {
    private static final String TAG = "LibraryReader";
    private final Context context;

    LibraryReader(Context ctx) {
        this.context = ctx;
    }

    void processTag(Tag tag) throws InvalidParameterException {
        Log.i(TAG, "processTag()");

        final AID LIBRARY_AID = new AID("015548");
        final AESKey LIBRARY_KEY = new AESKey("00000000000000000000000000000000");
        final int LIBRARY_KEY_NUMBER = 0;
        final int FILE_NUMBER = 0;
        final int FILE_OFFSET = 10;
        final int FILE_LENGTH = 12;
        final IsoDep isoDep = IsoDep.get(tag);

        try {
            StudentId studentId = StudentId.getStudentId(this.context, isoDep);
            studentId.selectApplication(LIBRARY_AID);
            studentId.authenticateAES(LIBRARY_KEY, LIBRARY_KEY_NUMBER);
            byte[] libraryId = studentId.readData(FILE_NUMBER, FILE_OFFSET, FILE_LENGTH);
            Log.i(TAG, "libraryId: " + new String(libraryId));

            studentId.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
