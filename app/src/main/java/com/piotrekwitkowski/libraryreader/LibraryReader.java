package com.piotrekwitkowski.libraryreader;

import android.nfc.Tag;
import android.util.Log;

import com.piotrekwitkowski.libraryreader.nfc.AESKey;
import com.piotrekwitkowski.libraryreader.nfc.AID;
import com.piotrekwitkowski.libraryreader.nfc.IsoDep;

class LibraryReader {
    private static final String TAG = "LibraryReader";
    private static final AID LIBRARY_AID = new AID("015548");

    private static final AESKey LIBRARY_AES_KEY = new AESKey("00000000000000000000000000000000");
    private static final byte LIBRARY_KEY_NUMBER = (byte) 0x00;

    void processTag(Tag tag) {
        Log.i(TAG, "processTag()");
        IsoDep isoDep = IsoDep.get(tag);
        try {
            StudentId studentId = StudentId.getStudentId(isoDep);
            studentId.selectApplication(LIBRARY_AID);
            byte[] sessionKey = studentId.authenticateAES(LIBRARY_AES_KEY, LIBRARY_KEY_NUMBER);
//            getFile(studentId, fileNo);

            studentId.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
