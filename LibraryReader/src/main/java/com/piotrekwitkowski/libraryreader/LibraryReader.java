package com.piotrekwitkowski.libraryreader;

import android.content.Context;
import android.nfc.Tag;

import com.piotrekwitkowski.log.Log;
import com.piotrekwitkowski.nfc.IsoDep;
import com.piotrekwitkowski.nfc.desfire.aids.AID;
import com.piotrekwitkowski.nfc.desfire.keys.ApplicationKey;

class LibraryReader {
    private static final String TAG = "LibraryReader";
    private static final AID LIBRARY_AID = new AID("015548");
    private static final ApplicationKey LIBRARY_KEY = new ApplicationKey("00000000000000000000000000000000", 0);
    private static final int FILE_NUMBER = 0;
    private static final int FILE_OFFSET = 10;
    private static final int FILE_LENGTH = 12;
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
            studentId.authenticateAES(LIBRARY_KEY);
            byte[] libraryId = studentId.readData(FILE_NUMBER, FILE_OFFSET, FILE_LENGTH);
            Log.i(TAG, "libraryId: " + new String(libraryId));

            studentId.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
