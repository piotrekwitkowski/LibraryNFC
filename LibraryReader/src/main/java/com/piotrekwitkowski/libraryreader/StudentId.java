package com.piotrekwitkowski.libraryreader;

import android.content.Context;

import com.piotrekwitkowski.log.Log;
import com.piotrekwitkowski.nfc.ByteUtils;
import com.piotrekwitkowski.nfc.HCE;
import com.piotrekwitkowski.nfc.Iso7816;
import com.piotrekwitkowski.nfc.IsoDep;
import com.piotrekwitkowski.nfc.Response;
import com.piotrekwitkowski.nfc.desfire.DESFireException;
import com.piotrekwitkowski.nfc.desfire.DESFireReader;
import com.piotrekwitkowski.nfc.desfire.aids.AID;
import com.piotrekwitkowski.nfc.desfire.keys.AESKey;

import java.io.IOException;
import java.util.Arrays;

@SuppressWarnings("SameParameterValue")
class StudentId {
    private static final String TAG = "StudentId";
    private final IsoDep isoDep;
    enum idForm {PHYSICAL, HCE}

    private StudentId(IsoDep isoDep) {
        this.isoDep = isoDep;
    }

    static StudentId getStudentId(Context context, IsoDep isoDep) throws Exception {
        Log.i(TAG, "getStudentId()");
        isoDep.connect();

        idForm idForm = getIdForm(isoDep);
        Log.i(TAG, "ID form: "+ idForm);

        if (idForm == StudentId.idForm.PHYSICAL) {
            return new StudentId(isoDep);
        } else if (idForm == StudentId.idForm.HCE) {
            Response response = HCE.selectAndroidApp(context, isoDep);
            if (Arrays.equals(response.getBytes(), Iso7816.ISO7816_RESPONSE_SUCCESS)) {
                return new StudentId(isoDep);
            } else {
                throw new StudentIdException("HCE Mobile Application select was unsuccessful");
            }
        } else {
            throw new StudentIdException("ID form not supported");
        }
    }

    void close() throws IOException {
        isoDep.close();
    }

    private static idForm getIdForm(IsoDep isoDep) throws StudentIdException {
        Log.i(TAG, "getIdForm()");

        byte[] historicalBytes = isoDep.getHistoricalBytes();
        Log.i(TAG, "historicalBytes: " + ByteUtils.toHexString(historicalBytes));

        if (Arrays.equals(historicalBytes, new byte[]{(byte) 0x80})) {
            return idForm.PHYSICAL;
        } else if (Arrays.equals(historicalBytes, new byte[]{})) {
            return idForm.HCE;
        } else {
            throw new StudentIdException("id form not recognized");
        }
    }

    void selectApplication(AID aid) throws IOException, DESFireException {
        byte[] applicationAid = aid.getBytes();
        DESFireReader.selectApplication(this.isoDep, applicationAid);
        Log.i(TAG, "Application selected: " + ByteUtils.toHexString(applicationAid));
    }

    void authenticateAES(AESKey key, byte keyNumber) throws Exception {
        byte[] aesKey = key.getKey();
        byte[] sessionKey = DESFireReader.authenticateAES(this.isoDep, aesKey, keyNumber);
        Log.i(TAG, "Session key: " + ByteUtils.toHexString(sessionKey));
    }

    byte[] readData(int fileNumber, int offset, int length) throws IOException, DESFireException {
        byte[] response = DESFireReader.readData(this.isoDep, fileNumber, offset, length);
        // TODO: check CRC (last 8 bytes)
        byte[] data = ByteUtils.trimEnd(response, 8);
        Log.i(TAG, "Data: " + ByteUtils.toHexString(data));
        return data;
    }

}
