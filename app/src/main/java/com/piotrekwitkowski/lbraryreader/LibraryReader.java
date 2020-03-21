package com.piotrekwitkowski.lbraryreader;

import android.nfc.Tag;
import android.util.Log;

import com.piotrekwitkowski.lbraryreader.nfc.DESFire;
import com.piotrekwitkowski.lbraryreader.nfc.IsoDep;

import java.io.IOException;

class LibraryReader {
    private static final String TAG = "LibraryReader";
    private static final byte[] LIBRARY_AID = ByteUtils.hexStringToByteArray("015548");

    void processTag(Tag tag) {
        Log.i(TAG, "processTag()");
        IsoDep isoDep = IsoDep.get(tag);
        try {
            StudentId studentId = StudentId.getStudentId(isoDep);
            selectLibraryApplication(studentId);
//            authenticateAES(studentId, key);
//            getFile(studentId, fileNo);

            studentId.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void selectLibraryApplication(StudentId studentId) throws IOException {
        DESFire.selectApplication(studentId.getIsoDep(), LibraryReader.LIBRARY_AID);
    }

}
