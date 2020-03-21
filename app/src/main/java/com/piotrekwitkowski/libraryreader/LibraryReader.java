package com.piotrekwitkowski.libraryreader;

import android.nfc.Tag;
import android.util.Log;

import com.piotrekwitkowski.libraryreader.nfc.AESKey;
import com.piotrekwitkowski.libraryreader.nfc.AID;
import com.piotrekwitkowski.libraryreader.nfc.IsoDep;

import java.util.Arrays;

class LibraryReader {
    private static final String TAG = "LibraryReader";
    private static final AID LIBRARY_AID = new AID("015548");

    private static final AESKey LIBRARY_AES_KEY = new AESKey("00000000000000000000000000000000");
    private static final byte LIBRARY_KEY_NUMBER = (byte) 0x00;
    private static final byte LIBRARY_FILE_NUMBER = (byte) 0x00;
    private static final byte[] LIBRARY_FILE_OFFSET = new byte[] {(byte) 0x00, (byte) 0x00,(byte) 0x00};
    private static final byte[] LIBRARY_FILE_LENGTH = new byte[] {(byte) 0x00, (byte) 0x00,(byte) 0x00};

    void processTag(Tag tag) {
        Log.i(TAG, "processTag()");
        IsoDep isoDep = IsoDep.get(tag);
        try {
            StudentId studentId = StudentId.getStudentId(isoDep);
            studentId.selectApplication(LIBRARY_AID);
            byte[] sessionKey = studentId.authenticateAES(LIBRARY_AES_KEY, LIBRARY_KEY_NUMBER);
            byte[] file = studentId.getFile(LIBRARY_FILE_NUMBER, LIBRARY_FILE_OFFSET, LIBRARY_FILE_LENGTH);
            byte[] libraryId = Arrays.copyOfRange(file, 11, 22);
            Log.i(TAG, "libraryId: " + new String(libraryId));

            studentId.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
