package com.piotrekwitkowski.libraryhce;

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;

import com.piotrekwitkowski.log.Log;
import com.piotrekwitkowski.nfc.ByteUtils;
import com.piotrekwitkowski.nfc.Iso7816;
import com.piotrekwitkowski.nfc.desfire.DESFireEmulation;
import com.piotrekwitkowski.nfc.desfire.aids.AIDWrongLengthException;
import com.piotrekwitkowski.nfc.desfire.applications.LibraryApplication;

public class HCEService extends HostApduService {
    private static final String TAG = "HCEService";
    private static boolean firstInteraction = true;
    private static DESFireEmulation emulation;
    private final NotificationService notifications = new NotificationService(this);

    @Override
    public byte[] processCommandApdu(byte[] command, Bundle extras) {
        byte[] response = firstInteraction ? getFirstResponse(command) : getNextResponse(command);
        Log.i(TAG, "--> " + ByteUtils.toHexString(response));
        return response;
    }

    private byte[] getFirstResponse(byte[] command) {
        Log.reset(TAG, "<-- " + ByteUtils.toHexString(command));
        notifications.createNotificationChannel(this);
        notifications.show("<--" + ByteUtils.toHexString(command));

        try {
            emulation = getEmulation();
            firstInteraction = false;
            return Iso7816.RESPONSE_SUCCESS;
        } catch (AIDWrongLengthException e) {
            return Iso7816.RESPONSE_INTERNAL_ERROR;
        }
    }

    private DESFireEmulation getEmulation() throws AIDWrongLengthException {
        LibraryApplication libraryApplication = new LibraryApplication();
        return new DESFireEmulation(libraryApplication);
    }

    private byte[] getNextResponse(byte[] command) {
        Log.i(TAG, "<-- " + ByteUtils.toHexString(command));
        notifications.show("<--" + ByteUtils.toHexString(command));
        return emulation.getResponse(command);
    }

    @Override
    public void onDeactivated(int reason) {
        Log.i(TAG, "onDeactivated(). Reason: " + reason);
        firstInteraction = true;
    }

}
