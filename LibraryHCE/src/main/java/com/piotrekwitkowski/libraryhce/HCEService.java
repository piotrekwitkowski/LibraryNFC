package com.piotrekwitkowski.libraryhce;

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;

import com.piotrekwitkowski.log.Log;
import com.piotrekwitkowski.nfc.ByteUtils;
import com.piotrekwitkowski.nfc.Iso7816;
import com.piotrekwitkowski.nfc.desfire.DESFireEmulation;

public class HCEService extends HostApduService {
    private static final String TAG = "HCEService";
    private static DESFireEmulation emulation;
    private static boolean firstInteraction = true;
    private final NotificationService notifications = new NotificationService(this);

    @Override
    public byte[] processCommandApdu(byte[] command, Bundle extras) {
        byte[] response = getResponse(command);
        Log.i(TAG, "--> " + ByteUtils.toHexString(response));
        return response;
    }

    private byte[] getResponse(byte[] command) {
        if (firstInteraction) {
            Log.reset(TAG, "<-- " + ByteUtils.toHexString(command));
            notifications.createNotificationChannel(this);
            notifications.show("<--" + ByteUtils.toHexString(command));
            firstInteraction = false;
            emulation = new DESFireEmulation();
            return Iso7816.ISO7816_RESPONSE_SUCCESS;
        } else {
            Log.i(TAG, "<-- " + ByteUtils.toHexString(command));
            notifications.show("<--" + ByteUtils.toHexString(command));
            return emulation.getResponse(command);
        }
    }

    @Override
    public void onDeactivated(int reason) {
        Log.i(TAG, "onDeactivated(). Reason: " + reason);
        firstInteraction = true;
    }

}
