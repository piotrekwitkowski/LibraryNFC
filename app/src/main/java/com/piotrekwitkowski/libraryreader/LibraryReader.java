package com.piotrekwitkowski.libraryreader;

import android.nfc.Tag;

import com.piotrekwitkowski.libraryreader.nfc.AESKey;
import com.piotrekwitkowski.libraryreader.nfc.AID;
import com.piotrekwitkowski.libraryreader.nfc.IsoDep;

import java.util.Arrays;

class LibraryReader {
    private static final String TAG = "LibraryReader";
    private static final AID AID = new AID("015548");
    private static final AESKey AES_KEY = new AESKey("00000000000000000000000000000000");
    private static final byte KEY_NUMBER = (byte) 0x00;
    private static final byte FILE_NUMBER = (byte) 0x00;
    private static final byte[] FILE_OFFSET = new byte[] {(byte) 0x00, (byte) 0x00,(byte) 0x00};
    private static final byte[] FILE_LENGTH = new byte[] {(byte) 0x00, (byte) 0x00,(byte) 0x00};

    void processTag(Tag tag) {
        Log.i(TAG, "processTag()");
        IsoDep isoDep = IsoDep.get(tag);
        try {
            StudentId studentId = StudentId.getStudentId(isoDep);
            studentId.selectApplication(AID);
            studentId.authenticateAES(AES_KEY, KEY_NUMBER);
            byte[] libraryFileValue = studentId.getValue(FILE_NUMBER, FILE_OFFSET, FILE_LENGTH);
            byte[] libraryId = Arrays.copyOfRange(libraryFileValue, 10, 22);
            Log.i(TAG, "libraryId: " + new String(libraryId));

            studentId.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
