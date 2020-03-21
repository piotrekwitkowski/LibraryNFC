package com.piotrekwitkowski.libraryreader;

import android.util.Log;

import com.piotrekwitkowski.libraryreader.nfc.AESKey;
import com.piotrekwitkowski.libraryreader.nfc.AID;
import com.piotrekwitkowski.libraryreader.nfc.ByteUtils;
import com.piotrekwitkowski.libraryreader.nfc.DESFire;
import com.piotrekwitkowski.libraryreader.nfc.HCE;
import com.piotrekwitkowski.libraryreader.nfc.Iso7816;
import com.piotrekwitkowski.libraryreader.nfc.IsoDep;

import java.io.IOException;
import java.util.Arrays;

class StudentId {
    private static final String TAG = "StudentId";
    private final IsoDep isoDep;
    enum idForm {PHYSICAL, HCE}

    private StudentId(IsoDep isoDep) {
        this.isoDep = isoDep;
    }

    static StudentId getStudentId(IsoDep isoDep) throws Exception {
        Log.i(TAG, "getStudentId()");
        isoDep.connect();

        idForm idForm = getIdForm(isoDep);
        Log.i(TAG, "id form: "+ idForm);

        if (idForm == StudentId.idForm.PHYSICAL) {
            return new StudentId(isoDep);
        } else if (idForm == StudentId.idForm.HCE) {
            byte[] selectResponse = HCE.selectAndroidApp(isoDep);
            if (!Arrays.equals(selectResponse, Iso7816.RESPONSE_SUCCESS)) {
                throw new StudentIdException("HCE Mobile Application select was unsuccessful");
            }
            return new StudentId(isoDep);
        } else {
            throw new StudentIdException("id form not supported");
        }
    }

    void close() throws IOException {
        isoDep.close();
    }

    private static idForm getIdForm(IsoDep isoDep) throws StudentIdException {
        Log.i(TAG, "getIdForm()");

        byte[] historicalBytes = isoDep.getHistoricalBytes();
        Log.i(TAG, "historicalBytes: " + ByteUtils.byteArrayToHexString(historicalBytes));

        if (Arrays.equals(historicalBytes, new byte[]{(byte) 0x80})) {
            return idForm.PHYSICAL;
        } else if (Arrays.equals(historicalBytes, new byte[]{})) {
            return idForm.HCE;
        } else throw new StudentIdException("id form not recognized");
    }

    void selectApplication(AID aid) throws IOException {
        byte[] applicationAid = aid.getAid();
        DESFire.selectApplication(this.isoDep, applicationAid);
    }

    byte[] authenticateAES(AESKey key, byte keyNumber) throws Exception {
        byte[] aesKey = key.getKey();
        return DESFire.authenticateAES(this.isoDep, aesKey, keyNumber);
    }


    byte[] getFile(byte fileNumber, byte[] offset, byte[] length) throws IOException {
        return DESFire.getValue(this.isoDep, fileNumber, offset, length);
    }

}
