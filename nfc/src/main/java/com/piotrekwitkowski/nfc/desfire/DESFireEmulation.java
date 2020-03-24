package com.piotrekwitkowski.nfc.desfire;

import com.piotrekwitkowski.log.Log;
import com.piotrekwitkowski.nfc.desfire.states.CommandResult;
import com.piotrekwitkowski.nfc.desfire.states.State;
import com.piotrekwitkowski.nfc.desfire.states.InitialState;

public class DESFireEmulation {
    private static final String TAG = "DESFireEmulation";
    private static final byte[] EMULATION_FAILURE_RESPONSE = new byte[] {(byte) 0xEE};
    private static State state = new InitialState();

    public byte[] getResponse(byte[] apdu) {
        Log.i(TAG, "getResponse()");

        Command command = new Command(apdu);
        try {
            CommandResult result = state.processCommand(command);
            state = result.getState();
            return result.getResponse().getBytes();
        } catch (Exception e) {
//            TODO: think about changing to DESFireException
            e.printStackTrace();
            return EMULATION_FAILURE_RESPONSE;
        }
    }

}
