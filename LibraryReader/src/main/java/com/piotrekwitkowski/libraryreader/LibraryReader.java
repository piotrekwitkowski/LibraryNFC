package com.piotrekwitkowski.libraryreader;

import android.content.Context;
import android.nfc.Tag;

import com.piotrekwitkowski.log.Log;
import com.piotrekwitkowski.nfc.desfire.aids.AID;
import com.piotrekwitkowski.nfc.desfire.aids.AIDWrongLengthException;
import com.piotrekwitkowski.nfc.desfire.keys.ApplicationKey;

class LibraryReader {
    private static final String TAG = "LibraryReader";
    private final Context context;

    LibraryReader(Context ctx) {
        this.context = ctx;
    }

    void processTag(Tag tag) throws AIDWrongLengthException {
        Log.i(TAG, "processTag()");

        final AID LIBRARY_AID = new AID("015548");
        final ApplicationKey LIBRARY_KEY = new ApplicationKey("00000000000000000000000000000000", 0);
        final int FILE_NUMBER = 0;
        final int FILE_OFFSET = 10;
        final int FILE_LENGTH = 12;
        final IsoDep isoDep = IsoDep.get(tag);

        try {
            StudentId studentId = StudentId.getStudentId(this.context, isoDep);
            studentId.selectApplication(LIBRARY_AID);
            studentId.authenticateAES(LIBRARY_KEY);
            byte[] libraryId = studentId.readData(FILE_NUMBER, FILE_OFFSET, FILE_LENGTH);
            Log.i(TAG, "libraryId: " + new String(libraryId));

            studentId.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
