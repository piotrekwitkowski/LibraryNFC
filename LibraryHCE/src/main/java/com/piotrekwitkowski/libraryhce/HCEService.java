package com.piotrekwitkowski.libraryhce;

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;

import com.piotrekwitkowski.nfc.se.SEWrapper;
import com.piotrekwitkowski.log.Log;
import com.piotrekwitkowski.nfc.ByteUtils;
import com.piotrekwitkowski.nfc.Iso7816;
import com.piotrekwitkowski.nfc.se.Application;
import com.piotrekwitkowski.nfc.se.Emulation;
import com.piotrekwitkowski.nfc.desfire.InvalidParameterException;
import com.piotrekwitkowski.libraryhce.application.LibraryApplication;

public class HCEService extends HostApduService {
    private static final String TAG = "HCEService";
    private static boolean firstInteraction = true;
    private static Emulation emulation;
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
        } catch (InvalidParameterException e) {
            return Iso7816.RESPONSE_INTERNAL_ERROR;
        }
    }

    private Emulation getEmulation() throws InvalidParameterException {
        Application libraryApplication = new LibraryApplication();
        Application[] applications = new Application[] {libraryApplication};
        SEWrapper seWrapper = new SEWrapper(applications);
        return new Emulation(seWrapper);
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
